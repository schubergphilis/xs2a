package de.adorsys.aspsp.xs2a.service.validator.aspect;

import de.adorsys.aspsp.xs2a.component.JsonConverter;
import de.adorsys.aspsp.xs2a.service.validator.header.CommonRequestHeader;
import de.adorsys.aspsp.xs2a.service.validator.parameter.CommonRequestParameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ValidationAspect {
    private Validator validator;
    private JsonConverter jsonConverter;

    @Around("execution(* de.adorsys.aspsp.xs2a.web.*.*(..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = obtainRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        ParameterContainer parameterContainer = resolveParameters(method);

        List<String> violations = invokeValidate(parameterContainer, request);

        if(!isEmpty(violations)){
            throw new ValidationException(violations.toString());
        }
        return joinPoint.proceed();
    }

    private HttpServletRequest obtainRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private ParameterContainer resolveParameters(Method method) {
        Class<? extends CommonRequestHeader> requestHeader;
        Class<? extends CommonRequestParameter> requestParameter;

        ShouldValidate validateOnClass = method.getDeclaringClass().getAnnotation(ShouldValidate.class);
        if(validateOnClass == null){
            throw new ValidationException("Wrong request header arguments");
        }
        requestHeader = validateOnClass.header();
        requestParameter = validateOnClass.parameter();

        ShouldValidate validateOnMethod = method.getAnnotation(ShouldValidate.class);

        if(validateOnMethod != null){
            requestHeader = validateOnMethod.header();
            requestParameter = validateOnMethod.parameter();
        }
        return new ParameterContainer(requestHeader, requestParameter);
    }

    private List<String> invokeValidate(ParameterContainer parameterContainer, HttpServletRequest request) {
        CommonRequestHeader requestHeader =  jsonConverter.toObject(obtainRequestHeaders(request), parameterContainer.getRequestHeader())
                                             .orElseThrow(() -> new ValidationException("Wrong request header arguments"));

        CommonRequestParameter requestParameter = jsonConverter.toObject(obtainRequestParameters(request), parameterContainer.getRequestParameter())
                                                  .orElseThrow(() -> new ValidationException("Wrong request parameter arguments"));

        Map<String, String> headerValidate = validate(requestHeader);
        Map<String, String> parameterValidate = validate(requestParameter);

        Map<String, String> result = Stream.of(headerValidate, parameterValidate)
                                           .map(Map::entrySet)
                                           .flatMap(Collection::stream)
                                           .collect(toMap(e -> e.getKey(), e -> e.getValue()));

        return result.entrySet().stream()
               .map(entry -> entry.getKey() + " : " + entry.getValue())
               .collect(toList());
    }

    private <T> Map<String, String> validate(T source) {
        return validator.validate(source).stream()
                                            .collect(toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage));
    }

    private Map<String, String> obtainRequestHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
               .stream().collect(toMap(Function.identity(), e -> request.getHeader(e)));
    }

    private Map<String, String> obtainRequestParameters(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
               .collect(toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
    }

    @Getter
    @AllArgsConstructor
    public class ParameterContainer {
        private Class<? extends CommonRequestHeader> requestHeader;
        private Class<? extends CommonRequestParameter> requestParameter;
    }
}

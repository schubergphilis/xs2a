/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.aspsp.xs2a.service.validator;


import de.adorsys.aspsp.xs2a.service.validator.header.HeadersFactory;
import de.adorsys.aspsp.xs2a.service.validator.header.RequestHeader;
import de.adorsys.aspsp.xs2a.service.validator.header.impl.ErrorMessageHeaderImpl;
import de.adorsys.aspsp.xs2a.service.validator.parameter.ParametersFactory;
import de.adorsys.aspsp.xs2a.service.validator.parameter.RequestParameter;
import de.adorsys.aspsp.xs2a.service.validator.parameter.impl.ErrorMessageParameterImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestValidatorService {
    private final Validator validator;
    private final ParametersFactory parametersFactory;
    private final AccountExceedingService accountExceedingService;

    public Map<String, String> getRequestViolationMap(HttpServletRequest request, Object handler) {
        Map<String, String> violationMap = new HashMap<>();
        violationMap.putAll(getRequestHeaderViolationMap(request, handler));
        violationMap.putAll(getRequestParametersViolationMap(request, handler));

        return violationMap;
    }

    public Map<String, String> getRequestParametersViolationMap(HttpServletRequest request, Object handler) {

        Map<String, String> requestParameterMap = getRequestParametersMap(request);

        RequestParameter parameterImpl = parametersFactory.getParameterImpl(requestParameterMap, ((HandlerMethod) handler).getBeanType());

        if (parameterImpl instanceof ErrorMessageParameterImpl) {
            return Collections.singletonMap("Wrong parameters : ", ((ErrorMessageParameterImpl) parameterImpl).getErrorMessage());
        }

        Map<String, String> requestParameterViolationsMap = validator.validate(parameterImpl).stream()
                                                                .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage));

        return requestParameterViolationsMap;
    }

    public Map<String, String> getRequestHeaderViolationMap(HttpServletRequest request, Object handler) {

        Map<String, String> requestHeadersMap = getRequestHeadersMap(request);

        RequestHeader headerImpl = HeadersFactory.getHeadersImpl(requestHeadersMap, ((HandlerMethod) handler).getBeanType());

        if (headerImpl instanceof ErrorMessageHeaderImpl) {
            return Collections.singletonMap("Wrong header arguments: ", ((ErrorMessageHeaderImpl) headerImpl).getErrorMessage());
        }

        Map<String, String> requestHeaderViolationsMap = validator.validate(headerImpl).stream()
                                                             .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage));

        requestHeaderViolationsMap.putAll(validAcceptHeaderValue(requestHeadersMap));
        requestHeaderViolationsMap.putAll(validConsentIdHeaderValue(requestHeadersMap));

        return requestHeaderViolationsMap;
    }

    private Map<String, String> validConsentIdHeaderValue(Map<String, String> requestHeadersMap) {
        String consentIdValue = requestHeadersMap.get("consent-id");

        if (consentIdValue != null && accountExceedingService.exceededAccessPerDay(consentIdValue)) {
            return Collections.singletonMap("consentIdExceededAccess", "Wrong request");
        }

        return Collections.EMPTY_MAP;
    }

    private Map<String, String> validAcceptHeaderValue(Map<String, String> requestHeadersMap) {
        String acceptValue = requestHeadersMap.get("accept");

        if (acceptValue == null
                || acceptValue.equals("application/json")
                || acceptValue.equals("*/*")) {

            return Collections.EMPTY_MAP;
        }

        return Collections.singletonMap("acceptError", "Wrong accept");
    }

    private Map<String, String> getRequestHeadersMap(HttpServletRequest request) {

        Map<String, String> requestHeaderMap = new HashMap<>();
        if (request == null) {
            return requestHeaderMap;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            requestHeaderMap.put(key, value);
        }

        return requestHeaderMap;
    }

    private Map<String, String> getRequestParametersMap(HttpServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                   .collect(Collectors.toMap(
                       Map.Entry::getKey,
                       e -> String.join(",", e.getValue())));
    }
}

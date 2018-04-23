package de.adorsys.aspsp.xs2a.service.validator.aspect;

import de.adorsys.aspsp.xs2a.service.validator.header.CommonRequestHeader;
import de.adorsys.aspsp.xs2a.service.validator.parameter.CommonRequestParameter;
import de.adorsys.aspsp.xs2a.service.validator.parameter.DumbRequestParameter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShouldValidate {
    Class<? extends CommonRequestHeader> header();
    Class<? extends CommonRequestParameter> parameter() default DumbRequestParameter.class;
}

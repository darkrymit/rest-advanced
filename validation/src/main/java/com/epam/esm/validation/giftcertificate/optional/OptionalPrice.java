package com.epam.esm.validation.giftcertificate.optional;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Digits(integer = 19,fraction = 4)
@DecimalMin(value = "0")
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface OptionalPrice {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
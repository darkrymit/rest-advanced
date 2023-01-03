package com.epam.esm.validation.giftcertificate;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.epam.esm.validation.giftcertificate.optional.OptionalName;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@NotNull
@OptionalName
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Name {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
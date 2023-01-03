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
import org.hibernate.validator.constraints.Length;

@Length(max = 200)
@Documented
@Constraint(validatedBy = {})
@Target({ElementType.TYPE_USE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface OptionalDescription {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
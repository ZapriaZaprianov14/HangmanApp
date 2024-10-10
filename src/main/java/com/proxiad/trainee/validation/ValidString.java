package com.proxiad.trainee.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsValidStringContsraint.class)
public @interface ValidString {
  String message() default "Should contain only latin letters, spaces or commas";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

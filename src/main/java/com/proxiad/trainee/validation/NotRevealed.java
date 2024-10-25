package com.proxiad.trainee.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsWordRevealedConstraint.class)
public @interface NotRevealed {
  String message() default "The word should not be fully revealed";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

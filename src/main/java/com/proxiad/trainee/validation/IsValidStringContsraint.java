package com.proxiad.trainee.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsValidStringContsraint implements ConstraintValidator<ValidString, String> {

  @Override
  public void initialize(ValidString constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (!stringHasOnlyValidChars(value)) {
      return false;
    }
    if (!stringEndsAndBeginsWithLetter(value)) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("Has to begin or end with a letter")
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  private boolean stringHasOnlyValidChars(String value) {
    Pattern pattern = Pattern.compile("[a-zA-Z\\s\\-\\']*");
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

  private boolean stringEndsAndBeginsWithLetter(String value) {
    Character firstChar = value.charAt(0);
    Character lastChar = value.charAt(value.length() - 1);
    return Character.isLetter(firstChar) && Character.isLetter(lastChar);
  }
}

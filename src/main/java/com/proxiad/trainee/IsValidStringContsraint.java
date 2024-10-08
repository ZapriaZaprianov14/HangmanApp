package com.proxiad.trainee;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsValidStringContsraint implements ConstraintValidator<ValidString, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (!stirngHasSpecialCharacters(value)) {
      return false;
    }
    if (!stirngEndsOrBeginsWithLetter(value)) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("Word has to begin or end with a letter")
          .addConstraintViolation();
      return false;
    }
    return true;
  }

  private boolean stirngHasSpecialCharacters(String value) {
    Pattern pattern = Pattern.compile("[a-zA-Z\\s\\-\\']*");
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

  private boolean stirngEndsOrBeginsWithLetter(String value) {
    Pattern pattern = Pattern.compile("^[a-zA-Z].*[a-zA-Z]$");
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }
}

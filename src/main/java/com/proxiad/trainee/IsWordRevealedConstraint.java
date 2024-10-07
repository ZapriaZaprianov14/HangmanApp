package com.proxiad.trainee;

import static com.proxiad.trainee.Constants.IRRELEVANT_CHARACTERS;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsWordRevealedConstraint implements ConstraintValidator<NotRevealed, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return !isWholeWordRevealed(value);
  }

  private boolean isWholeWordRevealed(String word) {
    Character firstChar = word.charAt(0);
    Character lastChar = word.charAt(word.length() - 1);
    int revealedLetters = 0;
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == firstChar
          || word.charAt(i) == lastChar
          || IRRELEVANT_CHARACTERS.contains(word.charAt(i))) {
        revealedLetters++;
      }
    }
    return revealedLetters == word.length();
  }
}

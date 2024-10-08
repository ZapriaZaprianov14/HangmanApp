package com.proxiad.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewGameDTO {
  @Size(min = 3, message = "Word should be at least 3 letters")
  @ValidString
  @NotRevealed
  private String wordToGuess;

  private String gamemode;

  @NotBlank
  @Size(min = 3, message = "Category should be at least 3 letters")
  @ValidString
  private String category;

  public String getWordToGuess() {
    return wordToGuess;
  }

  public void setWordToGuess(String wordToGuess) {
    this.wordToGuess = wordToGuess;
  }

  public String getGamemode() {
    return gamemode;
  }

  public void setGamemode(String gamemode) {
    this.gamemode = gamemode;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public NewGameDTO(String wordToGuess, String gamemode, String category) {
    this.wordToGuess = wordToGuess;
    this.gamemode = gamemode;
    this.category = category;
  }

  public NewGameDTO() {}
}

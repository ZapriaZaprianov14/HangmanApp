package com.proxiad.trainee;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class NewGameDTO {
  @NotRevealed
  @Size(min = 3, message = "Word should be at least 3 letters")
  @Pattern(
      regexp = "[a-zA-Z\\s\\-\\']*",
      message = "Invalid word: must only contain letters, whitespaces or dashes")
  @Pattern(
      regexp = "^[a-zA-Z][a-zA-Z]$",
      message = "Invalid word: cannot begin or end with a special character")
  private String wordToGuess;

  private String gamemode;

  @NotNull
  @Size(min = 3, message = "Category should be at least 3 letters")
  @Pattern(
      regexp = "[a-zA-Z\\s\\-\\']*",
      message = "Invalid category: must only contain letters, whitespaces or dashes")
  @Pattern(
      regexp = "^[a-zA-Z][a-zA-Z]$",
      message = "Invalid category: cannot begin or end with a special character")
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

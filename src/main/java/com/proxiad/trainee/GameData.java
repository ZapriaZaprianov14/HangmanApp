package com.proxiad.trainee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.proxiad.trainee.enums.GamemodeEnum;

public class GameData implements Serializable {
  /** */
  private static final long serialVersionUID = 1L;

  private UUID id;
  private int lives;
  private int correctlyGuessedLetters;
  private int irrelevantCharacters; // whitespaces _ and - are irrelevant
  private int guessesMade;
  private String word;
  private String wordProgress;
  private List<Character> unguessedLetters;
  private Set<Character> rightGuesses;
  private String category;
  private GamemodeEnum gamemode;
  private boolean gameWon;
  private boolean finished;

  public int getIrrelevantCharacters() {
    return irrelevantCharacters;
  }

  public void setIrrelevantCharacters(int irrelevantCharacters) {
    this.irrelevantCharacters = irrelevantCharacters;
  }

  public boolean isGameWon() {
    return gameWon;
  }

  public int getGuessesMade() {
    return guessesMade;
  }

  public void setGuessesMade(int guessesMade) {
    this.guessesMade = guessesMade;
  }

  public void setGameWon(boolean gameWon) {
    this.gameWon = gameWon;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public GamemodeEnum getGamemode() {
    return gamemode;
  }

  public void setGamemode(GamemodeEnum gamemode) {
    this.gamemode = gamemode;
  }

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public String getWordProgress() {
    return wordProgress;
  }

  public void setWordProgress(String wordProgress) {
    this.wordProgress = wordProgress;
  }

  public int getLives() {
    return lives;
  }

  public void setLives(int lives) {
    this.lives = lives;
  }

  public Set<Character> getRightGuesses() {
    return rightGuesses;
  }

  public void setRightGuesses(Set<Character> rightGuesses) {
    this.rightGuesses = rightGuesses;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public List<Character> getUnguessedLetters() {
    return unguessedLetters;
  }

  public void setUnguessedLetters(List<Character> unguessedLetters) {
    this.unguessedLetters = unguessedLetters;
  }

  public int getCorrectlyGuessedLetters() {
    return correctlyGuessedLetters;
  }

  public void setCorrectlyGuessedLetters(int correctlyGuessedLetters) {
    this.correctlyGuessedLetters = correctlyGuessedLetters;
  }

  public GameData() {
    this.id = UUID.randomUUID();
    this.unguessedLetters =
        new ArrayList<Character>(
            Arrays.asList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    this.rightGuesses = new HashSet<Character>();
  }

  public GameData(String word, String category, GamemodeEnum gamemode, int lives) {
    // id should be a sequence
    // auto increment
    // or time stamp
    setId(UUID.randomUUID());
    setUnguessedLetters(
        new ArrayList<Character>(
            Arrays.asList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')));

    setRightGuesses(new HashSet<Character>());
    setWord(word);
    setCategory(category);
    setGamemode(gamemode);
    setLives(lives);
  }
}

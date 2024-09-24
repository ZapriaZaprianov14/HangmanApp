package com.proxiad.trainee;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

@Service
public class GameServiceImpl implements GameService {

  private static final int MAX_LIVES = 9;

  @Autowired private GameRepository gameRepository;
  @Autowired private WordGeneratorService generatorService;

  @Override
  public GameData makeTry(GameData game, String guess, HttpSession session) {
    if (guess == null) {
      return game;
    }
    String word = game.getWord();
    Character charToGuess = guess.charAt(0);
    if (word.contains(guess) && game.getUnguessedLetters().contains(charToGuess)) {
      game.getRightGuesses().add(charToGuess);
    } else if (game.getUnguessedLetters().contains(charToGuess)) {
      game.setLives(game.getLives() - 1);
    }
    game.getUnguessedLetters().remove(charToGuess);
    game.setGuessesMade(game.getGuessesMade() + 1);

    game.setCorrectlyGuessedLetters(getCorrectlyGuessedLetters(game));
    if (game.getCorrectlyGuessedLetters() + game.getIrrelevantCharacters()
        >= game.getWord().length()) {
      game.setFinished(true);
      game.setGameWon(true);
      game.setWordProgress(word);
      gameRepository.saveGame(game, session);
      return game;
    }
    if (game.getLives() <= 0) {
      game.setFinished(true);
      game.setGameWon(false);
      game.setWordProgress(word);
      gameRepository.saveGame(game, session);
      return game;
    }

    game.setWordProgress(getWordProgress(game));
    gameRepository.setCurrentGame(game, session);
    return game;
  }

  @Override
  public GameData getGame(UUID id, HttpSession session) {
    return gameRepository.getGame(id, session);
  }

  @Override
  public GameData startNewGame(String category, String wordToGuess, HttpSession sessionn)
      throws InvalidWordException {
    CategoryEnum categoryEnum = CategoryEnum.valueOf(category.toUpperCase());
    GamemodeEnum gamemode;
    if (wordToGuess == null) {
      wordToGuess = getRandomWord(category);
      gamemode = GamemodeEnum.SINGLEPLAYER;
    } else {
      gamemode = GamemodeEnum.MULTIPLAYER;
    }
    validateWord(wordToGuess);
    wordToGuess = wordToGuess.toUpperCase();
    GameData game = new GameData(wordToGuess, categoryEnum, gamemode, MAX_LIVES);
    return initializeaGame(wordToGuess, game, sessionn);
  }

  @Override
  public GameData getCurrentGame(HttpSession session) {
    return gameRepository.getCurrentGame(session);
  }

  @Override
  public void leaveGame(HttpSession session) {
    gameRepository.leaveGame(session);
  }

  @Override
  public GameData resumeGame(UUID id, HttpSession session) {
    GameData game = gameRepository.resumeGame(id, session);
    return game;
  }

  @Override
  public List<GameData> getAllGames(HttpSession session) {
    return gameRepository.getAllGames(session);
  }

  private GameData initializeaGame(String word, GameData game, HttpSession session) {
    Character firstChar = word.charAt(0);
    Character lastChar = word.charAt(word.length() - 1);

    for (int i = 0; i < word.length(); i++) {
      char currChar = word.charAt(i);
      if (currChar == ' ' || currChar == '-' || currChar == '_') {
        game.setIrrelevantCharacters(game.getIrrelevantCharacters() + 1);
      }
    }

    makeTry(game, firstChar.toString(), session);
    makeTry(game, lastChar.toString(), session);

    return game;
  }

  private String getWordProgress(GameData game) {
    StringBuilder sb = new StringBuilder();
    String wordToGuess = game.getWord();
    char firstChar = game.getWord().charAt(0);
    sb.append(firstChar);
    for (int i = 1; i < game.getWord().length(); i++) {
      char currChar = wordToGuess.charAt(i);
      if (game.getRightGuesses().contains(currChar)) {
        sb.append(' ');
        sb.append(currChar);
        sb.append(' ');
      } else if (currChar == ' ' || currChar == '-' || currChar == '_') {
        sb.append(' ');
        sb.append(currChar);
        sb.append(' ');
      } else {
        sb.append(' ');
        sb.append('_');
        sb.append(' ');
      }
    }
    return sb.toString();
  }

  private int getCorrectlyGuessedLetters(GameData game) {
    String word = game.getWord();
    Set<Character> rightGuesses = game.getRightGuesses();
    int guessedLetters = 0;
    for (int i = 0; i < word.length(); i++) {
      if (rightGuesses.contains(word.charAt(i))) {
        guessedLetters++;
      }
    }
    return guessedLetters;
  }

  private String getRandomWord(String category) {
    return generatorService.createRandomWord(category);
  }

  private void validateWord(String word) throws InvalidWordException {
    Character lastChar = word.charAt(word.length() - 1);
    if (!Character.isLetter(lastChar)) {
      throw new InvalidWordException("Word should end with a letter");
    }
    if (word.length() <= 2) {
      throw new InvalidWordException(
          "Word should hane at least 3 characters. Word entered: " + word);
    }
    String regex = "[a-zA-Z\\s-]+";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(word);
    if (!matcher.matches()) {
      throw new InvalidWordException(
          "Word should contain only letters, whitespaces or dashes." + " Word entered: " + word);
    }

    if (isWholeWordRevealed(word)) {
      throw new InvalidWordException(
          "At least one of the letters should not be revealed at the beginning of the game."
              + "The first and last letters are revealed. Word entered: "
              + word);
    }
  }

  private boolean isWholeWordRevealed(String word) {
    Character firstChar = word.charAt(0);
    Character lastChar = word.charAt(word.length() - 1);
    int revealedLetters = 0;
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == firstChar
          || word.charAt(i) == lastChar
          || word.charAt(i) == ' '
          || word.charAt(i) == '-') {
        revealedLetters++;
      }
    }
    return revealedLetters == word.length();
  }
}
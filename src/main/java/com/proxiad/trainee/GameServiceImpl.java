package com.proxiad.trainee;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.servlet.http.HttpSession;
import static com.proxiad.trainee.Constants.IRRELEVANT_CHARACTERS;
;

public class GameServiceImpl implements GameService {

  private GameRepository gameRepository;
  private WordGeneratorService wordGeneratorService;

  public GameServiceImpl() {}

  @Override
  public GameData makeTry(GameData game, String guess, HttpSession session)
      throws InvalidGuessException {
    if (!Character.isLetter(guess.charAt(0)) || guess.length() > 1) {
      throw new InvalidGuessException("Your guess was invalid. The guess should be a letter.");
    }

    String word = game.getWord();
    guess = guess.toUpperCase();
    Character charToGuess = guess.charAt(0);
    if (word.contains(guess) && game.getUnguessedLetters().contains(charToGuess)) {
      game.getRightGuesses().add(charToGuess);
    } else if (game.getUnguessedLetters().contains(charToGuess)) {
      game.setLives(game.getLives() - 1);
    }
    game.getUnguessedLetters().remove(charToGuess);
    game.setGuessesMade(game.getGuessesMade() + 1);
    game.setCorrectlyGuessedLetters(getCorrectlyGuessedLetters(game));

    boolean gameWon = isWholeWordGuessed(game);
    if (gameWon || game.getLives() <= 0) {
      return finalizeGame(game, gameWon, session);
    }

    game.setWordProgress(getWordProgress(game));
    gameRepository.setCurrentGame(game, session);
    return game;
  }

  private GameData finalizeGame(GameData game, boolean gameWon, HttpSession session) {
    game.setFinished(true);
    game.setGameWon(gameWon);
    game.setWordProgress(game.getWord());
    gameRepository.saveGame(game, session);
    return game;
  }

  private boolean isWholeWordGuessed(GameData game) {
    return game.getCorrectlyGuessedLetters() + game.getIrrelevantCharacters()
        >= game.getWord().length();
  }

  @Override
  public GameData getGame(UUID id, HttpSession session) {
    return gameRepository.getGame(id, session);
  }

  @Override
  public GameData startNewGame(String category, String wordToGuess, HttpSession sessionn)
      throws InvalidWordException, InvalidCategoryException {
    if (category == null || isCategoryInvalid(category)) {
      throw new InvalidCategoryException("The given category is invalid.");
    }
    category = category.toUpperCase();
    CategoryEnum categoryEnum = CategoryEnum.valueOf(category);
    GamemodeEnum gamemode;
    if (wordToGuess == null) {
      wordToGuess = getRandomWord(category);
      gamemode = GamemodeEnum.SINGLEPLAYER;
    } else {
      gamemode = GamemodeEnum.MULTIPLAYER;
    }
    validateWord(wordToGuess);
    wordToGuess = wordToGuess.toUpperCase();
    GameData game = new GameData(wordToGuess, categoryEnum, gamemode, Constants.MAX_LIVES);
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
    return gameRepository.resumeGame(id, session);
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

    game.getRightGuesses().add(firstChar);
    game.getRightGuesses().add(lastChar);

    game.getUnguessedLetters().remove(firstChar);
    game.getUnguessedLetters().remove(lastChar);

    game.setWordProgress(getWordProgress(game));
    gameRepository.setCurrentGame(game, session);

    return game;
  }

  private String getWordProgress(GameData game) {
    StringBuilder sb = new StringBuilder();
    String wordToGuess = game.getWord();
    char firstChar = game.getWord().charAt(0);
    sb.append(firstChar);
    for (int i = 1; i < game.getWord().length(); i++) {
      char currChar = wordToGuess.charAt(i);
      if (game.getRightGuesses().contains(currChar) || IRRELEVANT_CHARACTERS.contains(currChar)) {
        sb.append(' ');
        sb.append(currChar);
      } else {
        sb.append(' ');
        sb.append('_');
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
    return wordGeneratorService.createRandomWord(category);
  }

  private void validateWord(String word) throws InvalidWordException {
    Character lastChar = word.charAt(word.length() - 1);
    if (word.length() <= 2) {
      throw new InvalidWordException(
          "Word should have at least 3 characters. Word entered: " + word);
    }
    if (!Character.isLetter(lastChar)) {
      throw new InvalidWordException("Word should end with a letter. Word entered: " + word);
    }
    String regex = "[a-zA-Z\\s-]+";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(word);
    if (!matcher.matches()) {
      throw new InvalidWordException(
          "Word should contain only letters, whitespaces or dashes." + " Word entered: " + word);
    }

    if (isWholeWordRevealed(word.toUpperCase())) {
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
          || IRRELEVANT_CHARACTERS.contains(word.charAt(i))) {
        revealedLetters++;
      }
    }
    return revealedLetters == word.length();
  }

  public void setGameRepository(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public void setWordGeneratorService(WordGeneratorService wordGeneratorService) {
    this.wordGeneratorService = wordGeneratorService;
  }

  @Override
  public boolean containsFinishedGames(List<GameData> games) {
    return games.stream().anyMatch(game -> game.isFinished());
  }

  @Override
  public boolean containsOngoingGames(List<GameData> games) {
    return games.stream().anyMatch(game -> !game.isFinished());
  }

  @Override
  public List<GameData> reverseListOfGames(List<GameData> games) {
    List<GameData> result = new ArrayList<>();
    for (int i = games.size() - 1; i >= 0; i--) {
      result.add(games.get(i));
    }
    return result;
  }

  public boolean isCategoryInvalid(String value) {
    try {
      CategoryEnum.valueOf(value.toUpperCase());
      return false;
    } catch (IllegalArgumentException e) {
      return true;
    }
  }
}

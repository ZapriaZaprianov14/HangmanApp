package com.proxiad.trainee;

import static com.proxiad.trainee.Constants.IRRELEVANT_CHARACTERS;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proxiad.trainee.enums.GamemodeEnum;
import com.proxiad.trainee.exceptions.GameNotFoundException;
import com.proxiad.trainee.exceptions.InvalidCategoryException;
import com.proxiad.trainee.exceptions.InvalidGuessException;
import com.proxiad.trainee.exceptions.InvalidWordException;
import com.proxiad.trainee.interfaces.GameRepository;
import com.proxiad.trainee.interfaces.GameService;
import com.proxiad.trainee.interfaces.WordGeneratorService;
import jakarta.servlet.http.HttpSession;

@Service
public class GameServiceImpl implements GameService {

  @Autowired private GameRepository gameRepository;
  @Autowired private WordGeneratorService wordGeneratorService;

  public GameServiceImpl() {}

  @Override
  public GameData makeTry(GameData game, String guess, HttpSession session)
      throws InvalidGuessException, GameNotFoundException {
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

  private GameData finalizeGame(GameData game, boolean gameWon, HttpSession session)
      throws GameNotFoundException {
    game.setFinished(true);
    game.setGameWon(gameWon);
    game.setWordProgress(game.getWord());
    gameRepository.leaveGame(session);
    return game;
  }

  private boolean isWholeWordGuessed(GameData game) {
    return game.getCorrectlyGuessedLetters() + game.getIrrelevantCharacters()
        >= game.getWord().length();
  }

  @Override
  public GameData getGame(UUID id, HttpSession session) throws GameNotFoundException {
    return gameRepository.getGame(id, session);
  }

  @Override
  public GameData startNewGame(NewGameDTO gameDTO, HttpSession session) {
    String category = gameDTO.getCategory().toUpperCase();
    String wordToGuess = gameDTO.getWordToGuess();
    GamemodeEnum gamemode = GamemodeEnum.valueOf(gameDTO.getGamemode());
    if (wordToGuess == null) {
      wordToGuess = getRandomWord(category);
    }
    wordToGuess = wordToGuess.toUpperCase();
    GameData game = new GameData(wordToGuess, category, gamemode, Constants.MAX_LIVES);
    return initializeGame(wordToGuess, game, session);
  }

  @Override
  public GameData getCurrentGame(HttpSession session) throws GameNotFoundException {
    return gameRepository.getCurrentGame(session);
  }

  @Override
  public void leaveGame(HttpSession session) throws GameNotFoundException {
    gameRepository.leaveGame(session);
  }

  @Override
  public GameData resumeGame(UUID id, HttpSession session) throws GameNotFoundException {
    return gameRepository.resumeGame(id, session);
  }

  @Override
  public List<GameData> getAllGames(HttpSession session) {
    return gameRepository.getAllGames(session);
  }

  private GameData initializeGame(String word, GameData game, HttpSession session) {
    Character firstChar = word.charAt(0);
    Character lastChar = word.charAt(word.length() - 1);

    for (int i = 0; i < word.length(); i++) {
      char currChar = word.charAt(i);
      if (IRRELEVANT_CHARACTERS.contains(currChar)) {
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

  public void setGameRepository(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  public void setWordGeneratorService(WordGeneratorService wordGeneratorService) {
    this.wordGeneratorService = wordGeneratorService;
  }
}

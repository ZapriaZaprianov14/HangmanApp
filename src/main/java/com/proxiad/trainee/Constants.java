package com.proxiad.trainee;

import java.util.Arrays;
import java.util.List;

public class Constants {
  public static final int MAX_LIVES = 9;
  public static final List<Character> IRRELEVANT_CHARACTERS = Arrays.asList(' ', '-', '\'');
  public static final String CURRENT_GAME = "currentGame";
  public static final String PREVIOUS_GAMES = "previousGames";
  public static final String GAMES_CONTROLLER_URL = "/HangmanApp/games";
  public static final String HOME_URL = "/HangmanApp/home";
  public static final String ONGOING_URL = "/HangmanApp/ongoing";
  public static final String HISTORY_URL = "/HangmanApp/history";
  public static final String MULTIPLAYER_URL = "/HangmanApp/multiplayer";
}

package com.proxiad.trainee;

import java.util.Arrays;
import java.util.List;

public class Constants {
  public static final int MAX_LIVES = 9;
  public static final List<Character> IRRELEVANT_CHARACTERS = Arrays.asList(' ', '-', '\'');
  public static final String CURRENT_GAME = "currentGame";
  public static final String PREVIOUS_GAMES = "previousGames";
  public static final String GAMES_CONTROLLER_URL = "/HangmanApp/api/games";
  public static final String HOME_URL = "/HangmanApp/api/home";
  public static final String ONGOING_URL = "/HangmanApp/api/ongoing";
  public static final String HISTORY_URL = "/HangmanApp/api/history";
  public static final String MULTIPLAYER_URL = "/HangmanApp/api/multiplayer";
  public static final char[][] QWERTY_KEYBOARD = {
    {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'},
    {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'},
    {'Z', 'X', 'C', 'V', 'B', 'N', 'M'}
  };
}

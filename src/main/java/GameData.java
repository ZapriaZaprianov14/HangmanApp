import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GameData implements Serializable {
  /** */
  private static final long serialVersionUID = 1L;

  private UUID id;
  private int lives;
  private int guessedLetters;
  private List<Character> unguessedLetters;
  private Set<Character> rightGuesses;
  private String word;
  private String wordProgress;
  private Category category;
  private boolean gameWon;
  private int irrelevantCharacters;

  public int getIrrelevantCharacters() {
    return irrelevantCharacters;
  }

  public void setIrrelevantCharacters(int irrelevantCharacters) {
    this.irrelevantCharacters = irrelevantCharacters;
  }

  public boolean isGameWon() {
    return gameWon;
  }

  public void setGameWon(boolean gameWon) {
    this.gameWon = gameWon;
  }

  public Category getCategory() {
  return category;}

  public void setCategory(Category category) {
  this.category = category;}

  private boolean finished;

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

  public int getGuessedLetters() {
    return guessedLetters;
  }

  public void setGuessedLetters(int guessedLetters) {
    this.guessedLetters = guessedLetters;
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

  public GameData() {
    id = UUID.randomUUID();
    this.lives = 9;
    this.guessedLetters = 0;
    this.irrelevantCharacters = 0;
    this.unguessedLetters =
        new ArrayList<Character>(
            Arrays.asList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    this.rightGuesses = new HashSet<Character>();
  }

  @Override
  public String toString() {
    return "GameData [id="
        + id
        + ", lives="
        + lives
        + ", guessedLetters="
        + guessedLetters
        + ", unguessedLetters="
        + unguessedLetters
        + ", rightGuesses="
        + rightGuesses
        + ", word="
        + word
        + ", wordProgress="
        + wordProgress
        + ", category="
        + category
        + ", gameWon="
        + gameWon
        + ", irrelevantCharacters="
        + irrelevantCharacters
        + ", finished="
        + finished
        + "]";
  }
}

import java.io.IOException;
import java.util.List;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/game")
public class GameServlet extends HttpServlet {
  private GameData gameData;

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    HttpSession session = request.getSession();

    this.gameData = (GameData) session.getAttribute("currentGameData");
    String guess = request.getParameter("guess");
    
    if (gameData == null) {
      System.out.println("Redirecting" ); 
      response.sendRedirect("index-redirect");
      return;
    }

    this.guessLetter(guess);

    gameData.setWordProgress(getWordProgress());

    if (gameData.getLives() <= 0) {
      // Game lost.
      gameData.setFinished(true);
      gameData.setGameWon(false);
      session.removeAttribute("currentGameData");
      List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");
      previousGames.add(gameData);
      request.setAttribute("message", "The word was: " + gameData.getWord());

      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/loss.jsp");
      dispatcher.forward(request, response);
      return;

    } else if (gameData.getGuessedLetters()
        >= gameData.getWord().length() - gameData.getIrrelevantCharacters()) {
      // Game won.
      gameData.setFinished(true);
      gameData.setGameWon(true);
      session.removeAttribute("currentGameData");
      gameData.setWordProgress(gameData.getWord());
      List<GameData> previousGames = (List<GameData>) session.getAttribute("previousGames");
      previousGames.add(gameData);

      request.setAttribute(
          "message", "You have successfuly guessed the word " + gameData.getWord());
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/win.jsp");
      dispatcher.forward(request, response);
      return;
    }

    session.setAttribute("currentGameData", gameData);

    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/game.jsp");
    dispatcher.forward(request, response);
  }

  //if the user types it into the browser
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/game.jsp");
    dispatcher.forward(request, response);
  }

  private String getWordProgress() {
    StringBuilder sb = new StringBuilder();
    String wordToGuess = gameData.getWord();
    char firstChar = gameData.getWord().charAt(0);
    sb.append(firstChar);
    int currentGuessed = 1;
    for (int i = 1; i < gameData.getWord().length(); i++) {
      char currChar = wordToGuess.charAt(i);
      if (gameData.getRightGuesses().contains(currChar)) {
        sb.append(' ');
        sb.append(currChar);
        sb.append(' ');
        currentGuessed++;
      } else if (currChar == ' ' || currChar == '-') {
        sb.append(' ');
        sb.append(currChar);
        sb.append(' ');
      } else {
        sb.append(' ');
        sb.append('_');
        sb.append(' ');
      }
    }
    gameData.setGuessedLetters(currentGuessed);
    return sb.toString();
  }

  private void guessLetter(String letter) {
    if (letter == null) {
      return;
    }
    String word = gameData.getWord();
    Character charToGuess = letter.charAt(0);
    if (word.contains(letter) && gameData.getUnguessedLetters().contains(charToGuess)) {

      gameData.getRightGuesses().add(charToGuess);
      gameData.getUnguessedLetters().remove(charToGuess);
    } else if (gameData.getUnguessedLetters().contains(charToGuess)) {
      gameData.getUnguessedLetters().remove(charToGuess);
      gameData.setLives(gameData.getLives() - 1);
    }
  }
}

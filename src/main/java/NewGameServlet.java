import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/new-game")
public class NewGameServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    initializeGame(request, response);
    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/game");
    dispatcher.forward(request, response);
  }

  // if user types into the browser
  // it does not save the game
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.sendRedirect("index-redirect");
    return;
  }

  private void initializeGame(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    String category = request.getParameter("category");

    // configurable from the initializingContextListener
    int maxLives = (int) session.getServletContext().getAttribute("maxLives");
    GameData gameData = new GameData();
    gameData.setLives(maxLives);
    Random random = new Random();

    try {
      JSONParser parser = new JSONParser();
      InputStream inputStream =
          NewGameServlet.class.getClassLoader().getResourceAsStream("words_data.json");
      if (inputStream == null) {
        System.out.println("File not found!");
        return;
      }

      JSONObject data = (JSONObject) parser.parse(new InputStreamReader(inputStream));
      List<String> words = new ArrayList<>((JSONArray) data.get(category.toLowerCase()));
      gameData.setWord(words.get(random.nextInt(words.size())));
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    gameData.setCategory(CategoryEnum.valueOf(category.toUpperCase()));
    gameData.setGamemode(GamemodeEnum.SINGLEPLAYER);
    String wordToGuess = gameData.getWord();
    Character firstChar = wordToGuess.charAt(0);
    Character lastChar = wordToGuess.charAt(wordToGuess.length() - 1);

    gameData.getUnguessedLetters().remove(firstChar);
    gameData.getUnguessedLetters().remove(lastChar);

    gameData.getRightGuesses().add(firstChar);
    gameData.getRightGuesses().add(lastChar);

    for (int i = 0; i < wordToGuess.length(); i++) {
      char currChar = wordToGuess.charAt(i);
      if (currChar == ' ' || currChar == '-' || currChar == '_') {
        gameData.setIrrelevantCharacters(gameData.getIrrelevantCharacters() + 1);
      }
    }
    session.setAttribute("currentGameData", gameData);
  }
}

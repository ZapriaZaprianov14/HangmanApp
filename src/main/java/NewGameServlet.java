import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
    HttpSession session = request.getSession();

    if (session.getAttribute("previousGames") == null) {
      HashMap<UUID, GameData> previousGames = new HashMap<UUID, GameData>();
      session.setAttribute("previousGames", previousGames);
    }
    initializeGame(request);
    session.setAttribute(
        "alphabet",
        new ArrayList<Character>(
            Arrays.asList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')));
    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/game");
    dispatcher.forward(request, response);
  }

  private void initializeGame(HttpServletRequest request) {

    HttpSession session = request.getSession();

    String category = request.getParameter("category");
    GameData gameData = new GameData();
    Random random = new Random();

    try {
      JSONParser parser = new JSONParser();
      JSONObject data =
          (JSONObject)
              parser.parse(
                  new FileReader(
                      "C:/Users/z.zapryanov/eclipse-workspace/HangmanApp/src/main/resources/words/words_data.json"));
      List<String> words = new ArrayList<>((JSONArray) data.get(category));

      gameData.setWord(words.get(random.nextInt(words.size())));
      gameData.setCategory(category);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    String wordToGuess = gameData.getWord();
    Character firstChar = wordToGuess.charAt(0);
    Character lastChar = wordToGuess.charAt(wordToGuess.length() - 1);

    gameData.getUnguessedLetters().remove(firstChar);
    gameData.getUnguessedLetters().remove(lastChar);

    gameData.getRightGuesses().add(firstChar);
    gameData.getRightGuesses().add(lastChar);

    for (int i = 0; i < wordToGuess.length(); i++) {
      char currChar = wordToGuess.charAt(i);
      if (currChar == ' ' || currChar == '-') {
        gameData.setIrrelevantCharacters(gameData.getIrrelevantCharacters() + 1);
      }
    }
    session.setAttribute("currentGameData", gameData);
  }
}

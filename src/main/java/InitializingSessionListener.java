import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class InitializingSessionListener implements HttpSessionListener {
  @Override
  public void sessionCreated(HttpSessionEvent hse) {
    HttpSession session = hse.getSession();
    List<GameData> previousGames = new ArrayList<GameData>();
    session.setAttribute("previousGames", previousGames);
  }
}

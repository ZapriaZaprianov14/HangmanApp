package com.proxiad.trainee;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

@Service
public class WordGeneratorService {

  public String createRandomWord(String category) {
    try {
      Random random = new Random();
      JSONParser parser = new JSONParser();
      InputStream inputStream =
          NewGameServlet.class.getClassLoader().getResourceAsStream("words_data.json");
      if (inputStream == null) {
        System.out.println("File not found!");
        return null;
      }

      JSONObject data = (JSONObject) parser.parse(new InputStreamReader(inputStream));
      List<String> words = new ArrayList<String>((JSONArray) data.get(category.toUpperCase()));
      return words.get(random.nextInt(words.size()));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

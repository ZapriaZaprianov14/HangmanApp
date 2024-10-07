package com.proxiad.trainee;

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
import org.springframework.stereotype.Service;
import com.proxiad.trainee.interfaces.WordGeneratorService;

@Service
public class WordGeneratorServiceImpl implements WordGeneratorService {

  @Override
  public String createRandomWord(String category) {
    Random random = new Random();
    List<String> words = getAllWordsFromCategory(category);
    return words.get(random.nextInt(words.size()));
  }

  public List<String> getAllWordsFromCategory(String category) {
    try {
      JSONParser parser = new JSONParser();
      InputStream inputStream =
          GameController.class.getClassLoader().getResourceAsStream("words_data.json");
      if (inputStream == null) {
        System.out.println("File not found!");
        return null;
      }

      JSONObject data = (JSONObject) parser.parse(new InputStreamReader(inputStream));
      List<String> words = new ArrayList<String>((JSONArray) data.get(category.toUpperCase()));
      return words;
    } catch (ParseException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

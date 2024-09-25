package com.proxiad.trainee;

import java.util.List;

public interface WordGeneratorService {
  String createRandomWord(String category);

  List<String> getAllWordsFromCategory(String category);
}

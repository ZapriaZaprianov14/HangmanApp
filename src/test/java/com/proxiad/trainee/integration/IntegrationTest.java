package com.proxiad.trainee.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

// should be called something more concrete
// all test methods begin with testCamelCase
public class IntegrationTest {

  private WebDriver driver;
  private HomePage homePage;
  private OngoingPage ongoingPage;
  private GamePage gamePage;
  private MultiplayerInputPage multiplayerInputPage;
  private WinPage winPage;
  private LosePage losePage;
  private HistoryPage historyPage;

  private static final String DEFAULT_WORD = "mazda";
  private static final String DEFAULT_CATEGORY = "cars";

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    driver.get("http://localhost:8080/HangmanApp/api/v1/home");
    homePage = new HomePage(driver);
    ongoingPage = new OngoingPage(driver);
    gamePage = new GamePage(driver);
    historyPage = new HistoryPage(driver);
    multiplayerInputPage = new MultiplayerInputPage(driver);
    winPage = new WinPage(driver);
    losePage = new LosePage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void testEmptyHistoryPageGoesBackHome() {
    homePage.historyButton.click();
    assertTrue(historyPage.noGamesMessage.getText().equals("No finished games"));
    historyPage.homeBtn.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void testEmptyOngoingPageGoesBackHome() {
    homePage.ongoingButton.click();
    assertTrue(ongoingPage.noGamesMessage.getText().equals("No ongoing games"));
    ongoingPage.homeBtn.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void testCreatesNewGame() {
    homePage.singlePlayerButton.click();
    assertGameStartedCorrectly();
    gamePage.leaveButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void testOpensMultiplayerInputJSP() {
    homePage.multiplayerButton.click();
    assertTrue(
        multiplayerInputPage
            .messageToPlayer1
            .getText()
            .equals("Player 1 enter the word and category"));
  }

  @Test
  public void testCreatesMultiplayerGameWithValidData() {
    homePage.multiplayerButton.click();
    multiplayerInputPage.enterWord(DEFAULT_WORD);
    multiplayerInputPage.enterCategory(DEFAULT_CATEGORY);
    multiplayerInputPage.enterButton.click();
    assertMultiplayerGameStartedCorrectly();
  }

  @Test
  public void testCreatesGameWithWordBegginingAndEngingInSameLetter() {
    homePage.multiplayerButton.click();
    multiplayerInputPage.enterWord("aba");
    multiplayerInputPage.enterCategory(DEFAULT_CATEGORY);
    multiplayerInputPage.enterButton.click();
    assertTrue(gamePage.wordProgress.getText().contains("_"));
    assertTrue(gamePage.disabledButtons.get(0).getAttribute("id").equals("letter-A"));
    assertTrue(gamePage.messagePlayer2.getText().equals("Player 2 guess the word"));
  }

  @Test
  public void testMultiplayerInputValidatesRevealedWord() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("aaabbb", "validCategory");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("The word should not be fully revealed"));
  }

  @Test
  public void testMultiplayerInputValidatesInvalidWordAndCategory() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("invalid1Word", "invalid2Category");
    assertTrue(
        multiplayerInputPage
            .wordError
            .getText()
            .equals("Should contain only latin letters, spaces or commas"));
    assertTrue(
        multiplayerInputPage
            .categoryError
            .getText()
            .equals("Should contain only latin letters, spaces or commas"));
  }

  @Test
  public void testMultiplayerInputValidatesWordAndCategoryEnding() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("invalidWord ", "invalidCategory2");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("Has to begin or end with a letter"));
    assertTrue(
        multiplayerInputPage.categoryError.getText().equals("Has to begin or end with a letter"));
  }

  @Test
  public void testMultiplayerInputValidatesWordAndCategoryBeginning() {
    homePage.multiplayerButton.click();
    enterWordAndCategory(" invalidWord", "2invalidCategory");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("Has to begin or end with a letter"));
    assertTrue(
        multiplayerInputPage.categoryError.getText().equals("Has to begin or end with a letter"));
  }

  @Test
  public void testMultiplayerInputValidatesShortWordAndCategory() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("aa", "bb");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("Word should be at least 3 letters"));
    assertTrue(
        multiplayerInputPage
            .categoryError
            .getText()
            .equals("Category should be at least 3 letters"));
  }

  @Test
  public void testWinsGameCorrectly() {
    homePage.multiplayerButton.click();
    enterWordAndCategory(DEFAULT_WORD, DEFAULT_CATEGORY);
    gamePage.clickLetterButton('D');

    assertTrue(gamePage.wordProgress.getText().equals("M A _ D A"));
    assertTrue(gamePage.disabledButtons.size() == 3);
    gamePage.clickLetterButton('Z');
    assertTrue(
        winPage
            .winningMessagElement
            .getText()
            .equals("You have successfully guessed the word: MAZDA"));

    winPage.homeButtonElement.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void testLosesGameCorrectly() {
    homePage.multiplayerButton.click();
    enterWordAndCategory(DEFAULT_WORD, DEFAULT_CATEGORY);
    gamePage.clickLetterButton('X');
    gamePage.clickLetterButton('C');
    gamePage.clickLetterButton('V');
    gamePage.clickLetterButton('B');
    gamePage.clickLetterButton('N');
    gamePage.clickLetterButton('S');
    gamePage.clickLetterButton('F');
    gamePage.clickLetterButton('G');

    assertTrue(gamePage.disabledButtons.size() == 10);
    assertTrue(gamePage.wordProgress.getText().equals("M A _ _ A"));

    gamePage.clickLetterButton('H');
    assertThat(losePage.loosingMessagElement.getText().equals("The word was: MAZDA"));
    losePage.homeButtonElement.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  private void enterWordAndCategory(String word, String category) {
    multiplayerInputPage.enterWord(word);
    multiplayerInputPage.enterCategory(category);
    multiplayerInputPage.enterButton.click();
  }

  @Test
  public void testOpensOngoingWhereGamesPresent() {
    int numberOfGames = 3;
    createOnoingGames(numberOfGames);
    homePage.ongoingButton.click();
    assertTrue(ongoingPage.gameRows.size() == numberOfGames);
  }

  @Test
  public void testResumeGameOpensGamePage() {
    createOnoingGames(2);
    homePage.ongoingButton.click();
    ongoingPage.resumeButtons.get(1).click();
    assertGameStartedCorrectly();
  }

  private void createOnoingGames(int times) {
    for (int i = 0; i < times; i++) {
      homePage.singlePlayerButton.click();
      gamePage.leaveButton.click();
    }
  }

  private void assertGameStartedCorrectly() {
    assertTrue(gamePage.wordProgress.getText().contains("_"));
    assertTrue(gamePage.disabledButtons.size() == 2);
    assertTrue(gamePage.gameCategory.getText().equals("Category: CARS"));
  }

  private void assertMultiplayerGameStartedCorrectly() {
    assertTrue(gamePage.wordProgress.getText().contains("_"));
    assertTrue(gamePage.disabledButtons.size() == 2);
    assertTrue(gamePage.disabledButtons.get(0).getAttribute("id").equals("letter-A"));
    assertTrue(gamePage.disabledButtons.get(1).getAttribute("id").equals("letter-M"));
    assertTrue(gamePage.messagePlayer2.getText().equals("Player 2 guess the word"));
    assertTrue(gamePage.gameCategory.getText().equals("Category: CARS"));
  }
}

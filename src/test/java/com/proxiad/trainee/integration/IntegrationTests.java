package com.proxiad.trainee.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class IntegrationTests {

  private WebDriver driver;
  private HomePage homePage;
  private OngoingPage ongoingPage;
  private GamePage gamePage;
  private EmptyPage emptyPage;
  private MultiplayerInputPage multiplayerInputPage;
  private WinPage winPage;
  private LosePage losePage;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    driver.get("http://localhost:8080/HangmanApp/");
    homePage = new HomePage(driver);
    ongoingPage = new OngoingPage(driver);
    gamePage = new GamePage(driver);
    emptyPage = new EmptyPage(driver);
    multiplayerInputPage = new MultiplayerInputPage(driver);
    winPage = new WinPage(driver);
    losePage = new LosePage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void emptyHistoryPageGoesBackHome() {
    homePage.historyButton.click();
    emptyPage.homeButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void emptyOngoingPageGoesBackHome() {
    homePage.ongoingButton.click();
    emptyPage.homeButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void createsNewGame() {
    homePage.singlePlayerButton.click();
    assertGameStartedCorrectly();
    gamePage.leaveButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void opensMultiplayerInputJSP() {
    homePage.multiplayerButton.click();
    assertTrue(
        multiplayerInputPage
            .messageToPlayer1
            .getText()
            .equals("Player 1 enter the word and category"));
  }

  @Test
  public void createsMultiplayerGameWithValidData() {
    homePage.multiplayerButton.click();
    multiplayerInputPage.enterWord("word");
    multiplayerInputPage.enterCategory("category");
    multiplayerInputPage.enterButton.click();
    assertTrue(gamePage.disabledButtons.size() == 2);
    assertTrue(gamePage.wordProgress.getText().equals("W _ _ D"));
    assertTrue(gamePage.messagePlayer2.getText().equals("Player 2 guess the word"));
  }

  @Test
  public void multiplayerInputValidatesRevealedWord() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("aaabbb", "validCategory");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("The word should not be fully revealed"));
  }

  @Test
  public void multiplayerInputValidatesInvalidWordAndCategory() {
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
  public void multiplayerInputValidatesWordAndCategoryEnding() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("invalidWord ", "invalidCategory2");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("Has to begin or end with a letter"));
    assertTrue(
        multiplayerInputPage.categoryError.getText().equals("Has to begin or end with a letter"));
  }

  @Test
  public void multiplayerInputValidatesWordAndCategoryBeginning() {
    homePage.multiplayerButton.click();
    enterWordAndCategory(" invalidWord", "2invalidCategory");
    assertTrue(
        multiplayerInputPage.wordError.getText().equals("Has to begin or end with a letter"));
    assertTrue(
        multiplayerInputPage.categoryError.getText().equals("Has to begin or end with a letter"));
  }

  @Test
  public void multiplayerInputValidatesShortWordAndCategory() {
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
  public void winsGameCorrectly() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("word", "category");
    gamePage.clickLetterButton('O');

    assertTrue(gamePage.wordProgress.getText().equals("W O _ D"));
    assertTrue(gamePage.disabledButtons.size() == 3);
    gamePage.clickLetterButton('R');
    assertTrue(
        winPage
            .winningMessagElement
            .getText()
            .equals("You have successfully guessed the word: WORD"));

    winPage.homeButtonElement.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void losesGameCorrectly() {
    homePage.multiplayerButton.click();
    enterWordAndCategory("word", "category");
    gamePage.clickLetterButton('Z');
    gamePage.clickLetterButton('X');
    gamePage.clickLetterButton('C');
    gamePage.clickLetterButton('V');
    gamePage.clickLetterButton('B');
    gamePage.clickLetterButton('N');
    gamePage.clickLetterButton('M');
    gamePage.clickLetterButton('A');

    assertTrue(gamePage.disabledButtons.size() == 10);
    assertTrue(gamePage.wordProgress.getText().equals("W _ _ D"));

    gamePage.clickLetterButton('S');
    assertThat(losePage.loosingMessagElement.getText().equals("The word was: WORD"));

    losePage.homeButtonElement.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  private void enterWordAndCategory(String word, String category) {
    multiplayerInputPage.enterWord(word);
    multiplayerInputPage.enterCategory(category);
    multiplayerInputPage.enterButton.click();
  }

  @Test
  public void opensOngoingWhereGamesPresent() {
    int numberOfGames = 3;
    createOnoingGames(numberOfGames);
    homePage.ongoingButton.click();
    assertTrue(ongoingPage.gameRows.size() == numberOfGames);
  }

  @Test
  public void resumeGameOpensGamePage() {
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
    assertThat(gamePage.disabledButtons.size() == 2);
  }
}

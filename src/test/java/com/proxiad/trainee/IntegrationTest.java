package com.proxiad.trainee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.intThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class IntegrationTest {

  WebDriver driver;
  HomePage homePage;
  OngoingPage ongoingPage;
  GamePage gamePage;
  EmptyPage emptyPage;
  MultiplayerInputPage multiplayerInputPage;

  @BeforeEach
  public void setUp() {
    driver = new ChromeDriver();
    driver.get("http://localhost:8080/HangmanApp/");
    homePage = new HomePage(driver);
    ongoingPage = new OngoingPage(driver);
    gamePage = new GamePage(driver);
    emptyPage = new EmptyPage(driver);
    multiplayerInputPage = new MultiplayerInputPage(driver);
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void opensEmptyHistoryPageWhenGamesAreNull() throws InterruptedException {
    homePage.historyButton.click();
    assertThat(emptyPage.messageElement.getText()).isEqualTo("No finished games.");
  }

  @Test
  public void emptyHistoryPageGoesBayckHome() throws InterruptedException {
    homePage.historyButton.click();
    emptyPage.homeButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void opensHistoryEmptyOngoingPageWhenGamesAreNull() throws InterruptedException {
    homePage.ongoingButton.click();
    assertThat(emptyPage.messageElement.getText()).isEqualTo("No ongoing games.");
  }

  @Test
  public void emptyOngoingPagaGoesBayckHome() throws InterruptedException {
    homePage.ongoingButton.click();
    emptyPage.homeButton.click();
    assertThat(homePage.welcomeMessage.getText()).contains("Welcome");
  }

  @Test
  public void createsNewGame() throws InterruptedException {
    homePage.singlePlayerButton.click();
    assertGameStartedCorrectly();
  }

  @Test
  public void opensMultiplayerInputJSP() throws InterruptedException {
    homePage.multiplayerButton.click();
    assertTrue(
        multiplayerInputPage
            .messageToPlayer1
            .getText()
            .equals("Player 1 enter the word and category"));
  }

  @Test
  public void createsMultiplayerGameWithValidData() throws InterruptedException {
    homePage.multiplayerButton.click();
    multiplayerInputPage.enterWord("word");
    multiplayerInputPage.enterCategory("category");
    multiplayerInputPage.enterButton.click();
    Thread.sleep(3000);
    assertTrue(gamePage.disabledButtons.size() == 2);
    assertTrue(gamePage.wordProgress.getText().equals("W _ _ D"));
    assertTrue(gamePage.messagePlayer2.getText().equals("Player 2 guess the word"));
  }

  @Test
  public void opensOngoingWhereGamesPresent() throws InterruptedException {
    int numberOfGames = 3;
    createOnoingGames(numberOfGames);
    homePage.ongoingButton.click();
    Thread.sleep(1000);
    assertTrue(ongoingPage.gameRows.size() == numberOfGames);
  }

  @Test
  public void resumeGameOpensGamePage() {
    createOnoingGames(2);
    homePage.ongoingButton.click();
    ongoingPage.resumeButtons.get(1).click();
    assertGameStartedCorrectly();
  }

  @Test
  public void multiplayerInputCreatesGame() {}

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

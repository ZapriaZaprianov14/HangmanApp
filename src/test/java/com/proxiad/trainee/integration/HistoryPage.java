package com.proxiad.trainee.integration;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HistoryPage {
  WebDriver driver;

  public HistoryPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(id = "home-btn")
  protected WebElement homeBtn;

  @FindBy(name = "game-row")
  protected List<WebElement> gameRows;

  @FindBy(id = "message")
  protected WebElement noGamesMessage;
}

package com.proxiad.trainee.integration;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GamePage {
  WebDriver driver;

  public GamePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(id = "leave-btn")
  protected WebElement leaveButton;

  @FindBy(id = "player2-msg")
  protected WebElement messagePlayer2;

  @FindBy(id = "word-progress")
  protected WebElement wordProgress;

  @FindBy(css = ".key-disabled")
  protected List<WebElement> disabledButtons;

  protected void clickLetterButton(char letter) {
    driver.findElement(By.id("letter-" + letter)).click();
    ;
  }
}

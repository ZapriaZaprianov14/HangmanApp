package com.proxiad.trainee;

import java.util.List;
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
  WebElement leaveButton;

  @FindBy(id = "player2-msg")
  WebElement messagePlayer2;

  @FindBy(css = ".preserve-space")
  WebElement wordProgress;

  @FindBy(css = ".key-disabled")
  List<WebElement> disabledButtons;
}

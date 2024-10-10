package com.proxiad.trainee;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OngoingPage {

  WebDriver driver;

  public OngoingPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(id = "home-btn")
  WebElement toHomeBtn;

  @FindBy(name = "game-row")
  List<WebElement> gameRows;

  @FindBy(name = "resume-btn")
  List<WebElement> resumeButtons;
}

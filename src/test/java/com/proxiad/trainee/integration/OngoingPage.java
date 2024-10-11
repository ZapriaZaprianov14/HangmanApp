package com.proxiad.trainee.integration;

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
  protected WebElement toHomeBtn;

  @FindBy(name = "game-row")
  protected List<WebElement> gameRows;

  @FindBy(name = "resume-btn")
  protected List<WebElement> resumeButtons;
}

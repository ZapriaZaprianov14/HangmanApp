package com.proxiad.trainee;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
  WebDriver driver;

  public HomePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  // Web elements
  @FindBy(xpath = "/html/body/div/div/form[3]/input[1]")
  WebElement historyButton;

  @FindBy(xpath = "/html/body/div/div/form[3]/input[2]")
  WebElement ongoingButton;

  @FindBy(xpath = "/html/body/div/div/p[1]")
  WebElement welcomeMessage;

  @FindBy(id = "cities-btn")
  WebElement singlePlayerButton;

  @FindBy(id = "multiplayer-btn")
  WebElement multiplayerButton;
}

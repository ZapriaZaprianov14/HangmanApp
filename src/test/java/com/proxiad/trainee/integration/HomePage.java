package com.proxiad.trainee.integration;

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
  @FindBy(xpath = "/html/body/div/div/a[1]")
  protected WebElement historyButton;

  @FindBy(xpath = "/html/body/div/div/a[2]")
  protected WebElement ongoingButton;

  @FindBy(xpath = "/html/body/div/div/p[1]")
  protected WebElement welcomeMessage;

  @FindBy(id = "cities-btn")
  protected WebElement singlePlayerButton;

  @FindBy(id = "multiplayer-btn")
  protected WebElement multiplayerButton;
}

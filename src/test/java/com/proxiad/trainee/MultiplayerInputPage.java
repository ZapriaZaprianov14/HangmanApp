package com.proxiad.trainee;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MultiplayerInputPage {
  WebDriver driver;

  public MultiplayerInputPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(id = "wordToGuess")
  WebElement wordInputElement;

  @FindBy(id = "category")
  WebElement categoryInputElement;

  @FindBy(tagName = "h4")
  WebElement messageToPlayer1;

  @FindBy(id = "enter-btn")
  WebElement enterButton;

  public void enterWord(String word) {
    wordInputElement.sendKeys(word);
  }

  public void enterCategory(String word) {
    categoryInputElement.sendKeys(word);
  }
}

package com.proxiad.trainee.integration;

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
  protected WebElement wordInputElement;

  @FindBy(id = "category")
  protected WebElement categoryInputElement;

  @FindBy(id = "word-error")
  protected WebElement wordError;

  @FindBy(id = "category-error")
  protected WebElement categoryError;

  @FindBy(tagName = "h4")
  protected WebElement messageToPlayer1;

  @FindBy(id = "enter-btn")
  protected WebElement enterButton;

  public void enterWord(String word) {
    wordInputElement.sendKeys(word);
  }

  public void enterCategory(String word) {
    categoryInputElement.sendKeys(word);
  }
}

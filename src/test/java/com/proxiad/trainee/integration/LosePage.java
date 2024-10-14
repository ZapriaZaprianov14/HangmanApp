package com.proxiad.trainee.integration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LosePage {
  WebDriver driver;

  public LosePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(tagName = "p")
  protected WebElement loosingMessagElement;

  @FindBy(tagName = "a")
  protected WebElement homeButtonElement;
}

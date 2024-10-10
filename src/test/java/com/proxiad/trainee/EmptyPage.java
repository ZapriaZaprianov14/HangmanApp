package com.proxiad.trainee;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EmptyPage {
  WebDriver driver;

  public EmptyPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(id = "message")
  WebElement messageElement;

  @FindBy(tagName = "input")
  WebElement homeButton;
}

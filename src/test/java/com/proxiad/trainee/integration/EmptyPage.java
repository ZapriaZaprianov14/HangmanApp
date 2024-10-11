package com.proxiad.trainee.integration;

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
  protected WebElement messageElement;

  @FindBy(tagName = "input")
  protected WebElement homeButton;
}

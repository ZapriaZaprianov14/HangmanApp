package com.proxiad.trainee.integration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.PageFactoryFinder;

public class WinPage {
  WebDriver driver;

  public WinPage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  @FindBy(tagName = "p")
  protected WebElement winningMessagElement;

  @FindBy(tagName = "input")
  protected WebElement homeButtonElement;
}

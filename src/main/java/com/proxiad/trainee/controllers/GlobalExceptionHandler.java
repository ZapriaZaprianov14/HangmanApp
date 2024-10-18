package com.proxiad.trainee.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = NoResourceFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleException(NoResourceFoundException ex, Model model) {
    model.addAttribute("message", "Page not found");
    return "unexpected-view";
  }

  @ExceptionHandler(value = NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleException(NoHandlerFoundException ex, Model model) {
    model.addAttribute("message", "Page not found");
    return "unexpected-view";
  }

  @ExceptionHandler(value = Exception.class)
  public String handleUnexpectedException(Exception ex, Model model) {
    model.addAttribute("message", "Unexpected exception");
    return "unexpected-view";
  }
}

package com.proxiad.trainee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.proxiad.trainee.GameRepositoryImpl;
import com.proxiad.trainee.GameServiceImpl;
import com.proxiad.trainee.WordGeneratorServiceImpl;
import com.proxiad.trainee.interfaces.GameRepository;
import com.proxiad.trainee.interfaces.GameService;
import com.proxiad.trainee.interfaces.WordGeneratorService;

@Configuration
@ComponentScan(basePackages = "com.proxiad.trainee")
public class RootConfig {

  //  @Bean
  //  public GameRepository gameRepository() {
  //    return new GameRepositoryImpl();
  //  }
  //
  //  @Bean
  //  public WordGeneratorService wordGeneratorService() {
  //    return new WordGeneratorServiceImpl();
  //  }
  //
  //  @Bean
  //  public GameService gameService() {
  //    GameServiceImpl gameService = new GameServiceImpl();
  //    gameService.setGameRepository(gameRepository());
  //    gameService.setWordGeneratorService(wordGeneratorService());
  //    return gameService;
  //  }
}

package com.ridango.game.controller;

import com.ridango.game.entity.Score;
import com.ridango.game.model.CharacterToken;
import com.ridango.game.model.Drink;
import com.ridango.game.service.CocktailService;
import com.ridango.game.service.ScoreService;
import com.ridango.game.util.TokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class GameController implements CommandLineRunner {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CocktailService cocktailService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
        int score = 0;
        boolean continueGame = true;

        while (continueGame) {
            System.out.println("\n*****************************************");
            System.out.println("*Welcome to the game Guess the Cocktail!*");
            System.out.println("*****************************************");

            Score highestScore = scoreService.getHighestScore();
            if (highestScore != null) {
                System.out.println("Best barista in the game: " + highestScore.getName() +
                        " / " + highestScore.getScore() +
                        (highestScore.getScore() == 1 ? " point" : " points"));
            }

            System.out.println("\nPlease enter your name");
            String player = scanner.next();

            boolean continueGameSession = true;
            while (continueGameSession) {
                Drink drink = cocktailService.getCocktail();
                String name = drink.getName();
                List<CharacterToken> tokenisedName = TokenUtils.tokeniseName(name);
                List<Character> uniqueCharsInName = TokenUtils.extractUniqueCharacters(name);
                List<Character> visibleCharacters = new ArrayList<>(List.of(' ', '\'', '-', '#', '&'));
                int charactersToReveal = uniqueCharsInName.size() / 5;
                int attempts = 5;

                // Game round
                gameRound: for (int i = 0; i < attempts; i++) {
                    for (int j = 0; j < charactersToReveal; j++) {
                        Character randomUniqueCharacterInName = TokenUtils.pickRandomCharacter(uniqueCharsInName);
                        if (i != 0 && randomUniqueCharacterInName != null
                                && randomUniqueCharacterInName != Character.valueOf('\0')) {
                            uniqueCharsInName.remove(Character.valueOf(randomUniqueCharacterInName));
                            visibleCharacters.add(randomUniqueCharacterInName);
                        }
                    }

                    tokenisedName = TokenUtils.checkTokenVisibility(tokenisedName, visibleCharacters);
                    String coveredName = TokenUtils.convertTokenisedNameToString(tokenisedName);

                    System.out.println("\nGuess the Cocktail. "
                            + (attempts - i)
                            + ((attempts - i) == 1 ? " attempt" : " attempts")
                            + " left. Score " + score);
                    System.out.println("Name: " + coveredName);
                    System.out.println("Spoiler: " + name);

                    System.out.println("\nHint " + (i + 1));
                    switch (i) {
                        case 0:
                            System.out.println("Instructions: " + drink.getInstructions());
                            break;
                        case 1:
                            System.out.println("Category: " + drink.getCategory());
                            break;
                        case 2:
                            System.out.println("Glass: " + drink.getGlass());
                            break;
                        case 3:
                            System.out.println("Ingredients: " + drink.getIngredients());
                            break;
                        case 4:
                            System.out.println("Image: " + drink.getImage());
                            break;
                        default:
                            break;
                    }

                    System.out.println("\nMenu: 1 - guess the drink; 2 - skip to see a hint; 3 - exit");
                    int menuChoice = scanner.next().charAt(0) - '0';
                    List<Integer> validChoices = Arrays.asList(1, 2, 3);

                    while (!validChoices.contains(menuChoice)) {
                        System.out.println("Enter 1-2-3");
                        System.out.println("Menu: 1 - guess the drink; 2 - skip to see a hint; 3 - exit");
                        menuChoice = scanner.next().charAt(0) - '0';
                    }

                    boolean breakRound = false;

                    switch (menuChoice) {
                        case 1:
                            System.out.println("Enter your guess");
                            String guess = scanner.next().trim();
                            if (guess.equalsIgnoreCase(name)) {
                                score += (attempts - i);
                                System.out.println(
                                        "\nSuccess! You got "
                                                + (attempts - i)
                                                + ((attempts - i) == 1 ? " point"
                                                        : " points")
                                                + ". Total score: " + score);
                                System.out.println("Do you want to continue? y/n");
                                char c = scanner.next().charAt(0);
                                if (c == 'n') {
                                    continueGameSession = false;
                                }
                                breakRound = true;
                            } else {
                                System.out.println("Oh no, wrong answer! On to next round");
                            }
                            break;
                        case 2:
                            if (i == 4) {
                                continueGameSession = false;
                            }
                            break;
                        case 3:
                            continueGameSession = false;
                            breakRound = true;
                            break;
                        default:
                            break;
                    }
                    if (breakRound) {
                        break gameRound;
                    }
                }
            }
            if (highestScore == null || score > highestScore.getScore()) {
                System.out.println("\nYou are the ultimate barista. " + score + " is the best score in town!");
                scoreService.saveScore(new Score(player, score));
            } else {
                System.out.println("\nGame over! Your total score is " + score);
            }
        }
        scanner.close();
    }
}

package com.ridango.game.controller;

import com.ridango.game.entity.Score;
import com.ridango.game.model.CharacterToken;
import com.ridango.game.model.Drink;
import com.ridango.game.service.CocktailService;
import com.ridango.game.service.ScoreService;
import com.ridango.game.util.TokenUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Component
public class GameController implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CocktailService cocktailService;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
        int score = 0;
        List<String> drinksUsedInSession = new ArrayList<>();
        boolean continueGame = true;

        // After every session the game restarts
        while (continueGame) {
            drinksUsedInSession.clear();
            System.out.println("\n********************************");
            System.out.println("*Welcome to Guess the Cocktail!*");
            System.out.println("********************************");

            Score highestScore = scoreService.getHighestScore();
            if (highestScore != null) {
                System.out.println("Best barista in the game: " + highestScore.getName() +
                        " / " + highestScore.getScore() +
                        (highestScore.getScore() == 1 ? " point" : " points"));
            }

            System.out.println("\nPlease enter your name");
            String player = scanner.next();

            boolean continueGameSession = true;

            // Game session lasts until the player fails to guess the cocktail or exits
            while (continueGameSession) {
                Drink drink = cocktailService.getCocktail();
                String id = drink.getId();

                // Checks if the drink used during the session is unique and has not been used
                // previously in the same session
                while (drinksUsedInSession.contains(id)) {
                    drink = cocktailService.getCocktail();
                    id = drink.getId();
                }
                drinksUsedInSession.add(id);
                String name = drink.getName();

                // Converts the name into character tokens that store character's value
                // and if they are hidden or not. Also sets which characters should be shown
                // first off
                List<CharacterToken> tokenisedName = TokenUtils.tokeniseName(name);
                List<Character> uniqueCharsInName = TokenUtils.extractUniqueCharacters(name);
                List<Character> visibleCharacters = new ArrayList<>(List.of(' ', '\'', '-', '#', '&'));
                uniqueCharsInName.removeAll(visibleCharacters);
                int charactersToReveal = uniqueCharsInName.size() / 5;
                int attempts = 5;

                // Game round with 5 attempts
                gameRound: for (int i = 0; i < attempts; i++) {

                    // Randomly chooses which charecters to reveal
                    // The amount of characters to be revealed in one attempt depends on the amount
                    // of unique characters in the name
                    // Skips first attempt so that all characters except special characters are
                    // hidden
                    for (int j = 0; j < charactersToReveal; j++) {
                        Character randomUniqueCharacterInName = TokenUtils.pickRandomCharacter(uniqueCharsInName);
                        if (i != 0 && randomUniqueCharacterInName != null
                                && randomUniqueCharacterInName != Character.valueOf('\0')) {
                            uniqueCharsInName.remove(Character.valueOf(randomUniqueCharacterInName));
                            visibleCharacters.add(randomUniqueCharacterInName);
                        }
                    }

                    // Applies the mask on the tokenised name
                    tokenisedName = TokenUtils.checkTokenVisibility(tokenisedName, visibleCharacters);
                    String coveredName = TokenUtils.convertTokenisedNameToString(tokenisedName);

                    System.out.println("\nGuess the Cocktail. "
                            + (attempts - i)
                            + ((attempts - i) == 1 ? " attempt" : " attempts")
                            + " left. Score " + score);
                    System.out.println("Name: " + coveredName);
                    System.out.println("Spoiler: " + name);

                    // Shows a hint according to the attemt level
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

                    // Shows the menu, which accepts only numbers 1-2-3
                    System.out.println("\nMenu: 1 - guess the drink; 2 - skip to see a hint; 3 - exit");
                    int menuChoice = scanner.next().charAt(0) - '0';
                    List<Integer> validChoices = Arrays.asList(1, 2, 3);

                    while (!validChoices.contains(menuChoice)) {
                        System.out.println("Enter 1-2-3");
                        System.out.println("Menu: 1 - guess the drink; 2 - skip to see a hint; 3 - exit");
                        menuChoice = scanner.next().charAt(0) - '0';
                    }

                    boolean breakRound = false;

                    // Game logic based on menu choice
                    switch (menuChoice) {
                        // The player chose to guess the name
                        case 1:
                            System.out.println("Enter your guess");
                            String guess = scanner.next().trim();
                            // The answer is correct (regardless of case differneces)
                            if (guess.equalsIgnoreCase(name)) {
                                score += (attempts - i);
                                System.out.println(
                                        "\nSuccess! You got "
                                                + (attempts - i)
                                                + ((attempts - i) == 1 ? " point"
                                                        : " points")
                                                + ". Total score: " + score);
                                // Prompt to continue game or exit
                                System.out.println("Do you want to continue? y/n");
                                char c = scanner.next().charAt(0);
                                if (c == 'n') {
                                    continueGameSession = false;
                                }
                                breakRound = true;
                            }
                            // Wrong answer. Player gets to try again or if it is the last attempt, the
                            // game ends
                            else {
                                System.out.println("Oh no, wrong answer!");
                                if (i == 4) {
                                    continueGameSession = false;
                                }
                            }
                            break;
                        // Player chose to see a new hint. If it is the last attempt, the game ends
                        case 2:
                            if (i == 4) {
                                continueGameSession = false;
                            }
                            break;
                        // Player exits the game
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

            // End of game. Compares the player's score with the highest score
            if (highestScore == null || score > highestScore.getScore()) {
                System.out.println("\nYou are the ultimate barista. " + score + " is the best score in town!");
                scoreService.saveScore(new Score(player, score));
            } else {
                System.out.println("\nGame over! Your total score is " + score);
            }

            // End of game menu, which accepts only numbers 1-2
            System.out.println("\nMenu: 1 - new game; 2 - close game");
            int endMenuChoice = scanner.next().charAt(0) - '0';
            List<Integer> validChoices = Arrays.asList(1, 2);

            while (!validChoices.contains(endMenuChoice)) {
                System.out.println("Enter 1-2");
                System.out.println("Menu: 1 - new game; 2 - close game");
                endMenuChoice = scanner.next().charAt(0) - '0';
            }

            switch (endMenuChoice) {
                // The player chose to continue the name
                case 1:
                    break;
                case 2:
                    continueGame = false;
                    break;
                default:
                    break;
            }

        }
        scanner.close();
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }
}

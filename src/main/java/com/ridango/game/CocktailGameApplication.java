package com.ridango.game;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ridango.game.entity.Score;
import com.ridango.game.model.CharacterToken;
import com.ridango.game.model.Drink;
import com.ridango.game.service.CocktailService;
import com.ridango.game.service.ScoreService;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private CocktailService cocktailService;

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	private List<CharacterToken> tokeniseName(String input) {
		List<CharacterToken> result = new ArrayList<CharacterToken>();

		for (int i = 0; i < input.length(); i++) {
			CharacterToken token = new CharacterToken();
			char c = input.charAt(i);
			token.setValue(c);
			result.add(token);
		}
		return result;
	}

	private List<CharacterToken> checkTokenVisibilty(List<CharacterToken> tokens, List<Character> visibleCharacters) {
		for (CharacterToken token : tokens) {
			if (visibleCharacters.contains(token.getValue())) {
				token.setIsHidden(false);
			}
		}
		return tokens;
	}

	private String convertTokenisedNameToString(List<CharacterToken> tokens) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < tokens.size(); i++) {
			CharacterToken token = tokens.get(i);
			if (token.getIsHidden()) {
				result.append('_');
			} else {
				result.append(token.getValue());
			}

			if (i < tokens.size() - 1) {
				result.append(' ');
			}
		}
		return result.toString();
	}

	private List<Character> extractUniqueCharacters(String input) {
		Set<Character> charSet = new LinkedHashSet<>();
		for (char c : input.toCharArray()) {
			charSet.add(c);
		}
		return new ArrayList<>(charSet);
	}

	private Character pickRandomCharacter(List<Character> charList) {
		if (charList == null || charList.isEmpty()) {
			return null;
		}
		Random random = new Random();
		int randomIndex = random.nextInt(charList.size());
		return charList.get(randomIndex);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in).useDelimiter("\\n");

		Score highestScore;
		Drink drink;
		String name;
		List<Character> uniqueCharsInName;
		List<Character> visibleCharacters;
		List<CharacterToken> tokenisedName;
		String coveredName;
		int charactersToReveal;

		int score = 0;
		int attemps;
		Boolean continueGameSession;
		Boolean continueGame = true;

		// Game
		while (continueGame) {
			continueGameSession = true;
			highestScore = scoreService.getHighestScore();

			System.out.println();
			System.out.println("*****************************************");
			System.out.println("*Welcome to the game Guess the Cocktail!*");
			System.out.println("*****************************************");
			if (highestScore != null) {
				System.out.println("Best barista in the game: " + highestScore.getName() +
						" / "
						+ highestScore.getScore());
			}

			System.out.println();

			System.out.println("Please enter your name");
			String player = scanner.next();

			System.out.println();
			System.out.print("Hi, " + player + "!");
			System.out.println();

			// Game session
			while (continueGameSession) {
				drink = cocktailService.getCocktail();
				name = drink.getName();
				uniqueCharsInName = extractUniqueCharacters(name);
				visibleCharacters = new ArrayList<Character>();
				visibleCharacters.add(' ');
				tokenisedName = tokeniseName(name);
				charactersToReveal = uniqueCharsInName.size() / 5;

				attemps = 5;

				// Game round
				gameRound: for (int i = 0; i < attemps; i++) {
					for (int j = 0; j < charactersToReveal; j++) {
						Character randomUniqueCharacterInName = pickRandomCharacter(uniqueCharsInName);
						if (i != 0 && randomUniqueCharacterInName != null
								&& randomUniqueCharacterInName != Character.valueOf('\0')) {
							uniqueCharsInName.remove(Character.valueOf(randomUniqueCharacterInName));
							visibleCharacters.add(randomUniqueCharacterInName);
						}
					}

					tokenisedName = checkTokenVisibilty(tokenisedName, visibleCharacters);
					coveredName = convertTokenisedNameToString(tokenisedName);

					System.out.println("\nGuess the Cocktail. " + (attemps - i) +
							" attempts left. Score " + score);
					System.out.println("Name: " + coveredName + "\n");
					System.out.println("Spoiler: " + name + "\n");

					System.out.println("Hint " + (i + 1));
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
							System.out.println("Ingredients: " + drink.getIngredients().toString());
							break;
						case 4:
							System.out.println("Image: " + drink.getImage());
							break;
						default:
							break;
					}

					System.out.println();
					System.out.println("Menu: 1 - guess the drink; 2 - skip to see a hint; 3 - exit");
					int menuChoice = scanner.nextInt();

					switch (menuChoice) {
						case 1:
							System.out.println("Enter your guess");
							String guess = scanner.next().trim();
							if (guess.equals(name)) {
								score += attemps - i;
								System.out.println(
										"\nSuccess! You got " + (attemps - i) + " points and your total score is "
												+ score + " points\n");
								System.out.println("Do you want continue? y/n \n");
								char c = scanner.next().charAt(0);
								if (c == 'n') {
									continueGameSession = false;
								}
								break gameRound;
							} else {
								System.out.println("Oh no! On to next round");
								System.out.println();
							}
							break;
						case 3:
							continueGameSession = false;
							break gameRound;
						default:
							break;
					}
				}
			}
			System.out.println("\nGame over!");
			System.out.println("\nYour total score is: " + score);
			scoreService.saveScore(new Score(player, score));
		}

		scanner.close();

	}
}

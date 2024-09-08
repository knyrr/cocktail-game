package com.ridango.game;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {
	CocktailApiClient service = new CocktailApiClient();

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	private List<CharToken> tokeniseName(String input) {
		List<CharToken> result = new ArrayList<CharToken>();

		for (int i = 0; i < input.length(); i++) {
			CharToken token = new CharToken();
			char c = input.charAt(i);
			token.setValue(c);
			result.add(token);
		}
		return result;
	}

	private List<CharToken> checkTokenVisibilty(List<CharToken> tokens, List<Character> visibleCharacters) {
		for (CharToken token : tokens) {
			if (visibleCharacters.contains(token.getValue())) {
				token.setIsHidden(false);
			}
		}
		return tokens;
	}

	private String convertTokenisedNameToString(List<CharToken> tokens) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < tokens.size(); i++) {
			CharToken token = tokens.get(i);
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
		;
		Drink drink = service.fetchRandomCocktail();

		String name = drink.getName();
		List<Character> uniqueCharsInName = extractUniqueCharacters(name);

		int score = 0;
		int round = 5; // 5

		List<Character> visibleCharacters = new ArrayList<Character>();
		visibleCharacters.add(' ');

		List<CharToken> tokenisedName = tokeniseName(name);
		String coveredName = convertTokenisedNameToString(tokenisedName);

		System.out.println();
		System.out.println("*****************************************");
		System.out.println("*Welcome to the game Guess the Cocktail!*");
		System.out.println("*****************************************");
		System.out.println();

		System.out.println("Please enter your name");
		String player = scanner.next();

		System.out.println();
		System.out.print("Hi, " + player + "!");
		System.out.println();

		for (int i = 0; i < round; i++) {

			Character randomUniqueCharacterInName = pickRandomCharacter(uniqueCharsInName);
			if (i != 0 && randomUniqueCharacterInName != null
					&& randomUniqueCharacterInName != Character.valueOf('\0')) {
				uniqueCharsInName.remove(Character.valueOf(randomUniqueCharacterInName));
				visibleCharacters.add(randomUniqueCharacterInName);

				tokenisedName = checkTokenVisibilty(tokenisedName, visibleCharacters);
				coveredName = convertTokenisedNameToString(tokenisedName);
			}

			System.out.println("Guess the Cocktail. Attempts left " + (round - i) + ". Score " + score);
			System.out.println("Name: " + coveredName);
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

						System.out.println("\nSuccess!\n");
						// küsida, kas tahab jätkata
					} else {
						System.out.println("Oh no! On to next round");
						System.out.println();
					}
					break;
				case 2:
					// järgmine vihje
					break;
				case 3:
					// mängu lõpp
					break;
				default:
					// midagi muud
					break;
			}
		}
		System.out.println("Game over!");
		scanner.close();
	}
}

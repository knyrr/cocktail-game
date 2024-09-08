package com.ridango.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {
	CocktailApiClient service = new CocktailApiClient();

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	private static String replaceWithUnderscores(String input) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			// If the character is a space, keep it; otherwise, replace it with an
			// underscore
			if (c == ' ') {
				result.append(' ');
			} else {
				result.append('_');
			}

			// Add a space after each character (except for the last one)
			if (i < input.length() - 1) {
				result.append(' ');
			}
		}

		return result.toString();
	}

	private List<CharToken> tokeniseName(String input) {
		List<CharToken> result = new ArrayList<CharToken>();
		List<Character> revealedCharacters = new ArrayList<Character>();
		revealedCharacters.add(' ');

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			CharToken token = new CharToken();
			token.setValue(c);
			if (revealedCharacters.contains(c)) {
				token.setIsHidden(false);
			}
			result.add(token);
		}
		return result;
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

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in).useDelimiter("\\n");
		;
		Drink drink = service.fetchRandomCocktail();

		String name = drink.getName();
		int score = 0;
		int round = 5;
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

		/*
		 * for (int i = 0; i < round; i++) {
		 * 
		 * }
		 */
		System.out.println(" Guess this drink: " + coveredName);
		System.out.println(name + "\n");

		System.out.println("Hint 1");
		System.out.println("Instructions: " + drink.getInstructions());

		System.out.println();
		System.out.println("Please enter 1 to guess the drink or 2 to skip and see a hint");
		int num1 = scanner.nextInt();

		if (num1 == 1) {
			System.out.println("Enter the name");
			String guess = scanner.next();
			System.out.println(guess + name);
			if (guess.equals(name)) {
				System.out.println("Success!");
			} else {
				System.out.println("Oh no!");
			}
		} else {
			System.out.println("Hint 2 2- category: " + drink.getCategory());
		}
		scanner.close();
	}
}

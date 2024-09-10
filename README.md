## Cocktail game\* Java 1.17 or newer

- A simple game of guess the cocktail using public API https://www.thecocktaildb.com/api.php

# Game rules:

- Random cocktail with instructions is shown to the player together with number of letter in the name of the cocktail (e.g. "Mojito" -> "\_ \_ \_ \_ \_ \_")
- Player must guess the name of the cocktail
- Player can skip the round if he doesn't know the answer to get more hints about the cocktail
- Player has 5 attempts to guess the name of the cocktail
- If player answers correctly the game continues with a new random cocktail and score is increased by number of attempts left
- If player answers wrongly or skips round the game shows:
  - Name of the cocktail with some new random letters revealed (e.g. "\_ \_ \_ \_ \_ _" -> " _ _ j _ \_ _" -> " _ _ j _ _ o" -> "M _ j \_ _ o" -> "M _ ji _ o" -> "M _ jito" -> "Mojito") (For longer cocktails more letters can be revealed than one)
  - Additional info about the cocktail (e.g. category, glass, ingredients, picture)
  - Number of attempts left
- If player fails to guess the cocktail after 5 attempts the game ends and high score is updated
- In one game same cocktail shouldn't appear twice

* Main tasks:

  - Build a Java console application using Java 1.8 or newer (we recommend using Java 17 or 21) which allows to play a game of "Guess the cocktail"
  - Build a game with the rules from above
  - Remember the current highest score achieved
  - Feel free to add any more functionalities you think would be cool to add

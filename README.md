# Guess the Cocktail

- A simple and interactive console game where players guess the names of cocktails based on hints and partially revealed names. Players earn points for correct guesses and receive hints to help them along the way.

## Features

- Guess the cocktail name based on partially revealed characters.
- Receive hints related to the cocktail (instructions, category, glass, ingredients, and image).
- Highest scores are saved.

## Technologies

- **Spring Boot**: Provides the foundation for building the web application.
- **Java**: The application is developed using Java 17.
- **H2 Database**: An simple database used for development and testing with an in-memory or in-file option.
- **Lombok**: A library that helps reduce boilerplate code in Java.
- **Jackson**: Used for JSON processing.
- **JUnit**: Testing framework used for unit tests.

## Installation and Setup

### Prerequisites

- Java 17
- Gradle

### Steps

1. Clone the repository:

   ```bash
   git clone git@github.com:knyrr/cocktail-game.git
   ```

2. Navigate to the project directory:

   ```bash
   cd cocktail-game
   ```

3. Build the project using Gradle:

   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   java -jar build/libs/cocktail-game-0.0.1-SNAPSHOT.jar
   ```

## Screeshot

| ![cocktail-game](https://github.com/user-attachments/assets/18a5910b-06cf-4271-ae07-5ed8d33bb1f3) |
| :-----------------------------------------------------------------------------------------------: |
|                              _Guess the Cocktail console screenshot_                              |

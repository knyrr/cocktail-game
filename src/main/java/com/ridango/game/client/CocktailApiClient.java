package com.ridango.game.client;

import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ridango.game.Drink;
import com.ridango.game.DrinkDeserializer;
import com.ridango.game.DrinkResponse;

public class CocktailApiClient {

    private static final String API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    private final RestTemplate restTemplate = new RestTemplate();

    public Drink fetchRandomCocktail() {
        String json = restTemplate.getForObject(API_URL, String.class);
        return processJson(json);
    }

    private Drink processJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Drink.class, new DrinkDeserializer())
                .create();
        DrinkResponse drinkResponse = gson.fromJson(json, DrinkResponse.class);
        if (drinkResponse.getDrinks() != null && !drinkResponse.getDrinks().isEmpty()) {
            return drinkResponse.getDrinks().get(0);
        } else {
            return null;
        }
    }

}

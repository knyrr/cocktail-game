package com.ridango.game;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@RestController
public class CocktailGameController {

    private static final String COCKTAIL_API_URL = "https://www.thecocktaildb.com/api/json/v1/1/random.php";

    @GetMapping("/getCocktail")
    public Drink getCocktail() {
        RestTemplate restTemplate = new RestTemplate();
        String json = restTemplate.getForObject(COCKTAIL_API_URL, String.class);
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

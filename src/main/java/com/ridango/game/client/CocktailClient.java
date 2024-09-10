package com.ridango.game.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.model.Drink;
import com.ridango.game.model.DrinkResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

@Component
public class CocktailClient {

    @Value("${cocktail.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public CocktailClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Drink getCocktail() {
        try {
            String json = restTemplate.getForObject(apiUrl, String.class);

            if (json == null) {
                return null;
            }

            return processJson(json);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Drink processJson(String json) {
        try {
            DrinkResponse drinkResponse = objectMapper.readValue(json, DrinkResponse.class);
            if (drinkResponse.getDrinks() != null && !drinkResponse.getDrinks().isEmpty()) {
                return drinkResponse.getDrinks().get(0);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

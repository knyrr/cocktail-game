package com.ridango.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ridango.game.client.CocktailClient;
import com.ridango.game.model.Drink;

@Service
public class CocktailService {

    private final CocktailClient cocktailApiClient;

    @Autowired
    public CocktailService(CocktailClient cocktailApiClient) {
        this.cocktailApiClient = cocktailApiClient;
    }

    public Drink getCocktail() {
        return cocktailApiClient.getCocktail();
    }
}
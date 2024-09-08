package com.ridango.game;

import com.google.gson.*;
import java.lang.reflect.Type;

public class DrinkDeserializer implements JsonDeserializer<Drink> {

    @Override
    public Drink deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Drink drink = new Drink();

        drink.setId(jsonObject.get("idDrink").getAsString());
        drink.setName(jsonObject.get("strDrink").getAsString());
        drink.setCategory(jsonObject.get("strCategory").getAsString());
        drink.setGlass(jsonObject.get("strGlass").getAsString());
        drink.setInstructions(jsonObject.get("strInstructions").getAsString());
        drink.setImage(jsonObject.get("strDrinkThumb").getAsString());

        for (int i = 1; i <= 15; i++) {
            String ingredientKey = "strIngredient" + i;
            if (jsonObject.has(ingredientKey) && !jsonObject.get(ingredientKey).isJsonNull()) {
                String ingredient = jsonObject.get(ingredientKey).getAsString();
                if (!ingredient.isEmpty()) {
                    drink.getIngredients().add(ingredient);
                }
            }
        }

        return drink;
    }
}

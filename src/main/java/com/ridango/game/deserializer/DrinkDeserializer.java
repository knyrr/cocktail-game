package com.ridango.game.deserializer;

import com.ridango.game.model.Drink;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class DrinkDeserializer extends JsonDeserializer<Drink> {

    @Override
    public Drink deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Drink drink = new Drink();

        drink.setId(node.get("idDrink").asText());
        drink.setName(node.get("strDrink").asText());
        drink.setCategory(node.get("strCategory").asText());
        drink.setGlass(node.get("strGlass").asText());
        drink.setInstructions(node.get("strInstructions").asText());
        drink.setImage(node.get("strDrinkThumb").asText());

        for (int i = 1; i <= 15; i++) {
            String ingredientKey = "strIngredient" + i;
            JsonNode ingredientNode = node.get(ingredientKey);
            if (ingredientNode != null && !ingredientNode.isNull()) {
                String ingredient = ingredientNode.asText();
                if (!ingredient.isEmpty()) {
                    drink.getIngredients().add(ingredient);
                }
            }
        }

        return drink;
    }
}

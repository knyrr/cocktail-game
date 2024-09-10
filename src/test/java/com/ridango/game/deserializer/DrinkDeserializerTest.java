package com.ridango.game.deserializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.config.JacksonConfig;
import com.ridango.game.model.Drink;

public class DrinkDeserializerTest {
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new JacksonConfig().objectMapper();
    }

    @Test
    void testDeserializeDrink() throws Exception {
        // Given
        String drinkJson = "{\"idDrink\":\"12308\",\"strDrink\":\"Stone Sour\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Whiskey sour glass\",\"strInstructions\":\"Shake all ingredients with ice, strain into a chilled whiskey sour glass, and serve.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Alle Zutaten mit Eis sch\\u00fctteln, in ein gek\\u00fchltes Whiskey-Sourglas abseihen und servieren.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Shakerare tutti gli ingredienti con ghiaccio, filtrare in un bicchiere ghiacciato di whisky sour e servire.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/vruvtp1472719895.jpg\",\"strIngredient1\":\"Apricot brandy\",\"strIngredient2\":\"Orange juice\",\"strIngredient3\":\"Sweet and sour\",\"strIngredient4\":null,\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 oz \",\"strMeasure2\":\"1 oz \",\"strMeasure3\":\"1 oz \",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2016-09-01 09:51:35\"}";

        // When
        Drink drink = objectMapper.readValue(drinkJson, Drink.class);

        // Then
        assertNotNull(drink);
        assertEquals("12308", drink.getId());
        assertEquals("Stone Sour", drink.getName());
        assertEquals("Ordinary Drink", drink.getCategory());
        assertEquals(3, drink.getIngredients().size());
        assertTrue(drink.getIngredients().contains("Apricot brandy"));
        assertTrue(drink.getIngredients().contains("Orange juice"));
        assertTrue(drink.getIngredients().contains("Sweet and sour"));
    }
}

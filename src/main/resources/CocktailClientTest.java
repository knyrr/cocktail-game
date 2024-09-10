package com.ridango.game.client;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridango.game.model.Drink;
import com.ridango.game.model.DrinkResponse;

public class CocktailClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CocktailClient cocktailClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCocktail_Success() throws Exception {
        // Given
        String jsonResponse = "{\"drinks\":[{\"idDrink\":\"12308\",\"strDrink\":\"Stone Sour\",\"strDrinkAlternate\":null,\"strTags\":null,\"strVideo\":null,\"strCategory\":\"Ordinary Drink\",\"strIBA\":null,\"strAlcoholic\":\"Alcoholic\",\"strGlass\":\"Whiskey sour glass\",\"strInstructions\":\"Shake all ingredients with ice, strain into a chilled whiskey sour glass, and serve.\",\"strInstructionsES\":null,\"strInstructionsDE\":\"Alle Zutaten mit Eis sch\\u00fctteln, in ein gek\\u00fchltes Whiskey-Sourglas abseihen und servieren.\",\"strInstructionsFR\":null,\"strInstructionsIT\":\"Shakerare tutti gli ingredienti con ghiaccio, filtrare in un bicchiere ghiacciato di whisky sour e servire.\",\"strInstructionsZH-HANS\":null,\"strInstructionsZH-HANT\":null,\"strDrinkThumb\":\"https:\\/\\/www.thecocktaildb.com\\/images\\/media\\/drink\\/vruvtp1472719895.jpg\",\"strIngredient1\":\"Apricot brandy\",\"strIngredient2\":\"Orange juice\",\"strIngredient3\":\"Sweet and sour\",\"strIngredient4\":null,\"strIngredient5\":null,\"strIngredient6\":null,\"strIngredient7\":null,\"strIngredient8\":null,\"strIngredient9\":null,\"strIngredient10\":null,\"strIngredient11\":null,\"strIngredient12\":null,\"strIngredient13\":null,\"strIngredient14\":null,\"strIngredient15\":null,\"strMeasure1\":\"1 oz \",\"strMeasure2\":\"1 oz \",\"strMeasure3\":\"1 oz \",\"strMeasure4\":null,\"strMeasure5\":null,\"strMeasure6\":null,\"strMeasure7\":null,\"strMeasure8\":null,\"strMeasure9\":null,\"strMeasure10\":null,\"strMeasure11\":null,\"strMeasure12\":null,\"strMeasure13\":null,\"strMeasure14\":null,\"strMeasure15\":null,\"strImageSource\":null,\"strImageAttribution\":null,\"strCreativeCommonsConfirmed\":\"No\",\"dateModified\":\"2016-09-01 09:51:35\"}]}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);

        Drink drink = new Drink();
        drink.setId("12308");
        drink.setName("Stone Sour");

        DrinkResponse drinkResponse = new DrinkResponse();
        drinkResponse.setDrinks(List.of(drink));

        when(objectMapper.readValue(anyString(), eq(DrinkResponse.class))).thenReturn(drinkResponse);

        // When
        Drink result = cocktailClient.getCocktail();

        // Then
        assertNotNull(result);
        assertEquals("Stone Sour", result.getName());
        assertEquals("12308", result.getId());
    }

    @Test
    void testGetCocktail_ApiReturnsNull() {
        // Given
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        // When
        Drink result = cocktailClient.getCocktail();

        // Then
        assertNull(result);
    }
}
package com.ridango.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ridango.game.client.CocktailClient;
import com.ridango.game.model.Drink;

public class CocktailServiceTest {

    @Mock
    private CocktailClient cocktailClient;

    @InjectMocks
    private CocktailService cocktailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCocktail() {
        // Given
        Drink mockDrink = new Drink();
        mockDrink.setId("12345");
        mockDrink.setName("Margarita");

        when(cocktailClient.getCocktail()).thenReturn(mockDrink);

        // When
        Drink result = cocktailService.getCocktail();

        // Then
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertEquals("Margarita", result.getName());

        // Verify that the method was called on the mock
        verify(cocktailClient).getCocktail();
    }
}

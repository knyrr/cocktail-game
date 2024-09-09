package com.ridango.game.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ridango.game.deserializer.DrinkDeserializer;
import com.ridango.game.model.Drink;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Drink.class, new DrinkDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }
}

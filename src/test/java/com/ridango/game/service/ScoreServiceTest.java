package com.ridango.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ridango.game.entity.Score;

public class ScoreServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ScoreService scoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetHighestScore_Success() {
        // Given
        String sql = "SELECT * FROM score ORDER BY score DESC LIMIT 1";
        Score highestScore = new Score("John", 100);

        when(jdbcTemplate.queryForObject(eq(sql),
                ArgumentMatchers.<org.springframework.jdbc.core.RowMapper<Score>>any())).thenReturn(highestScore);

        // When
        Score result = scoreService.getHighestScore();

        // Then
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals(100, result.getScore());
    }

    @Test
    void testGetHighestScore_NoResult() {
        // Given
        String sql = "SELECT * FROM score ORDER BY score DESC LIMIT 1";

        when(jdbcTemplate.queryForObject(eq(sql),
                ArgumentMatchers.<org.springframework.jdbc.core.RowMapper<Score>>any()))
                .thenThrow(new EmptyResultDataAccessException(1));

        // When
        Score result = scoreService.getHighestScore();

        // Then
        assertNull(result);
    }
}
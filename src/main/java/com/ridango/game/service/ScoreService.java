package com.ridango.game.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ridango.game.entity.Score;
import com.ridango.game.repository.ScoreRepository;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Score getHighestScore() {
        String sql = "SELECT * FROM score ORDER BY score DESC LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> new Score(rs.getString("name"), rs.getInt("score")));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public Score saveScore(Score score) {
        return scoreRepository.save(score);
    }

    void deleteScore(Long id) {
        scoreRepository.deleteById(id);
    };

}

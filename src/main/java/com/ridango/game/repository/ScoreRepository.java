package com.ridango.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridango.game.entity.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
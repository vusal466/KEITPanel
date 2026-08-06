package com.example.keitpanel.repositories;

import com.example.keitpanel.entities.position.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}

package com.example.blackjack.service;

import com.example.blackjack.engine.GameResult;
import java.util.Map;

public record SimulationResult(
    int totalGames,
    Map<GameResult, Integer> stats,
    long initialMoney,
    long finalMoney,
    double roi
) {}

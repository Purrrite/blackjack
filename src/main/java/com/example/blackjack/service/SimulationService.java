package com.example.blackjack.service;

import com.example.blackjack.config.BlackjackConfig;
import com.example.blackjack.engine.BlackjackEngine;
import com.example.blackjack.engine.GameResult;
import com.example.blackjack.model.Deck;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class SimulationService {
    
    private final BlackjackConfig config;

    public SimulationService(BlackjackConfig config) {
        this.config = config;
    }

    public SimulationResult runSimulation() {
        Deck deck = new Deck(config.getDeckCount());
        Map<GameResult, Integer> stats = new EnumMap<>(GameResult.class);
        for (GameResult result : GameResult.values()) stats.put(result, 0);

        long money = config.getInitialMoney();

        for (int i = 0; i < config.getCount(); i++) {
            if (deck.getUsageRatio() > config.getShuffleThreshold()) {
                deck.shuffle();
            }

            BlackjackEngine engine = new BlackjackEngine(deck);
            GameResult result = engine.play();

            stats.put(result, stats.get(result) + 1);
            money += calculateProfit(result);
        }

        long totalWagered = (long) config.getBetAmount() * config.getCount();
        double roi = (double) (money - config.getInitialMoney()) / totalWagered * 100;
        return new SimulationResult(config.getCount(), stats, config.getInitialMoney(), money, roi);
    }

    private int calculateProfit(GameResult result) {
        return switch (result) {
            case PLAYER_BLACKJACK -> (int) (config.getBetAmount() * 1.5);
            case PLAYER_WIN -> config.getBetAmount();
            case DEALER_BLACKJACK, DEALER_WIN -> -config.getBetAmount();
            case PUSH -> 0;
        };
    }
}

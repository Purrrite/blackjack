package com.example.blackjack.service;

import com.example.blackjack.config.BlackjackConfig;
import com.example.blackjack.engine.BlackjackEngine;
import com.example.blackjack.engine.DetailedGameRecord;
import com.example.blackjack.engine.GameResult;
import com.example.blackjack.model.Deck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationService {
    
    private final BlackjackConfig config;

    public SimulationService(BlackjackConfig config) {
        this.config = config;
    }

    public SimulationResult runSimulation(int count, long initialMoney, int betAmount) {
        Deck deck = new Deck(config.getDeckCount());
        Map<GameResult, Integer> stats = new EnumMap<>(GameResult.class);
        for (GameResult result : GameResult.values()) stats.put(result, 0);

        long money = initialMoney;

        for (int i = 0; i < count; i++) {
            if (deck.getUsageRatio() > config.getShuffleThreshold()) {
                deck.shuffle();
            }

            BlackjackEngine engine = new BlackjackEngine(deck);
            GameResult result = engine.play();

            stats.put(result, stats.get(result) + 1);
            money += calculateProfit(result, betAmount);
        }

        long totalWagered = (long) betAmount * count;
        double roi = (double) (money - initialMoney) / totalWagered * 100;
        return new SimulationResult(count, stats, initialMoney, money, roi);
    }

    public List<DetailedGameRecord> runDetailedSimulation(int count, long initialMoney, int betAmount) {
        Deck deck = new Deck(config.getDeckCount());
        List<DetailedGameRecord> records = new ArrayList<>();
        long currentMoney = initialMoney;

        for (int i = 0; i < count; i++) {
            if (deck.getUsageRatio() > config.getShuffleThreshold()) {
                deck.shuffle();
            }
            BlackjackEngine engine = new BlackjackEngine(deck);
            DetailedGameRecord record = engine.playDetailed(currentMoney, betAmount);
            records.add(record);
            currentMoney = record.finalMoney();
        }
        return records;
    }

    private long calculateProfit(GameResult result, int betAmount) {
        return switch (result) {
            case PLAYER_BLACKJACK -> (long) (betAmount * 1.5);
            case PLAYER_WIN -> betAmount;
            case DEALER_BLACKJACK, DEALER_WIN -> -betAmount;
            case PUSH -> 0;
        };
    }
}

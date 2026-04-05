package com.example.blackjack.engine;

import com.example.blackjack.model.*;
import java.util.ArrayList;
import java.util.List;

public class BlackjackEngine {
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private final Deck deck;

    public BlackjackEngine(Deck deck) {
        this.deck = deck;
    }

    /**
     * 빠른 통계 집계를 위한 경량 게임 실행 메서드
     */
    public GameResult play() {
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());

        if (player.isBlackjack() || dealer.isBlackjack()) {
            return determineWinner();
        }

        Card dealerUpCard = dealer.getHand().get(0);
        while (player.shouldHit(dealerUpCard)) {
            player.receiveCard(deck.deal());
            if (player.isBust()) break;
        }

        if (!player.isBust()) {
            while (dealer.shouldHit()) {
                dealer.receiveCard(deck.deal());
                if (dealer.isBust()) break;
            }
        }

        return determineWinner();
    }

    /**
     * 틱 기반 시각화를 위한 상세 게임 실행 메서드
     */
    public DetailedGameRecord playDetailed(long currentMoney, int betAmount) {
        List<GameStep> steps = new ArrayList<>();
        
        // Step 1: Initial Deal
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
        player.receiveCard(deck.deal());
        dealer.receiveCard(deck.deal());
        addStep(steps, "INITIAL_DEAL", currentMoney);

        if (player.isBlackjack() || dealer.isBlackjack()) {
            return finalizeDetailed(steps, currentMoney, betAmount);
        }

        // Step 2: Player Turn
        Card dealerUpCard = dealer.getHand().get(0);
        while (player.shouldHit(dealerUpCard)) {
            player.receiveCard(deck.deal());
            addStep(steps, "PLAYER_HIT", currentMoney);
            if (player.isBust()) break;
        }

        // Step 3: Dealer Turn
        if (!player.isBust()) {
            while (dealer.shouldHit()) {
                dealer.receiveCard(deck.deal());
                addStep(steps, "DEALER_HIT", currentMoney);
                if (dealer.isBust()) break;
            }
        }

        return finalizeDetailed(steps, currentMoney, betAmount);
    }

    private DetailedGameRecord finalizeDetailed(List<GameStep> steps, long currentMoney, int betAmount) {
        GameResult result = determineWinner();
        long profit = calculateProfit(result, betAmount);
        long finalMoney = currentMoney + profit;
        
        steps.add(new GameStep(
            "GAME_OVER",
            new ArrayList<>(player.getHand()),
            new ArrayList<>(dealer.getHand()),
            player.calculateScore(),
            dealer.calculateScore(),
            result.name(),
            finalMoney
        ));
        return new DetailedGameRecord(steps, finalMoney);
    }

    private void addStep(List<GameStep> steps, String action, long money) {
        steps.add(new GameStep(
            action,
            new ArrayList<>(player.getHand()),
            new ArrayList<>(dealer.getHand()),
            player.calculateScore(),
            dealer.calculateScore(),
            "PENDING",
            money
        ));
    }

    private long calculateProfit(GameResult result, int betAmount) {
        return switch (result) {
            case PLAYER_BLACKJACK -> (long) (betAmount * 1.5);
            case PLAYER_WIN -> betAmount;
            case DEALER_BLACKJACK, DEALER_WIN -> -betAmount;
            case PUSH -> 0;
        };
    }

    private GameResult determineWinner() {
        int pScore = player.calculateScore();
        int dScore = dealer.calculateScore();

        if (player.isBlackjack() && !dealer.isBlackjack()) return GameResult.PLAYER_BLACKJACK;
        if (dealer.isBlackjack() && !player.isBlackjack()) return GameResult.DEALER_BLACKJACK;
        if (player.isBlackjack() && dealer.isBlackjack()) return GameResult.PUSH;

        if (pScore > 21) return GameResult.DEALER_WIN;
        if (dScore > 21) return GameResult.PLAYER_WIN;
        
        if (pScore > dScore) return GameResult.PLAYER_WIN;
        if (pScore < dScore) return GameResult.DEALER_WIN;
        return GameResult.PUSH;
    }

    private static class Player extends Participant {
        public boolean shouldHit(Card dealerUpCard) {
            int score = calculateScore();
            return (dealerUpCard.getValue() >= 7) ? score < 17 : score < 12;
        }
    }

    private static class Dealer extends Participant {
        public boolean shouldHit() {
            return calculateScore() < 17;
        }
    }
}

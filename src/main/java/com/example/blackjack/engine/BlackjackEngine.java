package com.example.blackjack.engine;

import com.example.blackjack.model.*;

public class BlackjackEngine {
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private final Deck deck;

    public BlackjackEngine(Deck deck) {
        this.deck = deck;
    }

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
            if (player.isBust()) return GameResult.DEALER_WIN;
        }

        while (dealer.shouldHit()) {
            dealer.receiveCard(deck.deal());
            if (dealer.isBust()) return GameResult.PLAYER_WIN;
        }

        return determineWinner();
    }

    private GameResult determineWinner() {
        int pScore = player.calculateScore();
        int dScore = dealer.calculateScore();

        if (player.isBlackjack() && !dealer.isBlackjack()) return GameResult.PLAYER_BLACKJACK;
        if (dealer.isBlackjack() && !player.isBlackjack()) return GameResult.DEALER_BLACKJACK;
        if (player.isBlackjack() && dealer.isBlackjack()) return GameResult.PUSH;

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

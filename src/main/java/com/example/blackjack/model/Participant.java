package com.example.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Participant {
    protected final List<Card> hand = new ArrayList<>();

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public int calculateScore() {
        int score = hand.stream().mapToInt(Card::getValue).sum();
        long aceCount = hand.stream().filter(c -> c.rank() == Rank.ACE).count();

        while (score > 21 && aceCount > 0) { // If score exceeds 21 and there are Aces, treat one Ace as 1 instead of 11
            score -= 10;
            aceCount--;
        }
        return score;
    }

    public boolean isBust() {
        return calculateScore() > 21;
    }

    public boolean isBlackjack() {
        return hand.size() == 2 && calculateScore() == 21;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }
}

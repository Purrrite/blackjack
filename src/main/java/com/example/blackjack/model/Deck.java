package com.example.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private int cursor = 0;

    public Deck(int deckCount) {
        for (int i = 0; i < deckCount; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
        cursor = 0;
    }

    public Card deal() {
        if (cursor >= cards.size()) {
            throw new IllegalStateException("No more cards in the deck.");
        }
        return cards.get(cursor++);
    }

    public double getUsageRatio() {
        return (double) cursor / cards.size();
    }
}

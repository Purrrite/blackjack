package com.example.blackjack.model;

public record Card(Suit suit, Rank rank) {
    public int getValue() {
        return rank.getDefaultValue();
    }
}

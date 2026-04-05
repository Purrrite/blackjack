package com.example.blackjack.model;

public record Card(Suit suit, Rank rank) {
    public int getValue() {
        return rank.getDefaultValue();
    }

    /**
     * 웹 화면에서 카드를 그리기 위한 CSS 좌표 반환
     */
    public String getCssPosition() {
        double x = CardSprite.getXPercent(this);
        double y = CardSprite.getYPercent(this);
        return String.format("background-position: %.2f%% %.2f%%;", x, y);
    }
}

package com.example.blackjack.model;

import java.util.List;

public class CardSprite {
    public static final int COLS = 13;
    public static final int ROWS = 5;

    private static final List<Suit> SUIT_ORDER = List.of(
        Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS
    );

    // 이미지상의 랭크 순서 (왼쪽에서 오른쪽으로)
    private static final List<Rank> RANK_ORDER = List.of(
        Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX,
        Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING
    );

    /**
     * 카드 객체를 기반으로 스프라이트 시트에서의 X 인덱스(열)를 반환
     */
    public static int getColumnIndex(Card card) {
        return RANK_ORDER.indexOf(card.rank());
    }

    /**
     * 카드 객체를 기반으로 스프라이트 시트에서의 Y 인덱스(행)를 반환
     */
    public static int getRowIndex(Card card) {
        return SUIT_ORDER.indexOf(card.suit());
    }

    /**
     * CSS background-position 계산을 위한 퍼센트 값 반환 (가로)
     */
    public static double getXPercent(Card card) {
        return (double) getColumnIndex(card) * (100.0 / (COLS - 1));
    }

    /**
     * CSS background-position 계산을 위한 퍼센트 값 반환 (세로)
     */
    public static double getYPercent(Card card) {
        return (double) getRowIndex(card) * (100.0 / (ROWS - 1));
    }
}

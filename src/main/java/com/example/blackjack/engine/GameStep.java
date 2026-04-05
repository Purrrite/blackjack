package com.example.blackjack.engine;

import com.example.blackjack.model.Card;
import java.util.List;

public record GameStep(
    String action,
    List<Card> playerHand,
    List<Card> dealerHand,
    int playerScore,
    int dealerScore,
    String result,
    long currentMoney
) {}

package com.example.blackjack.engine;

import java.util.List;

public record DetailedGameRecord(
    List<GameStep> steps,
    long finalMoney
) {}

package com.example.blackjack.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "blackjack.simulation")
public class BlackjackConfig {
    private int count = 100_000;
    private long initialMoney = 1_000_000;
    private int betAmount = 100;
    private int deckCount = 6;
    private double shuffleThreshold = 0.8;

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public long getInitialMoney() { return initialMoney; }
    public void setInitialMoney(long initialMoney) { this.initialMoney = initialMoney; }
    public int getBetAmount() { return betAmount; }
    public void setBetAmount(int betAmount) { this.betAmount = betAmount; }
    public int getDeckCount() { return deckCount; }
    public void setDeckCount(int deckCount) { this.deckCount = deckCount; }
    public double getShuffleThreshold() { return shuffleThreshold; }
    public void setShuffleThreshold(double shuffleThreshold) { this.shuffleThreshold = shuffleThreshold; }
}

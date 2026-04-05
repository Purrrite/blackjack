package com.example.blackjack.controller;

import com.example.blackjack.service.SimulationResult;
import com.example.blackjack.service.SimulationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/simulate")
    public String showResult(
            @RequestParam(defaultValue = "100000") int count,
            @RequestParam(defaultValue = "1000000") long initialMoney,
            @RequestParam(defaultValue = "100") int betAmount,
            @RequestParam(required = false) Long finalMoney,
            @RequestParam(required = false) Integer pWins,
            @RequestParam(required = false) Integer pBJ,
            @RequestParam(required = false) Integer dWins,
            @RequestParam(required = false) Integer dBJ,
            @RequestParam(required = false) Integer ties,
            Model model) {
        
        SimulationResult result;
        if (finalMoney != null) {
            java.util.Map<com.example.blackjack.engine.GameResult, Integer> stats = new java.util.EnumMap<>(com.example.blackjack.engine.GameResult.class);
            stats.put(com.example.blackjack.engine.GameResult.PLAYER_WIN, pWins != null ? pWins : 0);
            stats.put(com.example.blackjack.engine.GameResult.PLAYER_BLACKJACK, pBJ != null ? pBJ : 0);
            stats.put(com.example.blackjack.engine.GameResult.DEALER_WIN, dWins != null ? dWins : 0);
            stats.put(com.example.blackjack.engine.GameResult.DEALER_BLACKJACK, dBJ != null ? dBJ : 0);
            stats.put(com.example.blackjack.engine.GameResult.PUSH, ties != null ? ties : 0);
            
            long totalWagered = (long) betAmount * count;
            double roi = (double) (finalMoney - initialMoney) / totalWagered * 100;
            result = new SimulationResult(count, stats, initialMoney, finalMoney, roi);
        } else {
            result = simulationService.runSimulation(count, initialMoney, betAmount);
        }
        
        model.addAttribute("result", result);
        return "result";
    }

    @GetMapping("/visual")
    public String showVisual(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "1000000") long initialMoney,
            @RequestParam(defaultValue = "100") int betAmount,
            Model model) {
        model.addAttribute("count", count);
        model.addAttribute("initialMoney", initialMoney);
        model.addAttribute("betAmount", betAmount);
        return "visual";
    }
}

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
            Model model) {
        
        SimulationResult result = simulationService.runSimulation(count, initialMoney, betAmount);
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

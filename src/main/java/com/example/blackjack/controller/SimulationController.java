package com.example.blackjack.controller;

import com.example.blackjack.service.SimulationResult;
import com.example.blackjack.service.SimulationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/")
    public String showResult(Model model) {
        SimulationResult result = simulationService.runSimulation();
        model.addAttribute("result", result);
        return "result";
    }
}

package com.example.blackjack.controller;

import com.example.blackjack.engine.DetailedGameRecord;
import com.example.blackjack.service.SimulationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SimulationApiController {

    private final SimulationService simulationService;

    public SimulationApiController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @GetMapping("/simulate/visual")
    public List<DetailedGameRecord> getVisualSimulation(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(defaultValue = "1000000") long initialMoney,
            @RequestParam(defaultValue = "100") int betAmount) {
        return simulationService.runDetailedSimulation(count, initialMoney, betAmount);
    }
}

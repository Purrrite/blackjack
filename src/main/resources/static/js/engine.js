import { CONFIG } from './config.js';

export class SimulationEngine {
    constructor(ui) {
        this.ui = ui;
        this.state = {
            gamesData: [],
            currentGameIdx: 0,
            currentStepIdx: 0,
            isSkipped: false,
            tickDelay: 100,
            stats: { pWins: 0, pBJ: 0, dWins: 0, dBJ: 0, ties: 0 }
        };
        this.onComplete = null;
    }

    async loadData(params) {
        const url = `${CONFIG.API_ENDPOINT}?count=${params.count}&initialMoney=${params.initialMoney}&betAmount=${params.betAmount}`;
        const response = await fetch(url);
        this.state.gamesData = await response.json();
    }

    start() {
        this.tick();
    }

    tick() {
        if (this.state.isSkipped) return;

        if (this.state.currentGameIdx >= this.state.gamesData.length) {
            if (this.onComplete) this.onComplete();
            return;
        }

        const game = this.state.gamesData[this.state.currentGameIdx];
        const step = game.steps[this.state.currentStepIdx];

        this.ui.render(step, this.state.currentGameIdx + 1, this.state.gamesData.length);
        
        if (step.action === "GAME_OVER") {
            this.updateStats(step.result);
        }

        this.nextStep(game.steps.length);
        setTimeout(() => this.tick(), this.state.tickDelay);
    }

    updateStats(result) {
        const keyMap = { 
            'PLAYER_WIN': 'pWins', 
            'PLAYER_BLACKJACK': 'pBJ', 
            'DEALER_WIN': 'dWins', 
            'DEALER_BLACKJACK': 'dBJ',
            'PUSH': 'ties'
        };
        if (keyMap[result]) {
            this.state.stats[keyMap[result]]++;
            this.ui.updateStats(this.state.stats);
        }
    }

    nextStep(maxSteps) {
        this.state.currentStepIdx++;
        if (this.state.currentStepIdx >= maxSteps) {
            this.state.currentGameIdx++;
            this.state.currentStepIdx = 0;
        }
    }

    setSpeed(value) {
        this.state.tickDelay = 510 - value;
        this.ui.updateSpeedDisplay(value);
    }

    skip() {
        this.state.isSkipped = true;
        if (this.onComplete) this.onComplete();
    }
}

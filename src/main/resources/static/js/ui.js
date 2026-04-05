import { CONFIG } from './config.js';

export class UIRenderer {
    constructor() {
        this.dom = {
            money: document.getElementById('current-money'),
            progress: document.getElementById('game-progress'),
            action: document.getElementById('current-action'),
            dealerCards: document.getElementById('dealer-cards'),
            playerCards: document.getElementById('player-cards'),
            dealerScore: document.getElementById('dealer-score'),
            playerScore: document.getElementById('player-score'),
            stats: {
                pWins: document.getElementById('p-wins'),
                pBJ: document.getElementById('p-bj'),
                dWins: document.getElementById('d-wins'),
                dBJ: document.getElementById('d-bj'),
                ties: document.getElementById('ties')
            },
            speedValue: document.getElementById('speed-value')
        };
    }

    render(step, gameNum, totalGames) {
        if (this.dom.money) this.dom.money.innerText = step.currentMoney.toLocaleString();
        if (this.dom.progress) this.dom.progress.innerText = `${gameNum} / ${totalGames}`;
        if (this.dom.action) this.dom.action.innerText = step.action;
        if (this.dom.dealerScore) this.dom.dealerScore.innerText = step.dealerScore;
        if (this.dom.playerScore) this.dom.playerScore.innerText = step.playerScore;

        this.renderHand(this.dom.dealerCards, step.dealerHand);
        this.renderHand(this.dom.playerCards, step.playerHand);
    }

    renderHand(container, hand) {
        if (!container) return;
        container.innerHTML = '';
        hand.forEach(card => {
            const cardEl = document.createElement('div');
            cardEl.className = 'card-view';
            
            const rankIdx = CONFIG.CARD.RANKS.indexOf(card.rank);
            const suitIdx = CONFIG.CARD.SUITS.indexOf(card.suit);
            
            // X 좌표: 배열에서 직접 추출
            const posX = CONFIG.CARD.X_COORDS[rankIdx];
            // Y 좌표: Bottom-Left 기준이므로 Top-Left로 변환 (Y - 카드높이)
            const posY = CONFIG.CARD.Y_COORDS[suitIdx] - CONFIG.CARD.HEIGHT;
            
            // background-position은 음수 값을 사용하여 이미지를 이동시킴
            cardEl.style.backgroundPosition = `-${posX}px -${posY}px`;
            container.appendChild(cardEl);
        });
    }

    updateStats(stats) {
        for (const key in stats) {
            if (this.dom.stats[key]) {
                this.dom.stats[key].innerText = stats[key];
            }
        }
    }

    updateSpeedDisplay(value) {
        if (this.dom.speedValue) this.dom.speedValue.innerText = `${value}%`;
    }
}

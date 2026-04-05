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
        this.actionSound = new Audio('/action.wav');
        this.actionSound.volume = 0.5; // 음량 50% 설정
    }

    render(step, gameNum, totalGames) {
        if (this.dom.money) this.dom.money.innerText = step.currentMoney.toLocaleString();
        if (this.dom.progress) this.dom.progress.innerText = `${gameNum} / ${totalGames}`;
        if (this.dom.action) this.dom.action.innerText = step.action;
        if (this.dom.dealerScore) this.dom.dealerScore.innerText = step.dealerScore;
        if (this.dom.playerScore) this.dom.playerScore.innerText = step.playerScore;

        this.renderHand(this.dom.dealerCards, step.dealerHand);
        this.renderHand(this.dom.playerCards, step.playerHand);

        // 행동이 일어날 때 사운드 재생
        if (step.action && step.action !== 'WAITING' && step.action !== 'GAME_OVER') {
            this.playActionSound();
        }
    }

    playActionSound() {
        if (this.actionSound) {
            // 무작위 Pitch 설정 (0.8 - 1.2배)
            const randomPitch = Math.random() * (1.2 - 0.8) + 0.8;
            this.actionSound.playbackRate = randomPitch;

            // 브라우저에 따라 pitch 변경을 허용하도록 설정 (기본값은 false인 경우가 많음)
            if ('preservesPitch' in this.actionSound) {
                this.actionSound.preservesPitch = false;
            } else if ('mozPreservesPitch' in this.actionSound) {
                this.actionSound.mozPreservesPitch = false;
            } else if ('webkitPreservesPitch' in this.actionSound) {
                this.actionSound.webkitPreservesPitch = false;
            }

            this.actionSound.currentTime = 0;
            this.actionSound.play().catch(e => console.log('Audio play failed:', e));
        }
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
        if (this.dom.speedValue) this.dom.speedValue.innerText = `${value} TPS`;
    }
}

let gamesData = [];
let currentGameIdx = 0;
let currentStepIdx = 0;
let isSkipped = false;
let tickTimeout = null;
let tickDelay = 100;

// URL 파라미터에서 초기 설정값 가져오기
const urlParams = new URLSearchParams(window.location.search);
const count = urlParams.get('count') || 10;
const initialMoney = urlParams.get('initialMoney') || 1000000;
const betAmount = urlParams.get('betAmount') || 100;

async function initSimulation() {
    try {
        const response = await fetch(`/api/simulate/visual?count=${count}&initialMoney=${initialMoney}&betAmount=${betAmount}`);
        gamesData = await response.json();
        updateDashboard();
        runTick();
    } catch (error) {
        console.error("Failed to load simulation data:", error);
    }
}

function runTick() {
    if (isSkipped) return;

    if (currentGameIdx >= gamesData.length) {
        finishSimulation();
        return;
    }

    const game = gamesData[currentGameIdx];
    const step = game.steps[currentStepIdx];

    renderStep(step, currentGameIdx + 1);

    currentStepIdx++;
    if (currentStepIdx >= game.steps.length) {
        currentGameIdx++;
        currentStepIdx = 0;
    }

    tickTimeout = setTimeout(runTick, tickDelay);
}

function renderStep(step, gameNum) {
    // 대시보드 업데이트
    document.getElementById('current-money').innerText = step.currentMoney.toLocaleString();
    document.getElementById('game-progress').innerText = `${gameNum} / ${gamesData.length}`;
    document.getElementById('current-action').innerText = step.action;

    // 보드 렌더링 (애니메이션 없이 즉시 업데이트)
    const dealerContainer = document.getElementById('dealer-cards');
    const playerContainer = document.getElementById('player-cards');

    dealerContainer.innerHTML = '';
    playerContainer.innerHTML = '';

    step.dealerHand.forEach(card => {
        dealerContainer.appendChild(createCardElement(card));
    });

    step.playerHand.forEach(card => {
        playerContainer.appendChild(createCardElement(card));
    });

    document.getElementById('dealer-score').innerText = step.dealerScore;
    document.getElementById('player-score').innerText = step.playerScore;
}

function createCardElement(card) {
    const div = document.createElement('div');
    div.className = 'card-view';
    // Ensure card image is visible
    div.style.backgroundImage = "url('/cards.png')";
    div.style.backgroundSize = "1300% 500%";
    
    // CardSprite 로직과 동일한 계산 방식 적용 (13열 5행)
    const rankOrder = ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"];
    const suitOrder = ["SPADES", "HEARTS", "DIAMONDS", "CLUBS"];
    
    const x = rankOrder.indexOf(card.rank) * (100 / 12);
    const y = suitOrder.indexOf(card.suit) * (100 / 4);
    
    div.style.backgroundPosition = `${x.toFixed(2)}% ${y.toFixed(2)}%`;
    return div;
}

function updateDashboard() {
    const targetElement = document.getElementById('target-games');
    if (targetElement) {
        targetElement.innerText = gamesData.length;
    }
}

function setSpeed(value) {
    tickDelay = 510 - value; // 슬라이더 값이 높을수록 딜레이는 낮게
    document.getElementById('speed-value').innerText = `${value}%`;
}

function skipSimulation() {
    isSkipped = true;
    clearTimeout(tickTimeout);
    finishSimulation();
}

function finishSimulation() {
    // 최종 요약 화면으로 이동 (기존 요약 컨트롤러 활용)
    window.location.href = `/simulate?count=${count}&initialMoney=${initialMoney}&betAmount=${betAmount}`;
}

// 속도 슬라이더 이벤트 리스너
document.getElementById('speed-range').addEventListener('input', (e) => {
    setSpeed(e.target.value);
});

// 초기화 시작
document.addEventListener('DOMContentLoaded', () => {
    initSimulation();
});

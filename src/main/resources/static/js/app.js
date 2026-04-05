import { SimulationEngine } from './engine.js';
import { UIRenderer } from './ui.js';

document.addEventListener('DOMContentLoaded', async () => {
    const ui = new UIRenderer();
    const engine = new SimulationEngine(ui);

    // URL 파라미터 파싱
    const urlParams = new URLSearchParams(window.location.search);
    const params = {
        count: urlParams.get('count') || 10,
        initialMoney: urlParams.get('initialMoney') || 1000000,
        betAmount: urlParams.get('betAmount') || 100
    };

    // 데이터 로드 및 엔진 준비
    await engine.loadData(params);
    
    engine.onComplete = () => {
        window.location.href = `/simulate?count=${params.count}&initialMoney=${params.initialMoney}&betAmount=${params.betAmount}`;
    };

    // 속도 초기화
    engine.setSpeed(410);
    engine.start();

    // 전역 이벤트 바인딩
    const speedRange = document.getElementById('speed-range');
    if (speedRange) {
        speedRange.addEventListener('input', (e) => engine.setSpeed(parseInt(e.target.value)));
    }

    // 전역 스코프에 스킵 함수 노출
    window.skipSimulation = () => engine.skip();
});

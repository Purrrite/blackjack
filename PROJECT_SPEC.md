# Blackjack Simulation Project Specification

이 프로젝트는 Java와 Spring Boot를 기반으로 한 블랙잭 게임 시뮬레이터입니다. 대규모 시뮬레이션을 통해 특정 전략의 기대 수익률(ROI)과 승률 통계를 분석하는 것을 목적으로 합니다.

## 1. 기술 스택
- **Language:** Java 21 (record, switch expressions 활용)
- **Framework:** Spring Boot 3.4.1
- **Build Tool:** Gradle
- **Template Engine:** Thymeleaf (HTML 렌더링)
- **Styling:** Vanilla CSS (카지노 테마 및 스프라이트 카드 렌더링)

## 2. 프로젝트 구조 (Package Structure)
- `com.example.blackjack`
    - `BlackjackApplication.java`: 애플리케이션 진입점 및 설정 스캔
    - `config`: 설정 관리
        - `BlackjackConfig.java`: 시뮬레이션 관련 상수 (횟수, 자본, 덱 개수 등) 관리
    - `controller`: 웹 요청 처리
        - `SimulationController.java`: 입력 폼(`index.html`) 및 결과 페이지(`result.html`) 매핑
    - `engine`: 핵심 게임 로직
        - `BlackjackEngine.java`: 단일 게임 진행 및 승패 판정
        - `GameResult.java`: 게임 결과 상태 (WIN, BLACKJACK, PUSH 등) 정의
    - `model`: 도메인 객체
        - `Card.java`, `Rank.java`, `Suit.java`: 카드 기본 구성
        - `Deck.java`: 6덱 시스템 및 셔플 로직
        - `Participant.java`: 플레이어/딜러 공통 추상 클래스
        - `CardSprite.java`: 스프라이트 이미지 좌표 계산 유틸리티
    - `service`: 비즈니스 로직
        - `SimulationService.java`: 대규모 시뮬레이션 실행 및 통계 집계
        - `SimulationResult.java`: 시뮬레이션 최종 통계 데이터 객체

## 3. 핵심 기능 및 규칙
### 게임 규칙
- **덱 구성:** 기본 6덱 (312장) 사용.
- **셔플:** 덱의 80% 이상 사용 시 자동으로 셔플 진행.
- **블랙잭:** 첫 두 장의 합이 21일 경우 1.5배의 배당 지급.
- **에이스(Ace) 처리:** 합계가 21을 초과할 경우 자동으로 1로 계산하는 소프트/하드 핸드 로직 포함.
- **딜러 전략:** 합계가 17점 미만일 경우 반드시 히트(Hit).

### 시뮬레이션 전략 (Basic Strategy)
- **플레이어 전략:** 딜러의 오픈 카드가 7점 이상일 경우 17점까지 히트, 6점 이하일 경우 12점까지 히트하는 단순화된 베이직 전략 적용.

### ROI 계산 공식
- `ROI = (최종 자본 - 초기 자본) / (판당 베팅액 * 총 게임 횟수) * 100`
- 이는 총 베팅 금액 대비 플레이어의 평균 수익률(House Edge의 역수)을 나타냄.

## 4. UI/UX 구성
- **Index 페이지 (`/`):** 
    - 시뮬레이션 횟수, 초기 자본, 베팅 금액 입력 폼 제공.
- **Result 페이지 (`/simulate`):**
    - 최종 자산 변화 및 ROI 시각적 표시 (양수: 초록색, 음수: 빨간색).
    - 게임 결과별 상세 통계 표 제공.

## 5. 정적 리소스 및 시각화
- **카드 이미지:** `src/main/resources/static/cards.png` (13x5 스프라이트 시트).
- **좌표 매핑:** `CardSprite` 클래스를 통해 이미지 내 개별 카드 좌표를 CSS `background-position`으로 변환.
- **설정 파일:** `src/main/resources/application.properties`를 통해 코드 수정 없이 기본 시뮬레이션 환경 설정 가능.

---
*Last Updated: 2024-04-05*

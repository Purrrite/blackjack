export const CONFIG = {
    CARD: {
        X_COORDS: [21, 110, 199, 288, 377, 466, 555, 644, 733, 822, 911, 1000, 1089, 1178],
        Y_COORDS: [136, 272, 408, 544, 680],

        // 랭크 및 슈트 순서 매핑
        RANKS: ["ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"],
        SUITS: ["SPADES", "HEARTS", "DIAMONDS", "CLUBS"],

        WIDTH: 80,
        HEIGHT: 116,
        IMAGE_SIZE: "1280px 698px"
    },
    API_ENDPOINT: "/api/simulate/visual"
};

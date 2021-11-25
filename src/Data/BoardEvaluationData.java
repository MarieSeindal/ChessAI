package Data;

public class BoardEvaluationData {

    public static int getWhitePawnValue(int i, int j) {
        return pawnTable[i][j];
    }

    public static int getRookValue(int i, int j) {
        return rookTable[i][j];
    }

    public static int getBishopValue(int i, int j) {
        return bishopTable[i][j];
    }

    public static int getKnightValue(int i, int j) {
        return knightTable[i][j];
    }

    public static int getQueenValue(int i, int j) {
        return queenTable[i][j];
    }

    public static int getKingValue(int i, int j) {
        return kingTable[i][j];
    }

    public static int[][] rotateTheTable180(int[][] input) {

        int[][] output = new int[8][8];

        int countY = 0;
        int countX = 0;

        for (int y = 7; y > -1; y--) {
            int[] row = new int[8];
            for (int x = 7; x > -1; x--) {
                output[countY][countX] = input[y][x];

                countX++;
            }

            countX = 0;
            countY++;
        }

        return output;
    }

    // Pawn position score
    public static final int[][] pawnTable = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {7, 7, 13, 23, 26, 13, 7, 7},
            {-2, -2, 4, 12, 15, 4, -2, -2},
            {-3, -3, 2, 9, 11, 2, -3, -3},
            {-4, -4, 0, 6, 8, 0, -4, -4},
            {-4, -4, 0, 4, 6, 0, -4, -4},
            {-1, -1, 1, 5, 6, 1, -1, -1},
            {0, 0, 0, 0, 0, 0, 0, 0},
    };

    // Rook position score
    public static final int[][] rookTable = {
            {9, 9, 11, 10, 11, 9, 9, 9},
            {4, 6, 7, 9, 9, 7, 6, 4},
            {9, 10, 10, 11, 11, 10, 10, 9},
            {8, 8, 8, 9, 9, 8, 8, 8},
            {6, 6, 5, 6, 6, 5, 6, 6},
            {4, 5, 5, 5, 5, 5, 5, 4},
            {3, 4, 4, 6, 6, 4, 4, 3},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    // Bishop position score
    public static final int[][] bishopTable = {
            {2, 3, 4, 4, 4, 4, 3, 2},
            {4, 7, 7, 7, 7, 7, 7, 4},
            {3, 5, 6, 6, 6, 6, 5, 3},
            {3, 5, 7, 7, 7, 7, 5, 4},
            {4, 5, 6, 8, 8, 6, 5, 4},
            {4, 5, 5, -2, -2, 5, 5, 4},
            {5, 5, 5, 3, 3, 5, 5, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    // Knight position score
    public static final int[][] knightTable = {
            {-2, 2, 7, 9, 9, 7, 2, -2},
            {1, 4, 12, 13, 13, 12, 4, 1},
            {5, 11, 18, 19, 19, 18, 11, 5},
            {3, 10, 14, 14, 14, 14, 10, 3},
            {0, 5, 8, 9, 9, 8, 5, 0},
            {-3, 1, 3, 4, 4, 4, 1, -3},
            {-5, -3, -1, 0, 0, -1, -3, -5},
            {-7, -5, -4, -2, -2, -4, -5, -7}
    };

    // Queen position score
    public static final int[][] queenTable = {
            {2, 3, 4, 3, 4, 3, 3, 2},
            {2, 3, 4, 4, 4, 4, 3, 2},
            {3, 4, 4, 4, 4, 4, 4, 3},
            {3, 3, 4, 4, 4, 4, 3, 3},
            {2, 3, 3, 4, 4, 3, 3, 2},
            {2, 2, 2, 3, 3, 2, 2, 2},
            {2, 2, 2, 2, 2, 2, 2, 2},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };

    // King position score
    public static final int[][] kingTable = {
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };
}

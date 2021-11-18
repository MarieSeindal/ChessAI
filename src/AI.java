import Data.BoardEvaluationData;

import java.io.*;

public class AI {

    private static int maxDepth = 5;
    private Board currentBoard;

    public AI(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public void runAI() {
    }

    public int minimax(ChessNode nodeToSearch, int depth, boolean isMax, int alpha, int beta) {

        System.out.println("Depth in minimax " + depth); //todo delete after use

        // If leaf node, return static value of the board
        if (depth == maxDepth) {
            System.out.println("Leaf return in depth " + depth);
            return evaluateLeaf(); //todo. expand evaluateLeaf()
        }

        // node in depth 0,2 ... straight numbers is max, as the algorithm runs the AI's turn.
        else if (isMax) {
            return maximizer(nodeToSearch, depth, alpha, beta);
        } else if (!isMax) { // node in depth 1,3 ... unequal numbers is min, as the algorithm runs the opponents turn.
            return minimizer(nodeToSearch, depth, alpha, beta);
        }
        return 0;
    }

    private int maximizer(ChessNode nodeToSearch, int depth, int alpha, int beta) {
        int bestValue = alpha;
        int value;
        System.out.println("Max alpha " + alpha); //todo debug print
        System.out.println("Max beta " + beta); //todo debug print

        //todo Fill children to list //Ad children to the parents arraylist
        fillChildren(nodeToSearch);

        for (ChessNode child : nodeToSearch.getChildren()) {
            value = minimax(child, depth + 1, false, alpha, beta);
            bestValue = max(bestValue, value);
            alpha = max(alpha, bestValue);

            if (beta <= alpha) break;
        }
        return bestValue;
    }

    private int minimizer(ChessNode nodeTosearch, int depth, int alpha, int beta) {
        int bestValue = beta;
        int value;
        System.out.println("Min alpha " + alpha);
        System.out.println("Min beta " + beta);

        //todo Fill children to list //Ad children to the parents arraylist
        fillChildren(nodeTosearch);

        for (ChessNode child : nodeTosearch.getChildren()) {
            value = minimax(child, depth + 1, true, alpha, beta);
            bestValue = min(bestValue, value);
            beta = min(beta, bestValue);

            if (beta <= alpha) break;
        }
        return bestValue;
    }

    // - - - - - Evaluate - - - - - //todo exapnd these
    public int evaluateLeaf() { // so far based on slide 11 in pptx week 6.
        int sum = 0;

        sum += evaluatePawn(0, 0);
        sum += evaluateRook(0, 0);
        sum += evaluateBishop(0, 0);
        sum += evaluateKnight(0, 0);
        sum += evaluateQueen(0, 0);
        sum += evaluateKing(0, 0);
        return sum;
    }

    // todo should differentiate between min/max, white/black
    // Takes position as argument and performs lookup of position value in value table
    private int evaluatePawn(int i, int j) {
        return 100 + BoardEvaluationData.getWhitePawnValue(i, j);
    }



    public void fillChildren(ChessNode parent){

        // get parrent board
        char[][] boardParent = parent.getBoard().getBoardArray();

        for (char[] row : boardParent) {
            for (char field : row) {
                //Check each field for each possible move.

                //When a move is found, clone it
                ChessNode copy = parent.clone(); //make copy

                //Then execute the move on the clone
                //copy.getBoard().performMove( INSERT MOVE ); // Make move //todo inset move

                //Add copy to list
                parent.addChildren(copy); //add child to the arraylist

            }
        }
    }

    private int evaluateRook(int i, int j) {
        return 500 + BoardEvaluationData.getRookValue(i, j);
    }

    private int evaluateBishop(int i, int j) {
        return 300 + BoardEvaluationData.getBishopValue(i, j);
    }

    private int evaluateKnight(int i, int j) {
        return 300 + BoardEvaluationData.getKnightValue(i, j);
    }

    private int evaluateQueen(int i, int j) {
        return 900 + BoardEvaluationData.getQueenValue(i, j);
    }

    private int evaluateKing(int i, int j) {
        return 10000 + BoardEvaluationData.getKingValue(i, j);
    }

//- - - - - - - - - - - - - - - - - - - - //

    public int max(int a, int b) {
        if (a > b) return a;
        else return b;
    }

    public int min(int a, int b) {
        if (a < b) return a;
        else return b;
    }

}

import Data.BoardEvaluationData;

import java.lang.*;
import java.util.ArrayList;

public class AI {

    private static int maxDepth = 2;
    protected Board currentBoard;
    private boolean isWhite = false;
    private int alpha = -10000000;
    private int beta = 10000000;
    public Board bestMoveBoard;
    public int nodeScore = -10000;

    public AI(){
        this.currentBoard = new Board();
        this.isWhite = true;
    }

    public AI(Board currentBoard, boolean isWhite) {
        this.currentBoard = currentBoard;
        this.isWhite = isWhite;
    }



    public void runAI(ChessNode firstNode) {
        System.out.println("Running AI!");
        minimax(firstNode, 0,true, alpha, beta);
    }

    public Board getBestMoveBoard(){
        return this.bestMoveBoard;
    }

    public void setCurrentPlayerAI(boolean isWhite){ // todo call this from Game(?) to tell the AI if it is black or white
        this.isWhite = isWhite;
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

            //Get best move
            if (depth == 1){
                System.out.println("Hurray we made it into if depth = 1 ");

                if ( bestValue > nodeScore){ //if we find a best value that is better than the recorded nodeScore, that move
                    System.out.println("Hurray we made it into if nodeScore score " + nodeScore);
                    nodeScore = bestValue;

                    // construct best moveBoard
                    bestMoveBoard = nodeTosearch.getBoard();
                }
            }

            if (beta <= alpha) break;
        }
        System.out.println("Returning in AI, best val: " + bestValue);
        return bestValue;
    }


    public void fillChildren(ChessNode parent){

        // get parrent board
        char[][] boardParent = parent.getBoard().getBoardArray();

        ArrayList<int[]> tempListOfMoves = new ArrayList<int[]>();

        int rows=0;
        for (char[] row : boardParent) {
            int column=0;
            for (char piece : row) {

                // don't do anything, if the piece is an empty spot
                if(piece != ' ') {

                    //Check each piece for each possible move.
                    if (isWhite && Character.isUpperCase(piece)) { //If it's the current player, and It's that players pieces.
                        //Get possible moves for that piece
                        int[] coords = {rows, column};
                        tempListOfMoves = Game.pieceMoveset(piece, coords, currentBoard, isWhite);

                        // we don't add anything to the parent, if there is no moves to make
                        if (tempListOfMoves.size() >= 1) {

                            for (int[] moveFound : tempListOfMoves) {

                                //When a move is found, clone it
                                Board copyOfBoard = parent.getBoard().clone();
                                //ChessNode copy = parent.clone(); //make copy
                                ChessNode copy = new ChessNode(copyOfBoard); //make copy

                                //todo Figure out if its a special move. Now assumes that AI cannot make special moves

                                //Create the move
                                Move makeMove = new Move(moveFound, coords, false, piece, currentBoard.getPiece(rows, column));

                                //Then execute the move on the clone
                                copy.getBoard().performMove(makeMove); // Make move

                                //Add copy to list
                                parent.addChildren(copy); //add child to the arraylist
                            }
                        }
                        //isWhite = !isWhite;
                    } else if (!isWhite && Character.isLowerCase(piece)) { //black
                        //Get possible moves for that piece
                        int[] coords = {rows, column};
                        tempListOfMoves = Game.pieceMoveset(piece, coords, currentBoard, isWhite);
                        for (int[] moveFound : tempListOfMoves) {

                            //When a move is found, clone it
                            ChessNode copy = parent.clone(); //make copy

                            //todo Figure out if its a special move. Now assumes that AI cannot make special moves

                            //Create the move
                            Move makeMove = new Move(moveFound, coords, false, piece, currentBoard.getPiece(rows, column));

                            //Then execute the move on the clone
                            copy.getBoard().performMove(makeMove); // Make move

                            //Add copy to list
                            parent.addChildren(copy); //add child to the arraylist
                        }
                        //isWhite = !isWhite;
                    }
                }
                column++;
            }
            rows++;
        }
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

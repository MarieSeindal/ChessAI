import Data.BoardEvaluationData;

import java.lang.*;
import java.util.ArrayList;

public class AI {

    static int timeLimitSeconds = 10;
    private static long timeLimit;
    private static int maxDepth = 4;
    protected Board currentBoard;
    private boolean isWhite = false;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    public Board bestMoveBoard;

    public AI(Board currentBoard, boolean isWhite, int timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.currentBoard = currentBoard;
        this.isWhite = isWhite;
        bestMoveBoard = currentBoard;
    }

    public AI() {
        this.currentBoard = new Board();
    }

    public void runAI(ChessNode firstNode) {
        System.out.println("Running AI as " + (isWhite ? "white" : "black") + " player!");
        timeLimit = System.currentTimeMillis() / 1000;
        minimax(firstNode, 0, isWhite, alpha, beta, false);
    }

    public Board getBestMoveBoard() {
        return this.bestMoveBoard;
    }

    public void setCurrentPlayerAI(boolean isWhite) { // todo call this from Game(?) to tell the AI if it is black or white
        this.isWhite = isWhite;
    }

    public int minimax(ChessNode nodeToSearch, int depth, boolean isWhite, int alpha, int beta, boolean stop) {

        // If leaf node, return static value of the board
        if (depth == maxDepth || stop) {
            return evaluateLeaf(bestMoveBoard, isWhite); //todo. expand evaluateLeaf()
        }

        // node in depth 0,2 ... straight numbers is max, as the algorithm runs the AI's turn.
        else if (isWhite) {
            return maximizer(nodeToSearch, depth, alpha, beta);
        } else if (!isWhite) { // node in depth 1,3 ... unequal numbers is min, as the algorithm runs the opponents turn.
            return minimizer(nodeToSearch, depth, alpha, beta);
        }

        return 0;
    }

    private int maximizer(ChessNode nodeToSearch, int depth, int alpha, int beta) {
        int bestValue;
        int newValue;
        bestValue = Integer.MIN_VALUE;

        //todo Fill children to list //Ad children to the parents arraylist
        fillChildren(nodeToSearch, true);

        for (ChessNode child : nodeToSearch.getChildren()) {
            // Check for timelimit and make minimax return bestBoard
            if (((System.currentTimeMillis() / 1000) - timeLimit) >= timeLimitSeconds) {
                minimax(child, depth + 1, false, alpha, beta, true);
                break;
            }
            newValue = minimax(child, depth + 1, false, alpha, beta, false);

            if (newValue > bestValue) { //if we find a new best value that is better than the recorded nodeScore, that move
                bestValue = newValue;

                if (depth == 1) {
                    // construct best moveBoard
                    bestMoveBoard = nodeToSearch.getBoard();
                }
            }

            alpha = Math.max(alpha, bestValue);
            if (beta <= alpha) {
                return bestValue;
            }
        }

        return bestValue;
    }

    private int minimizer(ChessNode nodeToSearch, int depth, int alpha, int beta) {
        int bestValue;
        int newValue;
        bestValue = Integer.MAX_VALUE;

        //todo Fill children to list //Ad children to the parents arraylist
        fillChildren(nodeToSearch, false);

        for (ChessNode child : nodeToSearch.getChildren()) {
            // Check for timelimit and make minimax return bestBoard
            if (((System.currentTimeMillis() / 1000) - timeLimit) >= timeLimitSeconds) {
                minimax(child, depth + 1, true, alpha, beta, true);
                break;
            }
            newValue = minimax(child, depth + 1, true, alpha, beta, false);

            if (newValue < bestValue) { //Check if we have a new min
                bestValue = newValue;

                if (depth == 1) {
                    // construct best moveBoard
                    bestMoveBoard = nodeToSearch.getBoard();
                }
            }

            beta = Math.min(beta, bestValue);
            if (beta <= alpha) { //Useless branch - don't explore
                return bestValue;
            }

        }
        return bestValue;
    }

    public void fillChildren(ChessNode parent, boolean white) {

        // get parrent board
        char[][] boardParent = parent.getBoard().getBoardArray();

        ArrayList<int[]> tempListOfMoves = new ArrayList<int[]>();

        int rows = 0;
        for (char[] row : boardParent) {
            int column = 0;
            for (char piece : row) {

                // don't do anything, if the piece is an empty spot
                if (piece != ' ') {

                    //Check each piece for each possible move.
                    if (white && Character.isUpperCase(piece)) { //If it's the current player, and It's that players pieces.
                        //Get possible moves for that piece
                        int[] coords = {rows, column};
                        if (rows > 7 || column > 7) {
                            System.out.println(rows + "," + column);
                        }
                        //System.out.println("white - fillChildren - cords - y :" + coords[0] + " | x : " + coords[1]);
                        tempListOfMoves = Game.pieceMoveset(piece, coords, currentBoard, true);

                        // we don't add anything to the parent, if there is no moves to make
                        if (tempListOfMoves.size() >= 1) {

                            for (int[] moveFound : tempListOfMoves) {

                                //When a move is found, clone it
                                Board copyOfBoard = parent.getBoard().cloning();
                                //ChessNode copy = parent.cloning(); //make copy
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
                    } else if (!white && Character.isLowerCase(piece)) { //black
                        //Get possible moves for that piece
                        int[] coords = {rows, column};
                        tempListOfMoves = Game.pieceMoveset(piece, coords, currentBoard, false);

                        // we don't add anything to the parent, if there is no moves to make
                        if (tempListOfMoves.size() >= 1) {

                            for (int[] moveFound : tempListOfMoves) {

                                //When a move is found, clone it
                                Board copyOfBoard = parent.getBoard().cloning();
                                //ChessNode copy = parent.cloning(); //make copy
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
                    }
                }
                column++;
            }
            rows++;
        }
    }

    // - - - - - Evaluate - - - - - //todo expand these
    public int evaluateLeaf(Board b, boolean isWhite) {

        char[][] boardArray = b.getBoardArray();

        int value = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char c = boardArray[i][j];
                if (!(c == ' ')) {
                    value += staticEvaluation(c, new int[]{i, j});
                }
            }
        }
        if (!isWhite) {
            value *= -1;
        }
        //System.out.println("board evaluation value is: " + value);
        return value;
    }

    public int staticEvaluation(char p, int[] c) { // so far based on slide 11 in pptx week 6.

        int evalScore = 0;

        switch (p) {
            case 'P': //White pawn
                evalScore += evaluatePawn(c[0], c[1]);
                break;
            case 'R': //White rook
                evalScore += evaluateRook(c[0], c[1]);
                break;
            case 'B': //White bishop
                evalScore += evaluateBishop(c[0], c[1]);
                break;
            case 'N': //White knight
                evalScore += evaluateKnight(c[0], c[1]);
                break;
            case 'Q': //White queen
                evalScore += evaluateQueen(c[0], c[1]);
                break;
            case 'K': //White king
                evalScore += evaluateKing(c[0], c[1]);
                break;
            case 'p': //Black pawn
                evalScore += evaluatePawn(7 - c[0], 7 - c[1]);
                break;
            case 'r': //Black rook
                evalScore += evaluateRook(7 - c[0], 7 - c[1]);
                break;
            case 'b': //Black bishop
                evalScore += evaluateBishop(7 - c[0], 7 - c[1]);
                break;
            case 'n': //Black knight
                evalScore += evaluateKnight(7 - c[0], 7 - c[1]);
                break;
            case 'q': //Black queen
                evalScore += evaluateQueen(7 - c[0], 7 - c[1]);
                break;
            case 'k': //Black king
                evalScore += evaluateKing(7 - c[0], 7 - c[1]);
                break;
        }
        return evalScore;
    }

    // todo should differentiate between min/max, white/black
    // Takes position as argument and performs lookup of position value in value table
    private int evaluatePawn(int i, int j) { return 10 + BoardEvaluationData.getWhitePawnValue(i, j); }

    private int evaluateRook(int i, int j) {
        return 50 + BoardEvaluationData.getRookValue(i, j);
    }

    private int evaluateBishop(int i, int j) {
        return 30 + BoardEvaluationData.getBishopValue(i, j);
    }

    private int evaluateKnight(int i, int j) {
        return 30 + BoardEvaluationData.getKnightValue(i, j);
    }

    private int evaluateQueen(int i, int j) {
        return 90 + BoardEvaluationData.getQueenValue(i, j);
    }

    private int evaluateKing(int i, int j) {
        return 900 + BoardEvaluationData.getKingValue(i, j);
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

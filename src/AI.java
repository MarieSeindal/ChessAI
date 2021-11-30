import Data.BoardEvaluationData;

import java.lang.*;
import java.util.ArrayList;

public class AI {

    private static int maxDepth = 2;
    static int timeLimitSeconds = 10;
    private static long timeLimit;
    protected Board currentBoard;
    private boolean isWhite = false;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
    public Board bestMoveBoard;
    public ArrayList<Board> usedBoards = new ArrayList<>();

    public AI(Board currentBoard, boolean isWhite, int timeLimitSeconds, ArrayList<Board> listOfBoards) {
        this.timeLimitSeconds = timeLimitSeconds;
        this.currentBoard = currentBoard;
        this.isWhite = isWhite;
        this.bestMoveBoard = currentBoard;
        // TODO: 
        this.usedBoards = listOfBoards;
    }

    public AI() {
        this.currentBoard = new Board();
    }

    // this is the master function, that runs everything
    public void runAI(ChessNode firstNode) {
        System.out.println("Running AI as " + (isWhite ? "white" : "black") + " player!");
        timeLimit = System.currentTimeMillis() / 1000;
        minimax(firstNode, 0, isWhite, alpha, beta, false);
    }

    // region minimax, maximizer and minimizer


    public int minimax(ChessNode nodeToSearch, int depth, boolean isWhite, int alpha, int beta, boolean stop) {

        // If leaf node, return static value of the board
        if (depth == maxDepth || stop) {
//            System.out.println("Leaf return in maxDepth " + depth);
//            System.out.println("alpha " + alpha);
//            System.out.println("beta " + beta);
            return evaluateLeaf(bestMoveBoard, isWhite); //todo. expand evaluateLeaf()
        }

        // node in depth 0,2 ... straight numbers is max, as the algorithm runs the AI's turn.
        else if (isWhite == true) {
            return maximizer(nodeToSearch, depth, alpha, beta);
        } else if (isWhite == false) { // node in depth 1,3 ... unequal numbers is min, as the algorithm runs the opponents turn.
            return minimizer(nodeToSearch, depth, alpha, beta);
        }

        return 0;
    }

    private int maximizer(ChessNode nodeToSearch, int depth, int alpha, int beta) {
        // TODO: this will always be white -----------------------********************!!!!!!!!!!!!!!!!!!!!!!!!$$$$$$$$$$$$$$$

        // System.out.println("Hello from max with depth "+depth);

        int bestValue;
        int newValue;
        bestValue = Integer.MIN_VALUE;

        int whiteKing = Game.checkTheKing(nodeToSearch.board, true);

        if(whiteKing == 404)
        {
            // System.out.println("********** AI - maximizer - GAME OVER: one king is dead, WHAT DID YOU DO !!!! **********");
            newValue = 20000000;
        }

        if(whiteKing != 0 && whiteKing != 404)
        {
            fillChildrenKingInCheck(nodeToSearch, true);
        }
        else if(whiteKing == 0)
        {
            fillChildren(nodeToSearch, true);
        }

        for (ChessNode child : nodeToSearch.getChildren()) {
            // Check for timelimit and make minimax return bestBoard
            if (((System.currentTimeMillis() / 1000) - timeLimit) >= timeLimitSeconds) {
                minimax(child, depth + 1, false, alpha, beta, true);
                break;
            }
            newValue = minimax(child, depth + 1, false, alpha, beta, false);

            if (newValue > bestValue) { //if we find a new best value that is better than the recorded nodeScore, that move
                // System.out.println("New max bestValue: " + newValue);

                bestValue = newValue;

                if (depth == 1) {
                    // construct best moveBoard
                    // TODO: look into this - this is just a note
                    bestMoveBoard = nodeToSearch.getBoard();
                }
            }

            alpha = Math.max(alpha, bestValue);
            if (beta <= alpha) {
                return bestValue;
            }
        }

        // System.out.println("Returning in AI, (**** maximizer ****) best val: " + bestValue);
        return bestValue;
    }

    private int minimizer(ChessNode nodeToSearch, int depth, int alpha, int beta) {
        // TODO: this will always be black -----------------------********************!!!!!!!!!!!!!!!!!!!!!!!!$$$$$$$$$$$$$$$

        // System.out.println("Hello from min with depth "+depth);

        int bestValue;
        int newValue;
        bestValue = Integer.MAX_VALUE;

        int whiteKing = Game.checkTheKing(nodeToSearch.board, false);

        if(whiteKing == 404)
        {
            // System.out.println("********** AI - minimizer - GAME OVER: their king is dead, WHAT DID WE DO !!!! **********");
            bestValue = -20000000;
        }

        if(whiteKing != 0 && whiteKing != 404)
        {
            fillChildrenKingInCheck(nodeToSearch, false);
        }
        else if(whiteKing == 0)
        {
            fillChildren(nodeToSearch, false);
        }

        for (ChessNode child : nodeToSearch.getChildren()) {
            // Check for timelimit and make minimax return bestBoard
            if (((System.currentTimeMillis() / 1000) - timeLimit) >= timeLimitSeconds) {
                minimax(child, depth + 1, true, alpha, beta, true);
                break;
            }
            newValue = minimax(child, depth + 1, true, alpha, beta, false);

            if (newValue < bestValue) { //Check if we have a new min
                // System.out.println("New min bestValue: " + newValue);
                bestValue = newValue;

                if (depth == 1) {
                    // construct best moveBoard
                    // TODO: look into this - this is just a note
                    bestMoveBoard = nodeToSearch.getBoard();
                }
            }

            beta = Math.min(beta, bestValue);
            if (beta <= alpha) { //Useless branch - don't explore
                return bestValue;
            }

        }
        // System.out.println("Returning in AI, (**** minimizer ****) best val: " + bestValue);
        return bestValue;
    }

    // endregion

    public void fillChildrenKingInCheck(ChessNode parent, boolean white)
    {
        Board tempBoard = parent.getBoard().cloning();
        ArrayList<int[]> locationsOfPieces = tempBoard.getAllPiece();

        for (int[] location: locationsOfPieces) {
            char piece = tempBoard.getPiece(location[0], location[1]);

            if(white && Character.isUpperCase(piece))
            {
                ArrayList<int[]> allMoves = Game.pieceMoveset(piece, location, tempBoard, true);
                ArrayList<int[]> filterMoves = Game.filterMoveset(tempBoard, piece, location, allMoves, true);

                if(filterMoves.size() > 0)
                {
                    for (int[] move: filterMoves) {
                        // RESETTER __________________________________________________________________
                        tempBoard = parent.getBoard().cloning();

                        //TODO: remove this line !!!!!
                        ArrayList<int[]> count = tempBoard.getAllPiece();

//                        //When a move is found, clone it
//                        Board copyOfBoard = tempBoard;
                        //ChessNode copy = parent.cloning(); //make copy
                        ChessNode copy = new ChessNode(tempBoard); //make copy
                        //todo Figure out if its a special move. Now assumes that AI cannot make special moves
                        //Create the move
                        Move makeMove = new Move(move, location, false, piece, 'o');
                        //Then execute the move on the clone
                        copy.getBoard().performMove(makeMove); // Make move

                        //Add copy to list
                        parent.addChildren(copy); //add child to the arraylist
                    }
                }
            }
            else if(!white && Character.isLowerCase(piece))
            {
                ArrayList<int[]> allMoves = Game.pieceMoveset(piece, location, tempBoard, true);
                ArrayList<int[]> filterMoves = Game.filterMoveset(tempBoard, piece, location, allMoves, true);

                if(filterMoves.size() > 0)
                {
                    for (int[] move: filterMoves) {
                        // RESETTER __________________________________________________________________
                        tempBoard = parent.getBoard().cloning();

                        //TODO: remove this line !!!!!
                        ArrayList<int[]> count = tempBoard.getAllPiece();

//                        //When a move is found, clone it
//                        Board copyOfBoard = parent.getBoard().cloning();
                        //ChessNode copy = parent.cloning(); //make copy
                        ChessNode copy = new ChessNode(tempBoard); //make copy
                        //todo Figure out if its a special move. Now assumes that AI cannot make special moves
                        //Create the move
                        Move makeMove = new Move(move, location, false, piece, 'o');
                        //Then execute the move on the clone
                        copy.getBoard().performMove(makeMove); // Make move

                        //Add copy to list
                        parent.addChildren(copy); //add child to the arraylist
                    }
                }
            }
        }
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
    public int evaluateLeaf(Board newBoard, boolean isWhite) {

        // int oldBoardValue = 0;
        int newBoardValue = 0;

        // 00 - check if the kings are in check, check mate or remis
        int ourKing = Game.checkTheKing(newBoard, isWhite);
        int enemyKing = Game.checkTheKing(newBoard, !isWhite);

//        if(ourKing == 404)
//        {
//            System.out.println("********** AI - GAME OVER: our king is dead, WHAT DID YOU DO !!!! **********");
//            return 5000000;
//        }
//        if(enemyKing == 404)
//        {
//            System.out.println("********** AI - GAME OVER: one king is dead, WHAT DID YOU DO !!!! **********");
//            return -5000000;
//        }

        // get all locations
        // ArrayList<int[]> oldLocations = this.currentBoard.getAllPiece();
        ArrayList<int[]> allPieces = newBoard.getAllPiece();

        // TODO: check if this works and change the values, so that it works
        // our king

        // region their king
        if(ourKing == 1) // check
        {
            // System.out.println("***___*** our King is in check");
            newBoardValue -= 500;
        }
        else if(ourKing == 2) // check mate
        {
            // System.out.println("***___*** our King is in check mate");
            newBoardValue -= 5000000;
        }
        else if(ourKing == 3) // remis
        {
            // System.out.println("***___*** our King is in remis");
            newBoardValue -= 1000;
        }
        // endregion

        // TODO: check if this works and change the values, so that it works
        // their king

        // region their king
        if(enemyKing == 1) // check
        {
            // System.out.println("***___*** the enemy King is in check");
            newBoardValue += 500;

        }
        else if(enemyKing == 2) // check mate
        {
            // System.out.println("***___*** the enemy King is in check mate");
            newBoardValue += 5000000;
        }
        else if(enemyKing == 3) // remis
        {
            // System.out.println("***___*** the enemy King is in remis");
            newBoardValue -= 1000;
        }
        // endregion

//        // old board
//        for (int[] location : oldLocations) {
//            char tempPiece = this.currentBoard.boardArray[location[0]][location[1]];
//
//            // black
//            if( Character.isLowerCase(tempPiece) &&  tempPiece != ' ')
//            {
//                if(isWhite)
//                    oldBoardValue -= staticEvaluation(tempPiece, new int[]{location[0],location[1]});
//                else
//                    oldBoardValue += staticEvaluation(tempPiece, new int[]{location[0],location[1]});
//            }
//
//            // white
//            if( Character.isUpperCase(tempPiece) &&  tempPiece != ' ')
//            {
//                if(isWhite)
//                    oldBoardValue += staticEvaluation(tempPiece, new int[]{location[0],location[1]});
//                else
//                    oldBoardValue -= staticEvaluation(tempPiece, new int[]{location[0],location[1]});
//            }
//        }




        // new board
        for (int[] location : allPieces) {
            char tempPiece = newBoard.boardArray[location[0]][location[1]];

            // black
            if( Character.isLowerCase(tempPiece) &&  tempPiece != ' ')
            {
                if(isWhite)
                    newBoardValue -= staticEvaluation(tempPiece, new int[]{location[0],location[1]});
                else
                    newBoardValue += staticEvaluation(tempPiece, new int[]{location[0],location[1]});
            }

            // white
            if( Character.isUpperCase(tempPiece) &&  tempPiece != ' ')
            {
                if(isWhite)
                    newBoardValue += staticEvaluation(tempPiece, new int[]{location[0],location[1]});
                else
                    newBoardValue -= staticEvaluation(tempPiece, new int[]{location[0],location[1]});
            }
        }

        // TODO: check with the debugger, if this makes bugs
//        if(isWhite == false) {
//            newBoardValue *= -1;
//        }

        //System.out.println("board evaluation value is: " + value);
        return newBoardValue;
    }

    // region all our static evaluations functions

    // returns a static kill value, based on the char type
    public int pieceEvaluation(char p) {

        int evalScore = 0;

        switch (p) {
            case 'P': //White pawn
            case 'p': //Black pawn
                evalScore = 100;
                break;
            case 'R': //White rook
            case 'r': //Black rook
                evalScore = 500;
                break;
            case 'B': //White bishop
            case 'b': //Black bishop
                evalScore = 300;
                break;
            case 'N': //White knight
            case 'n': //Black knight
                evalScore = 300;
                break;
            case 'Q': //White queen
            case 'q': //Black queen
                evalScore = 900;
                break;
            case 'K': //White king
            case 'k': //Black king
                evalScore = 10000;
                break;
        }
        return evalScore;
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

    // endregion

    // region setters and getters


    public Board getBestMoveBoard() {
        return this.bestMoveBoard;
    }

    public void setCurrentPlayerAI(boolean isWhite) { // todo call this from Game(?) to tell the AI if it is black or white
        this.isWhite = isWhite;
    }

    // endregion

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

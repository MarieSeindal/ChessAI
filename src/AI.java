import Data.BoardEvaluationData;
import java.io.*;
import java.lang.*;
import java.util.ArrayList;

public class AI {

    private static int maxDepth = 5;
    protected Board currentBoard;
    private boolean currentPlayerWhite = false;

    public AI(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public void runAI() {
    }

    public void setCurrentPlayerAI(boolean isWhite){ // todo call this from Game(?) to tell the AI if it is black or white
        currentPlayerWhite = isWhite;
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


    public void fillChildren(ChessNode parent){

        // get parrent board
        char[][] boardParent = parent.getBoard().getBoardArray();

        ArrayList<int[]> tempListOfMoves = new ArrayList<int[]>();

        int rows=0;
        for (char[] row : boardParent) {
            int column=0;
            for (char piece : row) {
                //Check each piece for each possible move.
                if(currentPlayerWhite && Character.isUpperCase(piece)){ //If it's the current player, and It's that players pieces.
                    //Get possible moves for that piece
                    int[] coords = {rows,column};
                    tempListOfMoves = pieceMoveset(piece, coords, currentBoard, currentPlayerWhite);
                    for (int[] moveFound : tempListOfMoves) {

                        //When a move is found, clone it
                        ChessNode copy = parent.clone(); //make copy

                        //todo Figure out if its a special move. Now assumes that AI cannot make special moves

                        //Create the move
                        Move makeMove = new Move(moveFound, coords, false, piece, currentBoard.getPiece(rows,column));

                        //Then execute the move on the clone
                        copy.getBoard().performMove(makeMove); // Make move

                        //Add copy to list
                        parent.addChildren(copy); //add child to the arraylist
                    }
                    currentPlayerWhite = !currentPlayerWhite;
                }
                else if(!currentPlayerWhite && Character.isLowerCase(piece)){ //black
                    //Get possible moves for that piece
                    int[] coords = {rows,column};
                    tempListOfMoves = pieceMoveset(piece, coords, currentBoard, currentPlayerWhite);
                    for (int[] moveFound : tempListOfMoves) {

                        //When a move is found, clone it
                        ChessNode copy = parent.clone(); //make copy

                        //todo Figure out if its a special move. Now assumes that AI cannot make special moves

                        //Create the move
                        Move makeMove = new Move(moveFound, coords, false, piece, currentBoard.getPiece(rows,column));

                        //Then execute the move on the clone
                        copy.getBoard().performMove(makeMove); // Make move

                        //Add copy to list
                        parent.addChildren(copy); //add child to the arraylist
                    }
                    currentPlayerWhite = !currentPlayerWhite;
                }
                column++;
            }
            rows++;
        }
    }

    // TODO: test this function
    /// boolean white - is the checker white ?
    /// char target - the char value of the spot, that we want to check
    /// return int - 0 is empty, 1 is the same color, -1 is the enemy
    public int checkLocation(boolean white, char target) {
        boolean isTargetWhite = Character.isUpperCase(target);

        if(target == ' ')
            return 0;
        else if (white && isTargetWhite)
            return 1;
        else
            return -1;
    }

    // TODO: test this function
    /// horizontalCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    /// int[] location - values from 0,0 to 7,7
    /// Board currentBoard - the current board
    /// boolean white - is this piece white?
    /// int limit - there is no limit, if the value is 0, you can use it for more than 1, but it will only be 0 or 1 (1 is the king)
    public ArrayList<int[]> horizontalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // left
        while (!end){
            // 00 - end the while loop
            if (location[1] - counter < 0)
            {
                end = true;
            }
            // check next spot
            int checkNextSport = checkLocation(white, currentBoard.board[ location[0] ][ (location[1] - counter) ]);

            // 01 - enemy
            if (checkNextSport == -1){
                int[] _move = new int[]{location[0], (location[1] - counter)};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSport == 1)
            {
                end = true;
            }
            // 03 - empty spot
            else if (checkNextSport == 0)
            {
                int[] _move = new int[]{location[0], (location[1] - counter)};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resetting values
        counter = 1;
        end = false;

        // right
        while(!end){
            // 00 - end the while loop
            if (location[1] + counter > 7)
            {
                end = true;
                break;
            }
            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ location[0] ][ (location[1] + counter) ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{location[0], (location[1] + counter)};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if(checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{location[0], (location[1] + counter)};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// verticalCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    // TODO: test this function
    public ArrayList<int[]>  verticalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // up
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter < 0) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ (location[0] - counter) ][ location[1] ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{(location[0] - counter), location[1]};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{(location[0] - counter), location[1]};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        counter = 1;
        end = false;

        // down
        while(end == false){
            // 00 - end the while loop
            if (location[0] + counter > 7){
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ (location[0] + counter) ][ location[1] ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{(location[0] + counter), location[1] };
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{(location[0] + counter), location[1] };
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// crossCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    // TODO: test this function
    public ArrayList<int[]>  crossCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // up and right (-1,+1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter < 0 || location[1] + counter > 7) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ location[0] - counter ][ location[1] + counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{location[0] - counter, location[1] + counter};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0){
                int[] _move = new int[]{location[0] - counter, location[1] + counter};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // up and left (-1,-1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter < 0 || location[1] - counter < 0) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ location[0] - counter ][ location[1] - counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{location[0] - counter, location[1] - counter};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{location[0] - counter, location[1] - counter};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // down and right (+1, +1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter > 7 || location[1] + counter > 7) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ location[0] + counter ][ location[1] + counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{location[0] + counter, location[1] + counter};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{location[0] + counter, location[1] + counter};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // down and left (+1, -1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter > 7 || location[1] - counter < 0) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = checkLocation(white, currentBoard.board[ location[0] + counter ][ location[1] - counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                int[] _move = new int[]{location[0] + counter, location[1] - counter};
                listOfMoves.add(_move);
                end = true;
            }
            // 02 - same color
            else if (checkNextSpot == 1)
            {
                end = true;
            }

            // 03 - empty spot
            else if (checkNextSpot == 0)
            {
                int[] _move = new int[]{location[0] + counter, location[1] - counter};
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            else if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        return listOfMoves;
    }

    // gets the list of moves, that one piece can make (here we check the other pieces on the board too)
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    public ArrayList<int[]> pieceMoveset(char piece, int[] location, Board currentBoard, boolean white){

        // this will be the 2D array, that which ever piece we have, will need
        // int[] location2d = convertIndexTo2D(1);

        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean areWeWhite = Character.isUpperCase(piece);

        int checkSpot = 404;

        switch (piece){
            case 'p':
                // region black p case

                // black is in the top, so 1 down
                // (y,x)

                // 01 - add one spot forward
                checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                if(checkSpot == 0 && location[0] + 1 <= 7)
                    listOfMoves.add(new int[]{location[0] +1, location[1]});

                // 02 - add 2 spot forward, if this is the first move for that piece
                if(location[0] == 1)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1]));
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0] +2, location[1]});
                }

                // 03 - check left kills (+1, -1)
                if(location[0]+1 <= 7 && location[1]-1 >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]-1));
                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0] +1, location[1]-1});
                }

                // 04 - check right kills (+1, +1)
                if(location[0]+1 <= 7 && location[1]-1 >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]+1));
                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0] +1, location[1]-1});
                }
                break;
            // endregion
            case 'P':
                // region white p case

                // white is in the bottom, so 1 up

                // 01 - add one spot forward
                checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                if(checkSpot == 0 && location[0] - 1 >= 0)
                    listOfMoves.add(new int[]{location[0] +1, location[1]});

                // 02 - add 2 spot forward, if this is the first move for that piece
                if(location[0] == 6)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1]));
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0] +2, location[1]});
                }

                // 03 - check left kills (-1, -1)
                if(location[0]-1 >= 0 && location[1]-1 >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]-1));
                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0] -1, location[1]-1});
                }

                // 04 - check right kills (-1, +1)
                if(location[0]-1 >= 0 && location[1]+1 <= 7)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]+1));
                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0] -1, location[1]+1});
                }
                break;
            // endregion
            case 'k':
            case 'K':
                // region white and black k case

                // here we are setting the limit to 1
                listOfMoves.addAll(horizontalCheck(location, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(verticalCheck(location, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(crossCheck(location, currentBoard, areWeWhite, 1));
                break;
            // endregion
            case 'q':
            case 'Q':
                // region black and white q case

                // Straight
                listOfMoves.addAll(horizontalCheck(location, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(verticalCheck(location, currentBoard, areWeWhite, 0));

                // Diagonal
                listOfMoves.addAll(crossCheck(location, currentBoard, areWeWhite, 0));

                System.out.println("Queen moves calculated");
                break;
            // endregion
            case 'r':
            case 'R':
                listOfMoves.addAll(horizontalCheck(location, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(verticalCheck(location, currentBoard, areWeWhite, 0));
                break;
            case 'b':
            case 'B':
                listOfMoves.addAll(crossCheck(location, currentBoard, areWeWhite, 0));
                System.out.println("Bishop moves calculated");
                break;
            case 'n':
            case 'N':
                // region black and white n case
                // there could be 8 moves that it can take (use * do check if it can be done)
                // (y,x)
                // (0,0)
                //       (7,7)

                // -,-,-,-,-,-,-,-
                // -,-,*,8,*,1,*,-
                // -,-,7,-,-,-,2,-
                // -,-,*,-,N,-,*,-
                // -,-,6,-,-,-,3,-
                // -,-,*,5,*,4,*,-
                // -,-,-,-,-,-,-,-
                // -,-,-,-,-,-,-,-

                // ------- right side --------

                // (y,x) 1 up and 2 right
                if( (location[0]-1) >= 0 && (location[1]+2) <= 7){
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                }

                // (y,x)  2 up and 1 right
                if((location[0]-2) >= 0 && (location[1]+1) <= 7)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-2, location[1]+1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-2, location[1]+1});
                }

                // (y,x) 1 down and 2 right
                if( (location[0]+1) <= 7 && (location[1]+2) <= 7)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                }
                // (y,x)  2 down and 1 right
                if((location[0]+2) <= 7 && (location[1]+1) <= 7)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+2, location[1]+1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+2, location[1]+1});
                }

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location[0]-1) >= 0 && (location[1]-2) >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                }

                // (y,x)  2 up and 1 left
                if((location[0]-2) >= 0 && (location[1]-1) >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] - 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-2, location[1]-1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-2, location[1]-1});
                }

                // (y,x) 1 down and 2 left
                if( (location[0]+1) <= 7 && (location[1]-2) >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                }

                // (y,x)  2 down and 1 left
                if((location[0]+2) <= 7 && (location[1]-1) >= 0)
                {
                    checkSpot = checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] - 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+2, location[1]-1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+2, location[1]-1});
                }
                break;
            // endregion
        }
        return listOfMoves;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.List;

public class Game {

    Board board; //The game board
    ArrayList<Board> usedBoards = new ArrayList<Board>(); //List of use boards, Do not have more than 3 duplicates
    Player p1;
    Player p2;
    boolean turn; // true for white?
    int turnsSinceKill = 0;
    Player player;

    public Game(Board startingBoard, Player p1, Player p2, boolean turn) {

        this.board = startingBoard;
        this.p1 = p1;
        this.p2 = p2;
        this.turn = turn;
    }

    public void initializeGame() {
        // Setup settings for the game before starting the game
    }

    public void play() {
        //Play
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

    // TODO: this function need to be tested
    public boolean threefoldRepetition() {
        List<String> checkBoards = new ArrayList<>();
        //Board[] checkedBoards;
        int counter = 0;

        for (Board usedBoard : usedBoards) {
            String boardString = usedBoard.getString();
            if (checkBoards.contains(boardString))
                counter++;
            else
                checkBoards.add(boardString);
        }

        // return true if the counter is 3 or bigger, else it returns false
        return counter >= 3;
    }

    public boolean isMoveLegal() {
        return false;
    }

    // - - - - - Getters and setters - - - - - //

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

//    public Board[] getUsedBoards() {
//        return usedBoards;
//    }
//    public void setUsedBoards(Board[] usedBoards) {
//        this.usedBoards = usedBoards;
//    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public boolean isTurn() {
        return turn;
    }

    public Player getPlayerTurn(boolean turn) {

        if (turn) {
            return p1;
        }
        return p2;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getTurnsSinceKill() {
        return turnsSinceKill;
    }

    public void setTurnsSinceKill(int turnsSinceKill) {
        this.turnsSinceKill = turnsSinceKill;
    }

}

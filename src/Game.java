import java.util.ArrayList;
import java.util.List;

import java.util.LinkedHashSet;
import java.util.HashSet;


public class Game {

    Board board; //The game board
    ArrayList<Board> usedBoards = new ArrayList<>(); //List of use boards, Do not have more than 3 duplicates
    Player p1;
    Player p2;
    boolean turn; // true for white?
    int turnsSinceKill = 0;
    int totalTurns = 0;
    ArrayList<String> castling = new ArrayList<>();
    String enPassantTarget;

    public Game() {
        this.board = new Board();
        usedBoards.add(board);
        this.p1 = new Player();
        this.p2 = new Player();
        this.turn = false;
    }

    public Game(Board startingBoard, Player p1, Player p2, boolean turn) {
        this.board = startingBoard;
        usedBoards.add(board);
        this.p1 = p1;
        this.p2 = p2;
        this.turn = turn;
    }

    // region logic

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

    public static boolean arrayListContains (ArrayList<int[]> theList, int[] checkContent) {
        ArrayList<String> _theList = new ArrayList<>();
        String _checkContent;

        for (int[] x: theList) {
            _theList.add(x[0]  + "," + x[1]);
        }
        _checkContent = checkContent[0] + "," + checkContent[1];

        if(_theList.contains(_checkContent))
            return true;
        else
            return false;
    }

    public static boolean arrayListContainsAll (ArrayList<int[]> theList, ArrayList<int[]> checkContent) {
        ArrayList<String> _theList = new ArrayList<>();
        ArrayList<String> _checkContent = new ArrayList<>();

        for (int[] x: theList) {
            _theList.add(x[0]  + "," + x[1]);
        }
        for (int[] x: checkContent) {
            _checkContent.add(x[0]  + "," + x[1]);
        }

        if(_theList.containsAll(_checkContent))
            return true;
        else
            return false;
    }

    // int return - 0 is nothing, 1 is "check", 2 is "check mate", 3 is "remis"
    // TODO: test this function
    public static int checkTheKing(Board currentBoard, boolean whiteTurnNow){
        ArrayList<int[]> listOfPieces = new ArrayList<int[]>();
        int[] blackKingLocation = new int []{404, 404};
        int[] whiteKingLocation = new int []{404, 404};

        int output = 808;

        // 001 - get a list of the other team's pieces
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                // 002 - set the locations of the kings, when we see them

                // set the black king location, if we see it
                if( currentBoard.getPiece(y,x) == 'k' )
                {
                    blackKingLocation[0] = y;
                    blackKingLocation[1] = x;
                }


                // set the white king location, if we see it
                if( currentBoard.getPiece(y,x) == 'K' )
                {
                    whiteKingLocation[0] = y;
                    whiteKingLocation[1] = x;
                }

                // get all locations of the Pieces
                if( checkLocation(whiteTurnNow, currentBoard.getPiece(y,x)) == -1)
                    listOfPieces.add(new int[]{y, x});
            }
        }

        // 003 - check white or black

        ArrayList<int[]> allSpotsTheEnemyCanMoveToo = new ArrayList<int[]>();

        // when it is white's turn, we will be looking for black pieces
        if (whiteTurnNow) {

            // 004 - get all the spots the enemy can go to
            for (int[] x : listOfPieces) {
                // TODO: this .addAll could be a problem, if the piece have 0 moves in the list. This need to be tested
                allSpotsTheEnemyCanMoveToo.addAll( pieceMoveset( currentBoard.getPiece(x[0], x[1]), x, currentBoard, false) ) ;
            }

            // remove duplicate elements - https://howtodoinjava.com/java/collections/arraylist/remove-duplicate-elements/
            LinkedHashSet<int[]> hashSet = new LinkedHashSet<>(allSpotsTheEnemyCanMoveToo);
            allSpotsTheEnemyCanMoveToo.clear();
            allSpotsTheEnemyCanMoveToo.addAll(new ArrayList<>(hashSet));

            // 005 - get our white king's moveset
            ArrayList<int[]> kingMoveset = new ArrayList<int[]>();
            kingMoveset.addAll(pieceMoveset('K', whiteKingLocation, currentBoard, true));

            // 006 - check if king is in "check", "check mate" or "remis"

//            LinkedHashSet hashSetKing = new LinkedHashSet(kingMoveset);
//            LinkedHashSet hashSetEnemyMoves = new LinkedHashSet(allSpotsTheEnemyCanMoveToo);

            boolean containsAllOfKingMoveset = Game.arrayListContainsAll(allSpotsTheEnemyCanMoveToo, kingMoveset);
            boolean containsKingLocation = Game.arrayListContains(allSpotsTheEnemyCanMoveToo, whiteKingLocation);

            // here we need to check the other team, before the next turn
            // so based on white's moveset, can the black king move to any place at all next turn, that will not set it in check/check mate?
            if(containsAllOfKingMoveset && (!containsKingLocation && kingMoveset.size() > 0) ) // "remis"
            {
                output = 3;
            }
            // TODO: this needs to be checked, since I don't believe this will do it for all cases
            else if(containsAllOfKingMoveset && (containsKingLocation && kingMoveset.size() > 0) ) // "check mate"
                output = 2;
            else if(containsKingLocation) // "check"
                output = 1;
            else
                output = 0;
        }
        else // when it is black's turn, we will be looking for white pieces
        {
            // 004 - get all the spots the enemy can go to
            for (int[] x : listOfPieces) {
                // TODO: this .addAll could be a problem, if the piece have 0 moves in the list. This need to be tested
                allSpotsTheEnemyCanMoveToo.addAll( pieceMoveset( currentBoard.getPiece(x[0], x[1]), x, currentBoard, true) ) ;
            }

            // remove duplicate elements - https://howtodoinjava.com/java/collections/arraylist/remove-duplicate-elements/
            LinkedHashSet<int[]> hashSet = new LinkedHashSet<>(allSpotsTheEnemyCanMoveToo);
            allSpotsTheEnemyCanMoveToo.clear();
            allSpotsTheEnemyCanMoveToo.addAll(new ArrayList<>(hashSet));

            // 005 - get our black king's moveset
            ArrayList<int[]> kingMoveset = new ArrayList<int[]>();
            kingMoveset.addAll(pieceMoveset('k', blackKingLocation, currentBoard, false));

            // 006 - check if king is in "check", "check mate" or "remis"

//            LinkedHashSet hashSetKing = new LinkedHashSet(kingMoveset);
//            LinkedHashSet hashSetEnemyMoves = new LinkedHashSet(allSpotsTheEnemyCanMoveToo);

            boolean containsAllOfKingMoveset = Game.arrayListContainsAll(allSpotsTheEnemyCanMoveToo, kingMoveset);
            boolean containsKingLocation = Game.arrayListContains(allSpotsTheEnemyCanMoveToo, blackKingLocation);

            // here we need to check the other team, before the next turn
            // so based on black's moveset, can the black king move to any place at all next turn, that will not set it in check/check mate?
            //       does every move turn into check mate new turn?                       is it not in check mate or check right now ?
            if(containsAllOfKingMoveset && (!containsKingLocation && kingMoveset.size() > 0) ) // "remis"
            {
                output = 3;
            }
            // TODO: this needs to be checked, since I don't believe this will do it for all cases
            else if(containsAllOfKingMoveset && (containsKingLocation && kingMoveset.size() > 0) ) // "check mate"
                output = 2;
            else if(containsKingLocation) // "check"
                output = 1;
            else
                output = 0;
        }

        return output;
    }

    /// boolean isYourPieceWhite - is the checker white ?
    /// char target - the char value of the spot, that we want to check
    /// return int - 0 is empty, 1 is the same color, -1 is the enemy
    public static int checkLocation(boolean isYourPieceWhite, char target) {
        boolean isTargetWhite = Character.isUpperCase(target);

        if(target == ' ')
            return 0;
        else if (isYourPieceWhite && isTargetWhite || (!isYourPieceWhite && !isTargetWhite) ) // the piece and the target have the same color
            return 1;
        else
            return -1;
    }

    // endregion

    // region all move functions that return or uses int[] values

    // region returns int[]

    /// horizontalCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    /// int[] location - values from 0,0 to 7,7
    /// Board currentBoard - the current board
    /// boolean white - is this piece white?
    /// int limit - there is no limit, if the value is 0, you can use it for more than 1, but it will only be 0 or 1 (1 is the king)
    public static ArrayList<int[]> horizontalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // left
        while (!end){
            // 00 - end the while loop
            if (location[1] - counter <= -1)
            {
                end = true;
                break;
            }
            // check next spot
            int checkNextSport = Game.checkLocation(white, currentBoard.boardArray[ location[0] ][ (location[1] - counter) ]);

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
            if (counter >= limit && limit != 0)
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
            if (location[1] + counter >= 8)
            {
                end = true;
                break;
            }
            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] ][ (location[1] + counter) ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// verticalCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    public static ArrayList<int[]>  verticalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // up
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ (location[0] - counter) ][ location[1] ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down
        while(end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8){
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ (location[0] + counter) ][ location[1] ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// crossCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    public static ArrayList<int[]>  crossCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // up and right (-1,+1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1 || location[1] + counter >= 8) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] - counter ][ location[1] + counter ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // up and left (-1,-1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1 || location[1] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] - counter ][ location[1] - counter ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down and right (+1, +1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8 || location[1] + counter >= 8) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] + counter ][ location[1] + counter ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down and left (+1, -1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8 || location[1] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] + counter ][ location[1] - counter ]);

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
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        return listOfMoves;
    }

    // endregion


    // gets the list of moves, that one piece can make (here we check the other pieces on the board too)
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    public static ArrayList<int[]> pieceMoveset(char piece, int[] location, Board currentBoard, boolean white){

        // this will be the 2D array, that which ever piece we have, will need
        // int[] location2d = convertIndexTo2D(1);

        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean areWeWhite = Character.isUpperCase(piece);

        int checkSpot = 404;

        switch (piece){
            case 'p':
                if(location[0] != 7)
                {

                    // region black p case

                    // black is in the top, so 1 down
                    // (y,x)

                    // 00 - check if we go off the board
                    if(location[0] + 1 >= 8)
                        checkSpot = 1;
                    else {
                        // System.out.println("black - pawn location y: " + location[0] + 1 + " | x : " + location[1]);
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                    }


                    // 01 - add one spot forward
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                    if(checkSpot == 0 && location[0] + 1 <= 7)
                        listOfMoves.add(new int[]{location[0] +1, location[1]});

                    // 02 - add 2 spot forward, if this is the first move for that piece
                    if(location[0] == 1)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1]));
                        if(checkSpot == 0)
                            listOfMoves.add(new int[]{location[0] +2, location[1]});
                    }

                    // 03 - check left kills (+1, -1)
                    if(location[0]+1 <= 7 && location[1]-1 >= 0)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]-1));
                        if(checkSpot == -1)
                            listOfMoves.add(new int[]{location[0] +1, location[1]-1});
                    }

                    // 04 - check right kills (+1, +1)
                    if(location[0]+1 <= 7 && location[1]+1 <= 7)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]+1));
                        if(checkSpot == -1)
                            listOfMoves.add(new int[]{location[0] +1, location[1]+1});
                    }
                }

                break;
            // endregion
            case 'P':
                // region white p case

                if(location[0] != 0)
                {

                    // white is in the bottom, so 1 up

                    // 00 - check if we go off the board
                    if(location[0] - 1 <= -1)
                        checkSpot = 1;
                    else {
                        // System.out.println("white - pawn location y: " + location[0] + 1 + " | x : " + location[1]);
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1]));
                    }


                    // 01 - add one spot forward
                    if(checkSpot == 0 && location[0] - 1 >= 0)
                        listOfMoves.add(new int[]{location[0] -1, location[1]});

                    // 02 - add 2 spot forward, if this is the first move for that piece
                    if(location[0] == 6)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1]));
                        if(checkSpot == 0)
                            listOfMoves.add(new int[]{location[0] -2, location[1]});
                    }

                    // 03 - check left kills (-1, -1)
                    if(location[0]-1 >= 0 && location[1]-1 >= 0)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]-1));
                        if(checkSpot == -1)
                            listOfMoves.add(new int[]{location[0] -1, location[1]-1});
                    }

                    // 04 - check right kills (-1, +1)
                    if(location[0]-1 >= 0 && location[1]+1 <= 7)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]+1));
                        if(checkSpot == -1)
                            listOfMoves.add(new int[]{location[0] -1, location[1]+1});
                    }
                }
                break;
            // endregion
            case 'k':
            case 'K':
                // region white and black k case

                // here we are setting the limit to 1
                listOfMoves.addAll(Game.horizontalCheck(location, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(Game.verticalCheck(location, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(Game.crossCheck(location, currentBoard, areWeWhite, 1));
                break;
            // endregion
            case 'q':
            case 'Q':
                // region black and white q case

                // Straight
                listOfMoves.addAll(Game.horizontalCheck(location, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(Game.verticalCheck(location, currentBoard, areWeWhite, 0));

                // Diagonal
                listOfMoves.addAll(Game.crossCheck(location, currentBoard, areWeWhite, 0));
                break;
            // endregion
            case 'r':
            case 'R':
                listOfMoves.addAll(Game.horizontalCheck(location, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(Game.verticalCheck(location, currentBoard, areWeWhite, 0));
                break;
            case 'b':
            case 'B':
                listOfMoves.addAll(Game.crossCheck(location, currentBoard, areWeWhite, 0));
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
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                }

                // (y,x)  2 up and 1 right
                if((location[0]-2) >= 0 && (location[1]+1) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-2, location[1]+1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-2, location[1]+1});
                }

                // (y,x) 1 down and 2 right
                if( (location[0]+1) <= 7 && (location[1]+2) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                }
                // (y,x)  2 down and 1 right
                if((location[0]+2) <= 7 && (location[1]+1) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+2, location[1]+1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+2, location[1]+1});
                }

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location[0]-1) >= 0 && (location[1]-2) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                }

                // (y,x)  2 up and 1 left
                if((location[0]-2) >= 0 && (location[1]-1) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] - 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]-2, location[1]-1});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]-2, location[1]-1});
                }

                // (y,x) 1 down and 2 left
                if( (location[0]+1) <= 7 && (location[1]-2) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                    if(checkSpot == 0)
                        listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                }

                // (y,x)  2 down and 1 left
                if((location[0]+2) <= 7 && (location[1]-1) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] - 1));

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

    // return - only the moves that give kills
    // currentBoard - a board obj, where we will check for kills
    // moveset - all the moves that can be made
    // white - are the player that is checking for kills white ?
    public static ArrayList<int[]> killsFromMoveset(ArrayList<int[]> moveset, Board currentBoard, boolean white)
    {
        ArrayList<int[]> listOfKills = new ArrayList<int[]>();

        for (int[] x: moveset) {
            if(Game.checkLocation(white, currentBoard.boardArray[x[0]][x[1]]) == -1)
                listOfKills.add(x);
        }

        return listOfKills;
    }

    // endregion

    // region all move functions that return or uses 'Move' values

    // region returns Move

    /// horizontalCheck will return a arrayList of Moves, that the checker can move to (kills will be add to the list too)
    /// int[] location - values from 0,0 to 7,7
    /// Board currentBoard - the current board
    /// boolean white - is this piece white?
    /// int limit - there is no limit, if the value is 0, you can use it for more than 1, but it will only be 0 or 1 (1 is the king)
    public static ArrayList<Move> horizontalMoveCheck(int[] location, Board currentBoard, char piece, boolean white, int limit)
    {
        ArrayList<Move> listOfMoves = new ArrayList<Move>();

        boolean end = false;
        int counter = 1;

        // left
        while (!end){
            // 00 - end the while loop
            if (location[1] - counter <= -1)
            {
                end = true;
                break;
            }
            // check next spot
            int checkNextSport = Game.checkLocation(white, currentBoard.boardArray[ location[0] ][ (location[1] - counter) ]);

            // 01 - enemy
            if (checkNextSport == -1){
                Move _move = new Move(new int[]{location[0], (location[1] - counter)}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0], (location[1] - counter)}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
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
            if (location[1] + counter >= 8)
            {
                end = true;
                break;
            }
            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] ][ (location[1] + counter) ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{location[0], (location[1] + counter)}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0], (location[1] + counter)}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// verticalCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    public static ArrayList<Move> verticalMoveCheck(int[] location, Board currentBoard, char piece, boolean white, int limit)
    {
        ArrayList<Move> listOfMoves = new ArrayList<Move> ();

        boolean end = false;
        int counter = 1;

        // up
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ (location[0] - counter) ][ location[1] ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{(location[0] - counter), location[1]}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{(location[0] - counter), location[1]}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down
        while(end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8){
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ (location[0] + counter) ][ location[1] ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{(location[0] + counter), location[1] }, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{(location[0] + counter), location[1] }, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }


        return listOfMoves;
    }

    /// crossCheck will return a arrayList of locations, that the checker can move to (kills will be add to the list too)
    public static ArrayList<Move>  crossMoveCheck(int[] location, Board currentBoard, char piece, boolean white, int limit)
    {
        ArrayList<Move> listOfMoves = new ArrayList<Move>();

        boolean end = false;
        int counter = 1;

        // up and right (-1,+1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1 || location[1] + counter >= 8) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] - counter ][ location[1] + counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{location[0] - counter, location[1] + counter}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0] - counter, location[1] + counter}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // up and left (-1,-1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] - counter <= -1 || location[1] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] - counter ][ location[1] - counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{location[0] - counter, location[1] - counter}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0] - counter, location[1] - counter}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down and right (+1, +1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8 || location[1] + counter >= 8) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] + counter ][ location[1] + counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{location[0] + counter, location[1] + counter}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0] + counter, location[1] + counter}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        // resets
        end = false;
        counter = 1;

        // down and left (+1, -1)
        while (end == false){
            // 00 - end the while loop
            if (location[0] + counter >= 8 || location[1] - counter <= -1) {
                end = true;
                break;
            }

            // check next spot
            int checkNextSpot = Game.checkLocation(white, currentBoard.boardArray[ location[0] + counter ][ location[1] - counter ]);

            // 01 - enemy
            if (checkNextSpot == -1){
                Move _move = new Move(new int[]{location[0] + counter, location[1] - counter}, location, false, piece, 'k');
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
                Move _move = new Move(new int[]{location[0] + counter, location[1] - counter}, location, false, piece, 'm');
                listOfMoves.add(_move);
            }

            // 04 - limit for the counter
            if (counter >= limit && limit != 0)
            {
                end = true;
            }

            counter++;
        }

        return listOfMoves;
    }

    // endregion

    // gets the list of moves, that one piece can make (here we check the other pieces on the board too)
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    public static ArrayList<Move> allMovesFromPiece(char piece, int[] location, Board currentBoard, boolean white){


        ArrayList<Move>  listOfMoves = new ArrayList<Move> ();

        boolean areWeWhite = Character.isUpperCase(piece);

        int checkSpot = 404;

        switch (piece){
            case 'p':
                // region black p case
                if(location[0] != 7)
                {



                    // black is in the top, so 1 down
                    // (y,x)

                    // 00 - check if we go off the board
                    if(location[0] + 1 >= 8)
                        checkSpot = 1;
                    else {
                        // System.out.println("black - pawn location y: " + location[0] + 1 + " | x : " + location[1]);
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                    }


                    // 01 - add one spot forward
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1]));
                    if(checkSpot == 0 && location[0] + 1 <= 7)
                        listOfMoves.add(new Move(new int[]{location[0] +1, location[1]}, location, false, piece, 'm'));

                    // 02 - add 2 spot forward, if this is the first move for that piece
                    if(location[0] == 1)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1]));
                        if(checkSpot == 0)
                            listOfMoves.add(new Move(new int[]{location[0] +2, location[1]}, location, true, piece, 'm'));
                    }

                    // 03 - check left kills (+1, -1)
                    if(location[0]+1 <= 7 && location[1]-1 >= 0)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]-1));
                        if(checkSpot == -1)
                            listOfMoves.add(new Move(new int[]{location[0] +1, location[1]-1}, location, false, piece, 'k'));
                    }

                    // 04 - check right kills (+1, +1)
                    if(location[0]+1 <= 7 && location[1]+1 <= 7)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]+1, location[1]+1));
                        if(checkSpot == -1)
                            listOfMoves.add(new Move(new int[]{location[0] +1, location[1]+1}, location, false, piece, 'k'));
                    }

                }
                // endregion
                break;

            case 'P':
                // region white p case

                if(location[0] != 0)
                {

                    // white is in the bottom, so 1 up

                    // 00 - check if we go off the board
                    if(location[0] - 1 <= -1)
                        checkSpot = 1;
                    else {
                        // System.out.println("white - pawn location y: " + location[0] + 1 + " | x : " + location[1]);
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1]));
                    }


                    // 01 - add one spot forward
                    if(checkSpot == 0 && location[0] - 1 >= 0)
                        listOfMoves.add(new Move(new int[]{location[0] -1, location[1]}, location, false, piece, 'm'));

                    // 02 - add 2 spot forward, if this is the first move for that piece
                    if(location[0] == 6)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1]));
                        if(checkSpot == 0)
                            listOfMoves.add(new Move(new int[]{location[0] -2, location[1]}, location, true, piece, 'm'));
                    }

                    // 03 - check left kills (-1, -1)
                    if(location[0]-1 >= 0 && location[1]-1 >= 0)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]-1));
                        if(checkSpot == -1)
                            listOfMoves.add(new Move(new int[]{location[0] -1, location[1]-1}, location, false, piece, 'k'));
                    }

                    // 04 - check right kills (-1, +1)
                    if(location[0]-1 >= 0 && location[1]+1 <= 7)
                    {
                        checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0]-1, location[1]+1));
                        if(checkSpot == -1)
                            listOfMoves.add(new Move(new int[]{location[0] -1, location[1]+1}, location, false, piece, 'k'));
                    }
                }
                break;
            // endregion
            case 'k':
            case 'K':
                // region white and black k case

                // here we are setting the limit to 1
                listOfMoves.addAll(Game.horizontalMoveCheck(location, currentBoard, piece, areWeWhite, 1));
                listOfMoves.addAll(Game.verticalMoveCheck(location, currentBoard, piece, areWeWhite, 1));
                listOfMoves.addAll(Game.crossMoveCheck(location, currentBoard, piece, areWeWhite, 1));
                break;
            // endregion
            case 'q':
            case 'Q':
                // region black and white q case

                // Straight
                listOfMoves.addAll(Game.horizontalMoveCheck(location, currentBoard, piece, areWeWhite, 0));
                listOfMoves.addAll(Game.verticalMoveCheck(location, currentBoard, piece, areWeWhite, 0));

                // Diagonal
                listOfMoves.addAll(Game.crossMoveCheck(location, currentBoard, piece, areWeWhite, 0));
                break;
            // endregion
            case 'r':
            case 'R':
                listOfMoves.addAll(Game.horizontalMoveCheck(location, currentBoard, piece, areWeWhite, 0));
                listOfMoves.addAll(Game.verticalMoveCheck(location, currentBoard, piece, areWeWhite, 0));
                break;
            case 'b':
            case 'B':
                listOfMoves.addAll(Game.crossMoveCheck(location, currentBoard, piece, areWeWhite, 0));
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
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]-1, location[1]+2}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]-1, location[1]+2}, location, false, piece, 'm'));
                }

                // (y,x)  2 up and 1 right
                if((location[0]-2) >= 0 && (location[1]+1) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]-2, location[1]+1}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]-2, location[1]+1}, location, false, piece, 'm'));

                }

                // (y,x) 1 down and 2 right
                if( (location[0]+1) <= 7 && (location[1]+2) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] + 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]+1, location[1]+2}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]+1, location[1]+2}, location, false, piece, 'm'));
                }
                // (y,x)  2 down and 1 right
                if((location[0]+2) <= 7 && (location[1]+1) <= 7)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] + 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]+2, location[1]+1}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]+2, location[1]+1}, location, false, piece, 'm'));
                }

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location[0]-1) >= 0 && (location[1]-2) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]-1, location[1]-2}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]-1, location[1]-2}, location, false, piece, 'm'));
                }

                // (y,x)  2 up and 1 left
                if((location[0]-2) >= 0 && (location[1]-1) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] - 2, location[1] - 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]-2, location[1]-1}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]-2, location[1]-1}, location, false, piece, 'm'));
                }

                // (y,x) 1 down and 2 left
                if( (location[0]+1) <= 7 && (location[1]-2) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 1, location[1] - 2));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]+1, location[1]-2}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]+1, location[1]-2}, location, false, piece, 'm'));
                }

                // (y,x)  2 down and 1 left
                if((location[0]+2) <= 7 && (location[1]-1) >= 0)
                {
                    checkSpot = Game.checkLocation(white, currentBoard.getPiece(location[0] + 2, location[1] - 1));

                    if(checkSpot == -1)
                        listOfMoves.add(new Move(new int[]{location[0]+2, location[1]-1}, location, false, piece, 'k'));
                    if(checkSpot == 0)
                        listOfMoves.add(new Move(new int[]{location[0]+2, location[1]-1}, location, false, piece, 'm'));
                }
                break;
            // endregion
        }
        return listOfMoves;
    }

    // endregion

    // - - - - - Getters and setters - - - - - //

    // region getters and setters

    public void addUsedBoard(Board newBoard){
        usedBoards.add(newBoard);
    }


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

    public Player getPlayerTurn(boolean whiteTurn) {

        if (whiteTurn) {
             return p1.isWhite()? p1 : p2;
        } else {
            return !p1.isWhite()? p1 : p2;
        }
    }

    public boolean getTurn() {
        return turn;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        this.turn = whiteTurn;
    }

    public int getTurnsSinceKill() {
        return turnsSinceKill;
    }

    public void setTurnsSinceKill(int turnsSinceKill) {
        this.turnsSinceKill = turnsSinceKill;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    public ArrayList<String> getCastling() {
        return castling;
    }

    public void setCastling(ArrayList<String> castling) {
        this.castling = castling;
    }

    public String getEnPassantTarget() {
        return enPassantTarget;
    }

    public void setEnPassantTarget(String enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }

    // endregion
}

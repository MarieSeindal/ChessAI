import java.util.ArrayList;
import java.lang.*;

public class Move {

    int[] newField;
    int[] oldField;
    boolean specialMove;
    char piece; //The piece you move
    char content; //What lies on the destination field

    public Move(int[] newField, int[] oldField, boolean specialMove, char piece, char content){ //What lies on the destination field)
        this.newField = newField;
        this.oldField = oldField;
        this.specialMove = specialMove;
        this.piece = piece; //The piece you move
        this.content = content; //What lies on the destination field

    }

    // check if the value from "pieceGeneralMoveset", can be used
    // or if there is anything in it's way
    // TODO: write this function
    public void pathChecker(){}

    public int[] convertIndexTo2D(int location){
        int[] array = new int[2];

        //Get the x-coordinate
        if (0 <= location && location <= 7)                   array[1] = 0;
        else if (8 <= location && location <= 15)             array[1] = 1;
        else if (16 <= location && location <= 23)            array[1] = 2;
        else if (24 <= location && location <= 31)            array[1] = 3;
        else if (32 <= location && location <= 39)            array[1] = 4;
        else if (40 <= location && location <= 47)            array[1] = 5;
        else if (48 <= location && location <= 55)            array[1] = 6;
        else if (56 <= location && location <= 63)            array[1] = 7;


        switch (location % 8){ //Get the y-coordinate
            case 0:
                array[0] = 0;
                break;
            case 1:
                array[0] = 1;
                break;
            case 2:
                array[0] = 2;
                break;
            case 3:
                array[0] = 3;
                break;
            case 4:
                array[0] = 4;
                break;
            case 5:
                array[0] = 5;
                break;
            case 6:
                array[0] = 6;
                break;
            case 7:
                array[0] = 7;
                break;

        }
        return array;

        //  0, 1, 2, 3, 4, 5, 6, 7
        //  8, 9,10,11,12,13,14,15
        // 16,17,18,19,20,21,22,23
        // 24,25,26,27,28,29,30,31
        // 32,33,34,35,36,37,38,39
        // 40,41,42,43,44,45,46,47
        // 48,49,50,51,52,53,54,55
        // 56,57,58,59,60,61,62,63
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
    /// int[] location - values from 0,0 to 7,7
    /// Board currentBoard - the current board
    /// boolean white - is this piece white?
    /// int limit - there is no limit, if the value is 0, you can use it for more than 1, but it will only be 0 or 1 (1 is the king)
    public ArrayList<int[]> horizontalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // left - case 1 (0,0) - case 2 (0,4)
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

        // right - case 1 (7,7) - case 2 (7, 2)
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

    // TODO: test this function
    public ArrayList<int[]>  verticalCheck(int[] location, Board currentBoard, boolean white, int limit)
    {
        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean end = false;
        int counter = 1;

        // up and right
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

        // down - case 1 (6,6) - case 2 (0, 2)
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

    // gets the list of moves, that one piece can make (here we check if the for the other pieces on the board)
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    public ArrayList<int[]> pieceMoveset(char piece, int[] location, Board currentBoard, boolean white){

        // this will be the 2D array, that which ever piece we have, will need
        // int[] location2d = convertIndexTo2D(1);

        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        boolean areWeWhite = Character.isUpperCase(piece);

        switch (piece){
            // TODO: write this, so it checks for other pieces
            case 'p':
                // black is in the top, so 1 down
                // (y,x)
                if(location[0] + 1 <= 7)
                    listOfMoves.add(new int[]{location[0] +1, location[1]});
                if(location[0] == 0)
                    listOfMoves.add(new int[]{location[0] +2, location[1]});
                break;
            // TODO: write this, so it checks for other pieces
            case 'P':
                // white is in the bottom, so 1 up
                if(location[0] - 1 >= 0)
                    listOfMoves.add(new int[]{location[0] -1, location[1]});
                if(location[0] == 7)
                    listOfMoves.add(new int[]{location[0] -2, location[1]});
                break;
            // TODO: write this, so it checks for other pieces
            case 'k':
            case 'K':
                // are we are setting the limit to 1
                listOfMoves.addAll(horizontalCheck(oldField, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(verticalCheck(oldField, currentBoard, areWeWhite, 1));
                listOfMoves.addAll(crossCheck(oldField, currentBoard, areWeWhite, 1));
                break;
            case 'q':
            case 'Q':

                // Straight
                listOfMoves.addAll(horizontalCheck(oldField, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(verticalCheck(oldField, currentBoard, areWeWhite, 0));

                // Diagonal
                listOfMoves.addAll(crossCheck(oldField, currentBoard, areWeWhite, 0));

                System.out.println("Queen moves calculated");
                break;
            case 'r':
            case 'R':
                listOfMoves.addAll(horizontalCheck(oldField, currentBoard, areWeWhite, 0));
                listOfMoves.addAll(verticalCheck(oldField, currentBoard, areWeWhite, 0));
                break;
            case 'b':
            case 'B':
                listOfMoves.addAll(crossCheck(oldField, currentBoard, areWeWhite, 0));
                System.out.println("Bishop moves calculated");
                break;
            // TODO: write this, so it checks for other pieces
            case 'n':
            case 'N':
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
                if( (location[0]-1) >= 0 && (location[1]+2) <= 7)
                    listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                // (y,x)  2 up and 1 right
                if((location[0]-2) >= 0 && (location[1]+1) <= 7)
                    listOfMoves.add(new int[]{location[0]-2, location[1]+1});

                // (y,x) 1 down and 2 right
                if( (location[0]+1) <= 7 && (location[1]+2) <= 7)
                    listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                // (y,x)  2 down and 1 right
                if((location[0]+2) <= 7 && (location[1]+1) <= 7)
                    listOfMoves.add(new int[]{location[0]+2, location[1]+1});

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location[0]-1) >= 0 && (location[1]-2) >= 0)
                    listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                // (y,x)  2 up and 1 left
                if((location[0]-2) >= 0 && (location[1]-1) >= 0)
                    listOfMoves.add(new int[]{location[0]-2, location[1]-1});

                // (y,x) 1 down and 2 left
                if( (location[0]+1) <= 7 && (location[1]-2) >= 0)
                    listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                // (y,x)  2 down and 1 left
                if((location[0]+2) <= 7 && (location[1]-1) >= 0)
                    listOfMoves.add(new int[]{location[0]+2, location[1]-1});
                break;
        }
        return listOfMoves;
    }

    // gets the list of moves, that one piece can make, like it was the only piece on the board
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    public ArrayList<int[]> pieceGeneralMoveset(char piece, int[] location){

        // this will be the 2D array, that which ever piece we have, will need
        // int[] location2d = convertIndexTo2D(1);

        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        switch (piece){
            case 'p':
                // black is in the top, so 1 down
                // (y,x)
                if(location[0] + 1 <= 7)
                    listOfMoves.add(new int[]{location[0] +1, location[1]});
                if(location[0] == 0)
                    listOfMoves.add(new int[]{location[0] +2, location[1]});
                break;
            case 'P':
                // white is in the bottom, so 1 up
                if(location[0] - 1 >= 0)
                    listOfMoves.add(new int[]{location[0] -1, location[1]});
                if(location[0] == 7)
                    listOfMoves.add(new int[]{location[0] -2, location[1]});
                break;
            case 'k':
            case 'K':

                if (location[0] >= 1 && location[0] <= 6 && location[1] >= 1 && location[1] <= 6){ //Not edge condition
                    listOfMoves.add(new int[]{location[0]-1, location[1]});      //up
                    listOfMoves.add(new int[]{location[0]-1, location[1]-1});    //up left
                    listOfMoves.add(new int[]{location[0], location[1]-1});      //left
                    listOfMoves.add(new int[]{location[0]+1, location[1]-1});    //left down
                    listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                    listOfMoves.add(new int[]{location[0]+1, location[1]+1});    //down right
                    listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                    listOfMoves.add(new int[]{location[0]-1, location[1]+1});    //right up

                }
                else if(location[0] == 0) { // King is at the top
                    if (location[1] == 0){ // Top left corner
                        // add the remaining 3 fields
                        listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                        listOfMoves.add(new int[]{location[0]+1, location[1]+1});    //down right
                        listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                    }
                    else if (location[1] == 7){ //Top right corner
                        // add the remaining 3 fields
                        listOfMoves.add(new int[]{location[0], location[1]-1});      //left
                        listOfMoves.add(new int[]{location[0]+1, location[1]-1});    //left down
                        listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                    }
                    else {
                        // ad the bottom 5 fields
                        listOfMoves.add(new int[]{location[0], location[1]-1});      //left
                        listOfMoves.add(new int[]{location[0]+1, location[1]-1});    //left down
                        listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                        listOfMoves.add(new int[]{location[0]+1, location[1]+1});    //down right
                        listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                    }
                }

                else if(location[0] == 7) { // King is at bottom
                    if (location[1] == 0){ // Bottom left corner
                        // add the remaining 3 fields
                        listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                        listOfMoves.add(new int[]{location[0]-1, location[1]+1});    //right up
                        listOfMoves.add(new int[]{location[0]-1, location[1]});      //up

                    }
                    else if (location[1] == 7){ //Bottom right corner
                        // add the remaining 3 fields
                        listOfMoves.add(new int[]{location[0]-1, location[1]});      //up
                        listOfMoves.add(new int[]{location[0]-1, location[1]-1});    //up left
                        listOfMoves.add(new int[]{location[0], location[1]-1});      //left

                    }
                    else {
                        // add the top 5 fields
                        listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                        listOfMoves.add(new int[]{location[0]-1, location[1]+1});    //right up
                        listOfMoves.add(new int[]{location[0]-1, location[1]});      //up
                        listOfMoves.add(new int[]{location[0]-1, location[1]-1});    //up left
                        listOfMoves.add(new int[]{location[0], location[1]-1});      //left

                    }
                }
                else if(location[1] == 0) { // King is to the left //Corners condition are handled.
                    listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                    listOfMoves.add(new int[]{location[0]+1, location[1]+1});    //down right
                    listOfMoves.add(new int[]{location[0], location[1]+1});      //right
                    listOfMoves.add(new int[]{location[0]-1, location[1]+1});    //right up
                    listOfMoves.add(new int[]{location[0]-1, location[1]});      //up
                }
                else if(location[1] == 7) { // King is to the right //Corners condition are handled.
                    listOfMoves.add(new int[]{location[0]-1, location[1]});      //up
                    listOfMoves.add(new int[]{location[0]-1, location[1]-1});    //up left
                    listOfMoves.add(new int[]{location[0], location[1]-1});      //left
                    listOfMoves.add(new int[]{location[0]+1, location[1]-1});    //left down
                    listOfMoves.add(new int[]{location[0]+1, location[1]});      //down
                }
                System.out.println("King moves calculated");
                break;
            case 'q':
            case 'Q':

                // Straight
                int counter = 0; //Is used to control that we move a field away from the piece.
                for(int i = location[0]; i >= 0; i-- ){ //check straight up from field
                    counter++;
                    listOfMoves.add(new int[]{location[0]-counter, location[1]});
                }
                counter = 0;
                for(int i = location[0]; i <= 7; i++ ){ //check straight down from field
                    counter++;
                    listOfMoves.add(new int[]{location[0]+counter, location[1]});
                }
                counter = 0;
                for(int i = location[1]; i >= 0; i-- ){ //check left from field
                    counter++;
                    listOfMoves.add(new int[]{location[0], location[1]-counter});
                }
                counter = 0;
                for(int i = location[1]; i <= 7; i++ ){ //check right from field
                    counter++;
                    listOfMoves.add(new int[]{location[0], location[1]+counter});
                }

                // Diagonal
                for (int i = 1; i < 8; i++) { //You can go a maximum of 7 fields in one direction.
                    //count top left squares
                    if ((location[0] - i) >= 0 && (location[1] - i) >= 0)
                        listOfMoves.add(new int[]{location[0] - i, location[1] - i});
                    //count bottom left
                    if ((location[0] + i) <= 7 && (location[1] - i) >= 0)
                        listOfMoves.add(new int[]{location[0] + i, location[1] - i});
                    //count top right
                    if ((location[0] - i) >= 0 && (location[1] + i) <= 7)
                        listOfMoves.add(new int[]{location[0] - i, location[1] + i});
                    //count bottom right
                    if ((location[0] + i) <= 7 && (location[1] + i) <= 7)
                        listOfMoves.add(new int[]{location[0] + i, location[1] + i});
                }

                System.out.println("Queen moves calculated");
                break;
            case 'r':
            case 'R':
                // System.out.println("");
                // there will always be 7 moves one way and 7 moves the other
                // -,-,-,-,1,-,-,-
                // -,-,-,-,2,-,-,-
                // -,-,-,-,3,-,-,-
                // 1,2,3,4,R,5,6,7
                // -,-,-,-,4,-,-,-
                // -,-,-,-,5,-,-,-
                // -,-,-,-,6,-,-,-
                // -,-,-,-,7,-,-,-

                // (y,x)
                // (0,0)
                //       (7,7)

                // horizontal
                for (int i = 0; i < 8; i++) {
                    int[] _move = new int[]{location[0], i};
                    if(_move != location)
                        listOfMoves.add(_move);
                }
                // vertical
                for (int i = 0; i < 8; i++) {
                    int[] _move = new int[]{i, location[1]};
                    if(_move != location)
                        listOfMoves.add(_move);
                }
                break;
            case 'b':
            case 'B':
                // -,1,-,-,-,-,-,1
                // -,-,2,-,-,-,2,-
                // -,-,-,3,-,3,-,-
                // -,-,-,-,B,-,-,-
                // -,-,-,4,-,4,-,-
                // -,-,5,-,-,-,5,-
                // -,6,-,-,-,-,-,6
                // 7,-,-,-,-,-,-,-
                for (int i = 1; i < 8; i++) {
                    //count top left squares
                    //one up and one left
                    if ((location[0] - i) >= 0 && (location[1] - i) >= 0)
                        listOfMoves.add(new int[]{location[0] - 1, location[1] - 1});
                    //count bottom left
                    //one down one left
                    if ((location[0] + i) <= 7 && (location[1] - i) >= 0)
                        listOfMoves.add(new int[]{location[0] + 1, location[1] - 1});
                    //count top right
                    //one up one right
                    if ((location[0] - i) >= 0 && (location[1] + i) <= 7)
                        listOfMoves.add(new int[]{location[0] - 1, location[1] + 1});
                    //count bottom right
                    //one down and one right
                    if ((location[0] + i) <= 7 && (location[1] + i) <= 7)
                        listOfMoves.add(new int[]{location[0] + 1, location[1] + 1});
                }
                System.out.println("Bishop moves calculated");
                break;
            case 'n':
            case 'N':
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
                if( (location[0]-1) >= 0 && (location[1]+2) <= 7)
                    listOfMoves.add(new int[]{location[0]-1, location[1]+2});
                // (y,x)  2 up and 1 right
                if((location[0]-2) >= 0 && (location[1]+1) <= 7)
                    listOfMoves.add(new int[]{location[0]-2, location[1]+1});

                // (y,x) 1 down and 2 right
                if( (location[0]+1) <= 7 && (location[1]+2) <= 7)
                    listOfMoves.add(new int[]{location[0]+1, location[1]+2});
                // (y,x)  2 down and 1 right
                if((location[0]+2) <= 7 && (location[1]+1) <= 7)
                    listOfMoves.add(new int[]{location[0]+2, location[1]+1});

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location[0]-1) >= 0 && (location[1]-2) >= 0)
                    listOfMoves.add(new int[]{location[0]-1, location[1]-2});
                // (y,x)  2 up and 1 left
                if((location[0]-2) >= 0 && (location[1]-1) >= 0)
                    listOfMoves.add(new int[]{location[0]-2, location[1]-1});

                // (y,x) 1 down and 2 left
                if( (location[0]+1) <= 7 && (location[1]-2) >= 0)
                    listOfMoves.add(new int[]{location[0]+1, location[1]-2});
                // (y,x)  2 down and 1 left
                if((location[0]+2) <= 7 && (location[1]-1) >= 0)
                    listOfMoves.add(new int[]{location[0]+2, location[1]-1});
                break;
        }
        return listOfMoves;
    }





    // - - - - - Getters and setters - - - - - //

    public int[] getNewField() {
        return newField;
    }
    public void setNewField(int[] newField) {
        this.newField = newField;
    }

    public int[] getOldField() {
        return oldField;
    }
    public void setOldField(int[] oldField) {
        this.oldField = oldField;
    }

    public boolean isSpecialMove() {
        return specialMove;
    }
    public void setSpecialMove(boolean specialMove) {
        this.specialMove = specialMove;
    }

    public char getPiece() {
        return piece;
    }
    public void setPiece(char piece) {
        this.piece = piece;
    }

    public char getContent() {
        return content;
    }
    public void setContent(char content) {
        this.content = content;
    }

}

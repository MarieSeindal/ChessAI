import java.util.ArrayList;

public class Move {

    int newField;
    int oldField;
    boolean specialMove;
    char piece; //The piece you move
    char content; //What lies on the destination field

    public Move(int newField, int oldField, boolean specialMove, char piece, char content){ //What lies on the destination field)
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

    // TODO: write this function
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

    // gets the list of moves, that one piece can make, like it was the only piece on the board
    // the piece can not go off of the board
    // char piece - is a character for the piece, can be lower or upper case, based on what color it is
    // int location - is the index value of the board, it needs to be converted to a 2D char array
    // TODO: write this function
    public ArrayList pieceGeneralMoveset(char piece, int location){

        // this will be the 2D array, that which ever piece we have, will need
        int[] location2d  = convertIndexTo2D(1);

        ArrayList<int[]> listOfMoves = new ArrayList<int[]>();

        switch (piece){
            case 'p':
                System.out.println("");
                break;
            case 'P':
                System.out.println("");
                break;
            case 'k':
            case 'K':
                kingMoves(location, listOfMoves);
                System.out.println("");
                break;
            case 'q':
            case 'Q':
                queenMoves(location, listOfMoves);
                System.out.println("");
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
                    int[] _move = new int[]{location2d[0], i};
                    if(_move != location2d)
                        listOfMoves.add(_move);
                }
                // vertical
                for (int i = 0; i < 8; i++) {
                    int[] _move = new int[]{i, location2d[1]};
                    if(_move != location2d)
                        listOfMoves.add(_move);
                }

                break;
            case 'b':
            case 'B':
                System.out.println("");
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
                if( (location2d[0]-1) >= 0 && (location2d[1]+2) <= 7)
                    listOfMoves.add(new int[]{location2d[0]-1, location2d[1]+2});
                // (y,x)  2 up and 1 right
                if((location2d[0]-2) >= 0 && (location2d[1]+1) <= 7)
                    listOfMoves.add(new int[]{location2d[0]-2, location2d[1]+1});

                // (y,x) 1 down and 2 right
                if( (location2d[0]+1) <= 7 && (location2d[1]+2) <= 7)
                    listOfMoves.add(new int[]{location2d[0]+1, location2d[1]+2});
                // (y,x)  2 down and 1 right
                if((location2d[0]+2) <= 7 && (location2d[1]+1) <= 7)
                    listOfMoves.add(new int[]{location2d[0]+2, location2d[1]+1});

                // ------- left side --------

                // (y,x) 1 up and 2 left
                if( (location2d[0]-1) >= 0 && (location2d[1]-2) >= 0)
                    listOfMoves.add(new int[]{location2d[0]-1, location2d[1]-2});
                // (y,x)  2 up and 1 left
                if((location2d[0]-2) >= 0 && (location2d[1]-1) >= 0)
                    listOfMoves.add(new int[]{location2d[0]-2, location2d[1]-1});

                // (y,x) 1 down and 2 left
                if( (location2d[0]+1) <= 7 && (location2d[1]-2) >= 0)
                    listOfMoves.add(new int[]{location2d[0]+1, location2d[1]-2});
                // (y,x)  2 down and 1 left
                if((location2d[0]+2) <= 7 && (location2d[1]-1) >= 0)
                    listOfMoves.add(new int[]{location2d[0]+2, location2d[1]-1});

                break;
        }

        return listOfMoves;
    }

    public void queenMoves(int location, ArrayList<int[]> listToFill){
        int[] boardLocation = convertIndexTo2D(location);

        for(int i = boardLocation[0]; i >= 0; i-- ){ //check straight up from field
            // add fields to some list
        }
        for(int i = boardLocation[0]; i <= 7; i++ ){ //check straight down from field
            // add fields to some list
        }
        int x = boardLocation[1];
        int y = boardLocation[0];

        //while (edge not hit) // one diagonal
            // check for edge condition
            // increment x and y
            // add field to list


    }

    public void kingMoves(int location, ArrayList<int[]> listToFill){
        int[] bl = convertIndexTo2D(location); //bl = boardLocation

        if (bl[0] >= 1 && bl[0] <= 6 && bl[1] >= 1 && bl[1] <= 6){ //Not edge condition
            listToFill.add(new int[]{bl[0]-1, bl[1]});      //up
            listToFill.add(new int[]{bl[0]-1, bl[1]-1});    //up left
            listToFill.add(new int[]{bl[0], bl[1]-1});      //left
            listToFill.add(new int[]{bl[0]+1, bl[1]-1});    //left down
            listToFill.add(new int[]{bl[0]+1, bl[1]});      //down
            listToFill.add(new int[]{bl[0]+1, bl[1]+1});    //down right
            listToFill.add(new int[]{bl[0], bl[1]+1});      //right
            listToFill.add(new int[]{bl[0]-1, bl[1]+1});    //right up
            listToFill.add(new int[]{bl[0]-1, bl[1]});      //up

        }
        else if(bl[0] == 0) { // King is at the top
            if (bl[1] == 0){ // Top left corner
                // add the remaining 3 fields
                listToFill.add(new int[]{bl[0]+1, bl[1]});      //down
                listToFill.add(new int[]{bl[0]+1, bl[1]+1});    //down right
                listToFill.add(new int[]{bl[0], bl[1]+1});      //right
            }
            else if (bl[1] == 7){ //Top right corner
                // add the remaining 3 fields
                listToFill.add(new int[]{bl[0], bl[1]-1});      //left
                listToFill.add(new int[]{bl[0]+1, bl[1]-1});    //left down
                listToFill.add(new int[]{bl[0]+1, bl[1]});      //down
            }
            else {
                // ad the bottom 5 fields
                listToFill.add(new int[]{bl[0], bl[1]-1});      //left
                listToFill.add(new int[]{bl[0]+1, bl[1]-1});    //left down
                listToFill.add(new int[]{bl[0]+1, bl[1]});      //down
                listToFill.add(new int[]{bl[0]+1, bl[1]+1});    //down right
                listToFill.add(new int[]{bl[0], bl[1]+1});      //right
            }
        } // top

        else if(bl[0] == 7) { // King is at bottom //todo
            if (bl[1] == 0){ // Bottom left corner
                // add the remaining 3 fields
                listToFill.add(new int[]{bl[0], bl[1]+1});      //right
                listToFill.add(new int[]{bl[0]-1, bl[1]+1});    //right up
                listToFill.add(new int[]{bl[0]-1, bl[1]});      //up

            }
            else if (bl[1] == 7){ //Bottom right corner
                // add the remaining 3 fields

            }
            else {
                // add the top 5 fields

            }
        }
        else if(bl[1] == 0) { // King is to the left //Corners condition are handled. todo

        }
        else if(bl[1] == 7) { // King is to the right //Corners condition are handled. todo

        }
    }


    // - - - - - Getters and setters - - - - - //

    public int getNewField() {
        return newField;
    }
    public void setNewField(int newField) {
        this.newField = newField;
    }

    public int getOldField() {
        return oldField;
    }
    public void setOldField(int oldField) {
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

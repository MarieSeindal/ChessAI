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
    public void pieceGeneralMoveset(char piece, int location){

        // this will be the 2D array, that which ever piece we have, will need
        convertIndexTo2D(1);

        switch (piece){
            case 'p':
                System.out.println("");
                break;
            case 'P':
                System.out.println("");
                break;
            case 'k':
            case 'K':
                System.out.println("");
                break;
            case 'q':
            case 'Q':
                System.out.println("");
                break;
            case 'r':
            case 'R':
                System.out.println("");
                break;
            case 'b':
            case 'B':
                System.out.println("");
                break;
            case 'n':
            case 'N':
                System.out.println("");
                break;
        }
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

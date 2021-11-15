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

        int x = location / 8;
        int y = location % 8;

        return new int[]{x,y};

        // 0,1,2,3,4,5,6,7
        // 8,9,10,11,12,13,14,15
        // 16,17,18,19,20,21,22,23
        // 24,25,26,27,28,29,30,31
        // 32,33,34,35,36,37,38,39
        // 40,41,42,43,44,45,46,47
        // 48,49,50,51,52,53,54,55
        // 56,57,58,59,60,61,62,63
    }

    // TODO: write this function
    public int convert2DToIndex(int[] location){

        int x = location[0];
        int y = location[1];

        int num = x*8+y;

        return num;

        // 0,1,2,3,4,5,6,7
        // 8,9,10,11,12,13,14,15
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

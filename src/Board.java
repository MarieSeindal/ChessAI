import java.io.Serializable;

public class Board implements Serializable {

    char[][] board;

    public Board(char[][] board){
        this.board = board;
    }

    public Board(){
        this.board = new char[][] {
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}};
    }

    public void performMove(Move move) {

        board[move.oldField[0]][move.oldField[1]] = ' ';
        board[move.newField[0]][move.newField[1]] = move.piece;

    }

    public boolean isEnemyPiece(boolean isWhite, char c) {

        if (isWhite && Character.isLowerCase(c)) {
            return true;
        } else if (!isWhite && Character.isUpperCase(c)) {
            return true;
        }
        return false;
    }

    public void resetBoard(){

    }

    public char checkStartPosition(boolean isWhite, int x, int y) {
        char piece = board[x][y];
        if (isWhite && Character.isUpperCase(piece)) {
            return piece;
        } else if (!isWhite && Character.isLowerCase(piece)) {
            return piece;
        }
        return ' ';
    }

    public char getPiece(int x, int y) {
        return board[x][y];
    }

    // - - - - - Getters and setters - - - - - //

    public char[][] getBoardArray() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }


    public String getString(){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {
                output.append(board[i][j]);
            }
        }

        return output.toString();
    }
}

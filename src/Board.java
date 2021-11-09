import java.lang.reflect.Array;

public class Board {



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

    public void resetBoard(){

    }

    // - - - - - Getters and setters - - - - - //

    public char[][] getBoard() {
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

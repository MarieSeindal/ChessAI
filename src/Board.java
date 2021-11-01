import java.lang.reflect.Array;

public class Board {



    char[][] board;

    public Board(char[][] board){
        this.board = board;
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

}

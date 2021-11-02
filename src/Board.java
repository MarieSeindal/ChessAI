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

    //    coordinates
    //    [
    //            [a8,b8,c8,d8,e8,f8,g8,h8],
    //            [a7,b7,c7,d7,e7,f7,g7,h7],
    //            [a6,b6,c6,d6,e6,f6,g6,h6],
    //            [a5,b5,c5,d5,e5,f5,g5,h5],
    //            [a4,b4,c4,d4,e4,f4,g4,h4],
    //            [a3,b3,c3,d3,e3,f3,g3,h3],
    //            [a2,b2,c2,d2,e2,f2,g2,h2],
    //            [a1,b1,c1,d1,e1,f1,g1,h1],
    //    ]

    // TODO: this function need to be tested
    public String getString(){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 7; i++) {

            for (int j = 0; j < 7; j++) {
                output.append(board[i][j]);
            }
        }

        return output.toString();
    }
}

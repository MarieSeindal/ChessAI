import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board implements Serializable {

    char[][] boardArray;

    public Board(char[][] boardArray) {
        this.boardArray = boardArray;
    }

    public Board() {
        //        this.boardArray = new char[][] {
        //                {' ',' ',' ',' ','k',' ',' ',' '},
        //                {' ','p',' ',' ',' ',' ',' ',' '},
        //                {' ',' ',' ',' ',' ',' ',' ',' '},
        //                {' ',' ',' ',' ',' ',' ',' ',' '},
        //                {' ',' ',' ',' ',' ',' ',' ',' '},
        //                {' ',' ',' ',' ',' ',' ',' ',' '},
        //                {' ','P',' ',' ',' ',' ',' ',' '},
        //                {' ',' ',' ',' ','K',' ',' ',' '}};

        this.boardArray = new char[][]{
                {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
    }

    public void performMove(Move move) {

        boardArray[move.oldField[0]][move.oldField[1]] = ' ';
        boardArray[move.newField[0]][move.newField[1]] = move.piece;

    }

    public boolean isEnemyPiece(boolean isWhite, char c) {

        if (isWhite && Character.isLowerCase(c)) {
            return true;
        } else if (!isWhite && Character.isUpperCase(c)) {
            return true;
        }
        return false;
    }

    public void resetBoard() {

    }

    public char checkStartPosition(boolean isWhite, int y, int x) {
        char piece = boardArray[y][x];
        if (isWhite && Character.isUpperCase(piece)) {
            return piece;
        } else if (!isWhite && Character.isLowerCase(piece)) {
            return piece;
        }
        return ' ';
    }

    public char getPiece(int y, int x) {
        return boardArray[y][x];
    }

    // - - - - - Getters and setters - - - - - //

    public ArrayList<int[]> getAllPiece() {
        ArrayList<int[]> output = new ArrayList<int[]>();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if(this.boardArray[y][x] != ' ')
                    output.add(new int[] {y,x} );
            }
        }

        return output;
    }

    public char[][] getBoardArray() {
        return boardArray;
    }

    public void setBoardArray(char[][] boardArray) {
        this.boardArray = boardArray;
    }

    public Move moveFromDifferenceIn2Boards(Board newBoard) {
        // Move move = new Move();
        // move.setSpecialMove(false); //todo assumed AI makes no special moves

        int[] oldSpot = new int[2];
        int[] newSpot = new int[2];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                //detect difference
                if (boardArray[y][x] != newBoard.getBoardArray()[y][x]) {
                    //detect "space", aka oldField
                    if (newBoard.getBoardArray()[y][x] == ' ')
                    {
                        oldSpot[0] = y;
                        oldSpot[1] = x;
                    }
                    else // newField
                    {
                        newSpot[0] = y;
                        newSpot[1] = x;
                    }
                }

            }
        }

        return new Move(newSpot, oldSpot, false, newBoard.getPiece(newSpot[0], newSpot[1]), 'o');

    }

    public String getString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {
                output.append(boardArray[i][j]);
            }
        }

        return output.toString();
    }

    public Board cloning() { //todo tjek op på hvilke klasser der skal være serialisable
        //todo husk kilde: http://javatechniques.com/public/java/docs/basics/faster-deep-copy.html
        Board obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = (Board) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

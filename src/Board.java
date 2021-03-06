import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.Arrays.*;

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

        // convert p/P that are at the end to q/Q
        //      (if p AND at the end, then true)         or       (if P AND at the end, then true)
        if( (move.piece == 'p' && move.newField[0] == 7) || (move.piece == 'P' && move.newField[0] == 0) ) // only p/P
        {
            if(move.piece == 'p') // if black
            {
                move.setPiece('q');
            }
            else // if white
            {
                move.setPiece('Q');
            }
        }

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

    static public ArrayList<Integer> hashBoardsAndRemoveDuplicates (ArrayList<Board> usedBoards) {
        // used code from - https://www.geeksforgeeks.org/how-to-remove-duplicates-from-arraylist-in-java/
        // used code from - https://stackoverflow.com/a/44367261/3065595

        ArrayList<Integer> _theHashList = new ArrayList<>();
        ArrayList<Integer> _output = new ArrayList<>();

        // converter
        for (Board board : usedBoards) {
            _theHashList.add(board.getHash());
        }

        for (int item: _theHashList) {

            // if we DON'T have the item in the list, add it
            if(!_output.contains(item))
            {
                _output.add(item);
            }
        }

        return _output;
    }

    public int getHash() {
        return java.util.Arrays.deepHashCode( boardArray );
    }

    public Board cloning() { //todo tjek op p?? hvilke klasser der skal v??re serialisable
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

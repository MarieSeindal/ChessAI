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

    public char checkStartPosition(boolean isWhite, int x, int y) {
        char piece = boardArray[x][y];
        if (isWhite && Character.isUpperCase(piece)) {
            return piece;
        } else if (!isWhite && Character.isLowerCase(piece)) {
            return piece;
        }
        return ' ';
    }

    public char getPiece(int x, int y) {
        return boardArray[x][y];
    }

    // - - - - - Getters and setters - - - - - //

    public char[][] getBoardArray() {
        return boardArray;
    }

    public void setBoardArray(char[][] boardArray) {
        this.boardArray = boardArray;
    }

    public Move moveFromDifferenceIn2Boards(Board newBoard) {
        Move move = new Move();
        move.setSpecialMove(false); //todo assumed AI makes no special moves

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (boardArray[i][j] != newBoard.getBoardArray()[i][j]) { //detect difference
                    if (newBoard.getBoardArray()[i][j] == ' ') { //detect "space", aka oldField
                        int[] oldFiled = {i, j};
                        move.setOldField(oldFiled);
                    } else { // newField
                        int[] newFiled = {i, j};
                        move.setNewField(newFiled);
                        move.setPiece(newBoard.getBoardArray()[i][j]);
                        move.setContent(boardArray[i][j]);
                    }
                }

            }
        }

        return move;

    }

    public HashMap<Character, int[]> getPlayerPieces(boolean isWhite) {

        HashMap<Character, int[]> pieceList = new HashMap<>();

        for (int j = 0; j < boardArray.length-1; j++) {
            for (int k = 0; k < boardArray[j].length-1; k++) {
                if (!(boardArray[j][k] == ' ')) {
                    if (Character.isUpperCase(boardArray[j][k]) && isWhite) { // White piece
                        pieceList.put(Character.valueOf(boardArray[j][k]), new int[]{j, k});
                    } else if (Character.isLowerCase(boardArray[j][k]) && !isWhite) { // Black piece
                        pieceList.put(Character.valueOf(boardArray[j][k]), new int[]{j, k});
                    }
                }
                /*if (Character.isUpperCase(boardArray[i][j])) {
                    whitePieces++;
                } else {
                    blackPieces++;
                }*/

            }
        }

        return pieceList;
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

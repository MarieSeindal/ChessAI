package Helper;

import java.util.ArrayList;

public class Fen {

    private char[][] boardLayout;
    private ArrayList<String> castling;
    private boolean playerTurn;
    private int movesKill;
    private int totalMoves;
    private String enPassantTarget;

    public char[][] getBoardLayout() {
        return boardLayout;
    }

    public void setBoardLayout(char[][] boardLayout) {
        this.boardLayout = boardLayout;
    }

    public ArrayList<String> getCastling() {
        return castling;
    }

    public void setCastling(ArrayList<String> castling) {
        this.castling = castling;
    }

    public boolean getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getTurnsSinceKill() {
        return movesKill;
    }

    public void setMovesKill(int movesKill) {
        this.movesKill = movesKill;
    }

    public int getTotalTurns() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public String getEnPassantTarget() {
        return enPassantTarget;
    }

    public void setEnPassantTarget(String enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }
}

import java.util.ArrayList;
import java.lang.*;

public class Move {

    int[] newField;
    int[] oldField;
    boolean specialMove;
    char piece; //The piece you move
    char content; //What lies on the destination field

//    public Move() {
//        this.newField = new int[]{404,404};
//        this.oldField = new int[]{404,404};
//        this.specialMove = true;
//        this.piece = 'o';
//        this.content = 'o';
//    }

    public Move(int[] newField, int[] oldField, boolean specialMove, char piece, char content){ //What lies on the destination field)
        this.newField = newField;
        this.oldField = oldField;
        this.specialMove = specialMove;
        this.piece = piece; //The piece you move
        this.content = content; //What lies on the destination field

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

import java.io.Serializable;
import java.util.ArrayList;

public class ChessNode implements Serializable {

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public ArrayList<ChessNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ChessNode> children) {
        this.children = children;
    }

    Board board;
    ArrayList<ChessNode> children;

    public ChessNode(){

    }


}

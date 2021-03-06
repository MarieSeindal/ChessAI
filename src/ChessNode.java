import java.io.*;
import java.util.ArrayList;

public class ChessNode implements Serializable {

    Board board;
    ArrayList<Move> moves;
    ArrayList<ChessNode> children;

    public ChessNode(){
        children = new ArrayList<>();
        this.board = new Board();
    }

    public ChessNode(ArrayList<Move> allTheMovse){
        children = new ArrayList<>();
        moves = allTheMovse;
        this.board = null;
    }

    public ChessNode(Board boardInNode){
        children = new ArrayList<>();
        this.board = boardInNode;
    }

    public ChessNode cloning() { //todo tjek op på hvilke klasser der skal være serialisable
        //todo husk kilde: http://javatechniques.com/public/java/docs/basics/faster-deep-copy.html
        ChessNode obj = null;
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
            obj = (ChessNode) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // region getters and setters

    public ArrayList<Move> getMoves() { return moves; }
    public void setMoves(ArrayList<Move> inputMoves) { this.moves = inputMoves; }
    public void addMove(Move oneMove) { this.moves.add(oneMove); }
    public void addMoves(ArrayList<Move> allMoves) { this.moves.addAll(allMoves); }

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

    public void addChildren(ChessNode child) {
        this.children.add(child);
    }

    // endregion

}

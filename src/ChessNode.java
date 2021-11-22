import java.io.*;
import java.util.ArrayList;

public class ChessNode implements Serializable {

    Board board;
    ArrayList<ChessNode> children;

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


    public ChessNode(Board boardInNode){
        children = new ArrayList<>();
        this.board = boardInNode;
    }

    public void addChildren(ChessNode child) {
        this.children.add(child);
    }

    public ChessNode clone() { //todo tjek op på hvilke klasser der skal være serialisable
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



}

import java.io.*;

public class childNode {
    Move move;

    public void setMove(Move inputMove) {
        this.move = inputMove;
    }

    public Move getMove(){
        return this.move;
    }


    public childNode(Move inputMove){
        this.move = inputMove;
    }


    public childNode cloning() { //todo tjek op på hvilke klasser der skal være serialisable
        //todo husk kilde: http://javatechniques.com/public/java/docs/basics/faster-deep-copy.html
        childNode obj = null;
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
            obj = (childNode) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }
}

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.List;

public class Game {

    Board board; //The game board
    ArrayList<Board> usedBoards = new ArrayList<Board>(); //List of use boards, Do not have more than 3 duplicates
    Player p1;
    Player p2;
    boolean turn; // true for white?
    int turnsSinceKill = 0;

    public Game(Board startingBoard, Player p1, Player p2, boolean turn){
        this.board = startingBoard;
        this.p1 = p1;
        this.p2 = p2;
        this.turn = turn;
    }

    public void initializeGame(){
        // Setup settings for the game before starting the game
    }

    public void play(){
        //Play
    }

    // TODO: this function really could be optimized
    public boolean threefoldRepetition ()
    {
        // https://www.geeksforgeeks.org/java-util-collections-frequency-java/

        List<String> checkBoards = new ArrayList<>();

        // convert all of the Boards into strings
        for (Board b: usedBoards) {
            checkBoards.add(b.getString());
        }

        for (String boardString : checkBoards) {

            if (Collections.frequency(checkBoards, boardString) == 3)
                return true;
        }

        // done with looking at each value
        return false;
    }

    public boolean isMoveLegal(){
        return false;
    }

    // - - - - - Getters and setters - - - - - //

    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

//    public Board[] getUsedBoards() {
//        return usedBoards;
//    }
//    public void setUsedBoards(Board[] usedBoards) {
//        this.usedBoards = usedBoards;
//    }

    public Player getP1() {
        return p1;
    }
    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }
    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public boolean isTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public int getTurnsSinceKill() {
        return turnsSinceKill;
    }
    public void setTurnsSinceKill(int turnsSinceKill) {
        this.turnsSinceKill = turnsSinceKill;
    }

}

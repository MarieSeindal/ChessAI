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

        // temp, will be deleted after testing
        char[][] tBoard = {{'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}};

        char[][] tBoard2 = {{'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}};

        char[][] tBoard3 = {{'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}};

        char[][] tBoard4 = {{'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'p','p','p','p','p','p','p','p'},
                {' ',' ',' ',' ',' ',' ',' ',' '},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'}};

        usedBoards.add(new Board(tBoard));
        usedBoards.add(new Board(tBoard2));
        usedBoards.add(new Board(tBoard3));
        usedBoards.add(new Board(tBoard4));

        // end of temp

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

    // TODO: this function need to be tested
    public boolean threefoldRepetition ()
    {
        List<String> checkBoards = new ArrayList<>();
        //Board[] checkedBoards;
        int counter = 0;

        for (Board usedBoard : usedBoards) {
            // make a string version of the board
            String boardString = usedBoard.getString();
            // check if their is a board like this one in the list
            if (checkBoards.contains(boardString))
                // if yes, add to the counter
                counter++;
            else
                // if no, add it to the list
                checkBoards.add(boardString);
        }

        // return true if the counter is 3 or bigger, else it returns false
        return counter >= 3;
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

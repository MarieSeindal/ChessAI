import Interfaces.I_TUI;
import TUI.TUI;

import java.util.Scanner;

public class Main {

    private static I_TUI tui;
    private static Game game;
    private static Board board;
    private static Scanner sc;
    private static boolean turn; //Initializing variables
    private static Player p1, p2;

    public static void main(String[] args) {

        tui = new TUI();
        sc = new Scanner(System.in);
        int gameModeSelection;
        String resumeGame = "";
        char startPiece = ' ';
        char destinationPiece = ' ';
        Player player;
        turn = true;

        // Prompt for resume game

        //resumeGame = tui.showResumeMenu(sc);

        // Prompt for gamemode
        gameModeSelection = tui.showStartMenu(sc);

        switch (gameModeSelection) {
            case 1 -> { // Human vs AI
                p1 = new Player(true, false);
                p2 = new Player(false, true);
            }
            case 2 -> { // Human vs Human
                p1 = new Player(true, false);
                p2 = new Player(false, false);
            }
            case 3 -> { // AI vs AI
                p1 = new Player(true, true);
                p2 = new Player(false, true);
            }
            default -> System.out.println("Error in game mode selection: " + gameModeSelection);
        }

        board = new Board();
        game = new Game(board, p1, p2, true);
        //tui.initBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"); // Using default test FEN
        tui.printBoard(board.getBoardArray());

        /**
         * Start the main game loop
         */

        while (true) {

            // todo check if player is checkmate?
            player = game.getPlayerTurn(turn);

            if (player.isWhite()) {
                System.out.println("White Players turn");
            } else {
                System.out.println("Black Players turn");
            }

            if (!player.isAi) { // Human player's turn

                int[] startPos, destinationPos;

                // Get start position
                while (true) {

                    System.out.println("Please enter the coordinate for the"
                            + " piece you want to move (row(,|.)col): ");

                    startPos = tui.getMovePosition(sc);

                    // Validate piece based on user input
                    try {
                        startPiece = board.checkStartPosition(player.isWhite(), startPos[0], startPos[1]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid piece");
                    }

                    if (startPiece != ' ') {
                        break;
                    } else {
                        System.out.println("Error: Invalid piece selected!");
                    }

                }

                // Get destination position
                while (true) {

                    System.out.print("Destination position (row(,|.)col): ");
                    destinationPos = tui.getMovePosition(sc);
                    // Validate destination based on user input
                    try {
                        destinationPiece = board.getPiece(destinationPos[0], destinationPos[1]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid destination selected!");
                    }

                    if (startPiece != ' ' && destinationPiece == ' ') {
                        Move move = new Move(destinationPos, startPos, false, startPiece, ' ');
                        board.performMove(move);
                        System.out.println("Move complete!");
                        // Next player's turn
                        tui.printBoard(board.getBoardArray());
                        //tui.updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"); // Using default test FEN
                        turn = !turn;
                        break;
                    }

                }
            } else { // AI player's turn
                // todo finish AI player
                tui.printBoard(board.getBoardArray());
                turn = !turn;
            }
        }

        // todo check if game is over?
        //System.out.println(" "+(turn ? "Black" : "White" + " player has won the game"));
        //sc.close();
    }

    public static void testBoardCompare() {
        Player p1 = new Player(true, false);
        Player p2 = new Player(false, false);

        Game testGame = new Game(new Board(), p1, p2, false);

        // temp, will be deleted after testing
        char[][] tBoard = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};

        char[][] tBoard2 = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};

        testGame.usedBoards.add(new Board(tBoard));
        testGame.usedBoards.add(new Board(tBoard2));
        testGame.usedBoards.add(new Board(tBoard));
        testGame.usedBoards.add(new Board(tBoard2));
        testGame.usedBoards.add(new Board(tBoard2));

        // this function converts all the board into a string value,
        // then each one of them gets compared to the full list,
        // if the count is 3, then it will return true
        // if no count is 3, then when it is done, it returns false
        boolean output = testGame.threefoldRepetition();
        System.out.println("output is : " + output);
    }

    // Marie first commit
    // we got a main file here
    // fixed java JDK 17
}

import Interfaces.I_TUI;
import TUI.TUI;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static I_TUI tui;
    private static Game game;
    private static Scanner sc;

    public static void main(String[] args) {

        tui = new TUI();
        sc = new Scanner(System.in);
        int gameModeSelection = tui.showStartMenu(sc);

        Player p1 = null;
        Player p2 = null;

        switch (gameModeSelection) {
            case 1 -> { // Human vs AI
                p1 = new Player(true, false);
                p2 = new Player(false, true);
            }
            case 2 -> { // AI vs AI
                p1 = new Player(true, true);
                p2 = new Player(false, true);
            }
            case 3 -> { // Human vs Human
                p1 = new Player(true, false);
                p2 = new Player(false, false);
            }
            default -> System.out.println("Error in game mode selection: " + gameModeSelection);
        }

        game = new Game(new Board(), p1, p2, true);

        // Start the game
        while (true) {

            if (game.turn) {

                while (true) {
                    tui.updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"); // Using default test FEN
                    System.out.println("Please enter the coordinate for the"
                            + " piece you want to move: ");

                    System.out.print("Start position ({X},|.{Y}): ");
                    double startPos = tui.getMovePosition(sc);
                    System.out.print("End position ({X},|.{Y}): ");
                    double endPos = tui.getMovePosition(sc);
                }

            }

            sc.close();
        }
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


    /**
     * Test loop for menu and representation of chess board
     */

    private static void testUI() {

        I_TUI tui = new TUI();

        /**
         * Handle user input;
         */

        int gameSelectionInt;

        do {
            tui.showStartMenu(sc);

            while (!sc.hasNextInt()) {
                tui.showStartMenu(sc);
                System.out.println("Please enter a number!");
                sc.next();
            }
            gameSelectionInt = sc.nextInt();
        } while (gameSelectionInt <= 0);

        //sc.close();

        /**
         * Test updateBoard();
         */

        String testFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        String testFEN2 = "r1b1k1nr/p2p1pNp/n2B4/1p1NP2P/6P1/3P1Q2/P1P1K3/q5b1";

        switch (gameSelectionInt) {
            case 1 -> {
                System.out.println("User selected: Player vs AI");
                tui.updateBoard(testFEN2);
            }
            case 2 -> {
                System.out.println("User selected: Player vs Player");
                tui.updateBoard(testFEN2);
            }
            case 3 -> {
                System.out.println("User selected: AI vs AI");
                tui.updateBoard(testFEN2);
            }
        }

    }
}

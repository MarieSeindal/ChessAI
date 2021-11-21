import Helper.Fen;
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
        Fen resumeFen;
        char startPiece = ' ';
        char destinationPiece = ' ';
        Player player;
        turn = true;

        // Prompt for resume game
        resumeFen = tui.showResumeMenu(sc);

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
        game = new Game(board, p1, p2, turn);

        // Resume game
        if (resumeFen != null) {
            turn = resumeFen.getPlayerTurn();
            board.setBoard(resumeFen.getBoardLayout());
            game.setTurn(turn);
            game.setTurnsSinceKill(resumeFen.getTurnsSinceKill());
            game.setTotalTurns(resumeFen.getTotalTurns());
            game.setCastling(resumeFen.getCastling());
            game.setEnPassantTarget(resumeFen.getEnPassantTarget());
            tui.showResumeGameData(resumeFen);
        }

        //tui.initBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"); // Using default test FEN

        /* Start the main game loop */
        while (true) {

            // todo check end game conditions (checkmate, 50 moves no kill etc.)
            if (game.turnsSinceKill >= 50) {
                System.out.println("********** GAME OVER: Draw **********");
                break;
            }

            player = game.getPlayerTurn(turn);

            if (player.isWhite()) {
                System.out.println("White Players turn");
            } else {
                System.out.println("Black Players turn");
                // Increment after each black player move.
                game.setTotalTurns(game.getTotalTurns()+1);
            }

            tui.printBoard(board.getBoardArray(), !turn);

            if (!player.isAi) { // Human player's turn

                int[] movePos;

                // Get start position and destination
                while (true) {

                    System.out.println("Please enter your move ex. (a2 a3) ");
                    movePos = tui.getMovePosition(sc);

                    // Validate piece based on user input
                    try {
                        startPiece = board.checkStartPosition(player.isWhite(), movePos[0], movePos[1]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid piece");
                    }

                    try {
                        destinationPiece = board.getPiece(movePos[2], movePos[3]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid destination selected!");
                    }

                    /* Check if chosen board position is empty */
                    if (startPiece != ' ') {

                        boolean isKill = board.isEnemyPiece(player.isWhite(), destinationPiece);

                        /* Check if chosen destination position is empty or enemy */
                        if (destinationPiece == ' ' || isKill) {
                            Move move = new Move(new int[]{movePos[2], movePos[3]}, new int[]{movePos[0], movePos[1]}, false, startPiece, ' ');
                            board.performMove(move);

                            if (isKill) {
                                /* Reset kill counter */
                                game.setTurnsSinceKill(0);
                            } else {
                                game.setTurnsSinceKill(game.getTurnsSinceKill() + 1);
                            }

                            System.out.println("Move complete!");
                            System.out.println("------------------------");
                            turn = !turn;

                            // Next player's turn
                            break;
                        }
                    }

                }

            } else { // AI player's turn
                // todo finish AI player
                tui.printBoard(board.getBoardArray(), false);
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

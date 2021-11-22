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

    public static void testMovesetFunctions()
    {
        //  region test

        // endregion

        // white queen test
        int[] location1 = new int[]{3,3};
        char[][] tBoard1 =
                {{'r', 'n', 'b', 'q', 'k', ' ', 'n', 'r'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'b', ' ', ' ', ' '},
                {' ', ' ', ' ', 'Q', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', 'B', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'R', 'N', ' ', ' ', 'K', 'B', 'N', 'R'}};
        // should give -
        // 0,3 ; 1,3 ; 2,3 ; 4,3 ; 5,3 ; 6,3 ; 7,3
        // 3,0 ; 3,1 ; 3,2 ; 3,4 ; 3,5 ; 3,6 ; 3,7
        // 2,4
        // 4,4 ; 5,5; 6,6;
        // 4,2 ;
        // 2,2 ; 1,1 ; 0,0

        // black queen test
        int[] location2 = new int[]{5,6};
        char[][] tBoard2 =
                {{'r', 'n', 'b', ' ', ' ', 'b', 'n', 'r'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'k', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', 'q', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
        // should give -
        // 0,6 ; 1,6 ; 2,6 ; 3,6 ; 4,6 ; 6,6 ;
        // 5,0 ; 5,1 ; 5,2 ; 5,3 ; 5,4 ; 5,5 ; 5,7
        // 4,7
        // 6,5 ; 7,4 ;
        // 6,7 ;
        // 4,5 ;

        // white tower test
        int[] location3 = new int[]{2,2};
        char[][] tBoard3 =
                {{'r', 'n', 'b', 'q', 'k', ' ', 'n', 'r'},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', 'R', ' ', ' ', 'B', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', 'N', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', 'K', 'B', 'N', 'R'}};
        // should give -
        // 0,2 ; 1,2 ; 3,2 ; 4,2 ;
        // 2,0 ; 2,1 ; 2,3 ; 2,4 ;

        // black tower test
        int[] location4 = new int[]{2,2};
        char[][] tBoard4 =
                        {{' ', 'n', 'b', 'q', 'k', ' ', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'r', ' ', ' ', 'B', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'N', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'K', 'B', 'N', 'R'}};
        // should give -
        // 1,2 ; 3,2 ; 4,2 ; 5,2
        // 2,0 ; 2,1 ; 2,3 ; 2,4 ; 2,5

        // white b test
        int[] location5 = new int[]{3,2};
        char[][] tBoard5 =
                        {{'r', 'n', 'b', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'B', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', ' ', 'Q', 'K', 'B', 'N', 'R'}};
        // should give -
        // 5,0 ; 4,1 ; 2,3 ; 1,4 ; 0,5
        // 1,0 ; 2,1 ; 4,3 ; 5,4 ; 6,5

        // black b test
        int[] location6 = new int[]{3,2};
        char[][] tBoard6 =
                        {{'r', 'n', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'b', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
        // should give -
        // 5,0 ; 4,1 ; 2,3 ; 1,4
        // 1,0 ; 2,1 ; 4,3 ; 5,4 ; 6,5 ; 7,6

        // white n test
        int[] location7 = new int[]{3,2};
        char[][] tBoard7 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', 'r', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'Q', ' ', ' ', ' '},
                        {' ', ' ', 'n', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'K', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'Q', ' ', 'B', 'N', 'R'}};
        // should give -
        // 1,3 ; 2,4 ; 4,4 ; 5,3 ; 5,1 ; 4,0 ; 2,0


        // black n test
        int[] location8 = new int[]{3,2};
        char[][] tBoard8 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', 'r', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'Q', ' ', ' ', ' '},
                        {' ', ' ', 'N', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'K', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'Q', ' ', 'B', 'N', 'R'}};
        // should give -
        // 1,1 ; 1,3 ; 4,4 ; 5,1 ; 4,0 ; 2,0

        // black p test
        int[] location9 = new int[]{1,2};
        char[][] tBoard9 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', 'p', ' ', ' ', ' ', ' ', ' '},
                        {' ', 'r', ' ', 'N', 'Q', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'K', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'Q', ' ', 'B', ' ', 'R'}};
        // should give -
        // 1,2 ; 2,2 ; 1,3

        // white p test
        int[] location10 = new int[]{6,2};
        char[][] tBoard10 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'Q', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', 'r', ' ', 'Q', ' ', ' ', 'q', ' '},
                        {' ', ' ', 'P', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'K', ' ', 'B', ' ', 'R'}};
        // should give -
        // 5,2 ; 4,2 ;  5,1

        // white k test
        int[] location11 = new int[]{4,3};
        char[][] tBoard11 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'r', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'K', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'B', ' ', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', ' ', ' ', ' ', 'B', ' ', 'R'}};
        // should give -
        // 3,2 ; 3,3 ; 3,4 ; 4,2 ; 4,4 ; 5,3 ; 5,4
        // TODO: king should not be able to go to - 3,2 ; 3,3 ; 4,4 ; 5,4 , since that put it in check mate

        // black k test
        int[] location12 = new int[]{4,3};
        char[][] tBoard12 =
                        {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'b', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'k', ' ', ' ', ' ', ' '},
                        {' ', ' ', 'R', ' ', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', ' ', ' ', ' ', 'B', ' ', 'R'}};
        // should give -
        // 3,2 ; 3,3 ; 4,2 ; 4,4 ; 5,2 ; 5,3 ; 5,4 ;
        // TODO: king should not be able to go to - 4,2 ; 3,2 ; 5,3 ; 5,4 , since that put it in check mate
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

import Helper.Fen;
import Helper.Writer;
import Interfaces.I_TUI;
import TUI.TUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static I_TUI tui;
    private static Game game;
    private static Scanner sc;
    private static boolean whiteTurn; //Initializing variables
    private static Player p1, p2;
    private static int[] gameModeSelection;
    private static boolean isP1White;
    private static char startPiece;
    private static char destinationPiece;
    private static Writer writer;

    public static void main(String[] args) {

        // region everything we have

        boolean runTests = false;

        if(runTests) // this is only running, if you change the boolean above
        {
//            System.out.println("test of both black and white move functions");
//            testMovesetFunctions();
//
//            System.out.println("- - - - - - - - - - - - - - - - - - - -- -  - - - -- - -  - --");

            System.out.println("test of 3 the 'Threefold repetition rule' function");
            testBoardCompare();
        }
        else // this is the whole game loop
        {
        tui = new TUI();
        sc = new Scanner(System.in);
        Fen resumeFen;
        startPiece = ' ';
        destinationPiece = ' ';
        Player player;
        isP1White = true;
        whiteTurn = true;

        // Prompt for resume game
        resumeFen = tui.showResumeMenu(sc);

        // Prompt for gamemode
        gameModeSelection = tui.showStartMenu(sc);

        if (gameModeSelection[1] == 1) { // p2 is white
            isP1White = false;
        }

        switch (gameModeSelection[0]) {
            case 1 -> { // Human vs AI
                p1 = new Player(isP1White, false);
                p2 = new Player(!isP1White, true);
            }
            case 2 -> { // Human vs Human
                p1 = new Player(isP1White, false);
                p2 = new Player(!isP1White, false);
            }
            case 3 -> { // AI vs AI
                p1 = new Player(isP1White, true);
                p2 = new Player(!isP1White, true);
            }
            case 4 -> { // AI vs Human
                p1 = new Player(isP1White, true);
                p2 = new Player(!isP1White, false);
            }
            default -> System.out.println("Error in game mode selection: " + gameModeSelection[0]);
        }

        //board = new Board();
        game = new Game(new Board(), p1, p2, whiteTurn);

        // Prepare txt file for writing
        writer = new Writer();
        writer.initialize();

        // Resume game
        if (resumeFen != null) {
            whiteTurn = resumeFen.getPlayerTurn();
            game.board.setBoardArray(resumeFen.getBoardLayout());
            if (!whiteTurn) {
                game.setWhiteTurn(true);
                whiteTurn = false;
            } else {
                game.setWhiteTurn(false);
                whiteTurn = true;
            }
            game.setTurnsSinceKill(resumeFen.getTurnsSinceKill());
            game.setTotalTurns(resumeFen.getTotalTurns());
            game.setCastling(resumeFen.getCastling());
            game.setEnPassantTarget(resumeFen.getEnPassantTarget());
            tui.showResumeGameData(resumeFen);
        }

        //tui.initBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"); // Using default test FEN

        /* Start the main game loop */
        while (true) {

            game.addUsedBoard(game.board.cloning());

            // todo check end game conditions (checkmate, 50 moves no kill etc.)
            if (game.turnsSinceKill >= 50) {
                System.out.println("********** GAME OVER: (50 moves) Draw **********");
                break;
            }

            if(game.threefoldRepetition())
            {
                System.out.println("********** GAME OVER: (3 threefold boards) Draw **********");
                break;
            }

            player = game.getPlayerTurn(whiteTurn);

            int whiteKing = Game.checkTheKing(game.board, player.isWhite());
            int blackKing = Game.checkTheKing(game.board, player.isWhite());

            // TODO: this does not work, fixed the todo inside 'checkTheKing'
//            if(whiteKing == 404 || blackKing == 404)
//            {
//                System.out.println("********** GAME OVER: one king is dead, WHAT DID YOU DO !!!! **********");
//                break;
//            }
//
//            if(whiteKing == 3 ||blackKing == 3)
//            {
//                System.out.println("********** GAME OVER: Draw (remis) **********");
//                break;
//            }
//            if(whiteKing == 2)
//            {
//                System.out.println("********** GAME OVER: black wins - white is in 'check mate' **********");
//                break;
//            }
//            if(blackKing == 2)
//            {
//                System.out.println("********** GAME OVER: white wins - black is in 'check mate' **********");
//                break;
//            }
            if(whiteKing == 1)
            {
                System.out.println("---------- black have put white in 'check' ----------");
            }
            if(blackKing == 1)
            {
                System.out.println("---------- black have put white in 'check' ----------");
            }

            //System.out.println("P1 is black: "+gameModeSelection[1]+" = "+(gameModeSelection[1] == 1? true : false)+". Is currentplayer P1 "+(player==game.getP1() ? true : false));

            System.out.println();

            //System.out.println("is current player white? "+player.isWhite());

            if (player.isWhite()) {
                System.out.println("White Players turn (" + (player == p1 ? "p1" : "p2") + ")");
            } else {
                System.out.println("Black Players turn (" + (player == p1 ? "p1" : "p2") + ")");
                // Increment after each black player move.
                game.setTotalTurns(game.getTotalTurns() + 1);
            }

//            if(false)
//                tui.printBoard(game.board.getBoardArray(), whiteTurn);
//            else
                tui.jarPrintBoard(game.board.getBoardArray(), whiteTurn);

            if (!player.isAi) { // Human player's turn

                int[] movePos;
                boolean validMove = false;

                // Get start position and destination
                while (!validMove) {

                    System.out.println("Please enter your move eg. (a2 a3) ");
                    movePos = tui.getMovePosition(sc);

                    // Validate piece based on user input
                    try {
                        startPiece = game.board.checkStartPosition(player.isWhite(), movePos[0], movePos[1]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid piece");
                    }

                    try {
                        destinationPiece = game.board.getPiece(movePos[2], movePos[3]);
                    } catch (Exception e) {
                        System.out.println("Error: Invalid destination selected!");
                    }

                    // Check if chosen board position is empty
                    if (startPiece != ' ') {

                        int isKill = Game.checkLocation(player.isWhite(), game.board.getPiece(movePos[2], movePos[3]));
                        // boolean isKill = game.board.isEnemyPiece(player.isWhite(), destinationPiece);
                        ArrayList<int[]> possibleMoves;

                        /* Check if chosen destination position is empty or enemy */
                        if (destinationPiece == ' ' || isKill == -1) {

                            // Check if move is legal
                            int[] start = new int[]{movePos[0], movePos[1]};
                            int[] destination = new int[]{movePos[2], movePos[3]};
                            System.out.println("New move info: Piece: " + startPiece + " | " + start[0] + "," + start[1] + "->" + destination[0] + "," + destination[1]);

                            try {
                                possibleMoves = game.pieceMoveset(startPiece, start, game.board, player.isWhite());

                                // Check if destination coordinate is in the list of possible moves
                                if (possibleMoves.size() > 0) {
                                    for (int[] temp : possibleMoves) {
                                        // Check for match
                                        System.out.println("Comparing: " + destination[0] + "," + destination[1] + " with " + temp[0] + "," + temp[1]);
                                        if (Arrays.equals(destination, temp)) {
                                            Move move = new Move(new int[]{movePos[2], movePos[3]}, new int[]{movePos[0], movePos[1]}, false, startPiece, ' ');

                                            // Write move to file
                                            writer.write("Human Moving: " + move.getOldField()[0]+","+move.getOldField()[1] + " ("+(char) move.getPiece()+") -> " + move.getNewField()[0]+","+move.getNewField()[1]+" ("+((char) move.getContent() == ' ' ? "' '" : (char) move.getContent() )+")");

                                            game.board.performMove(move);

                                            if (isKill == -1) {
                                                /* Reset kill counter */
                                                game.setTurnsSinceKill(0);
                                            } else {
                                                game.setTurnsSinceKill(game.getTurnsSinceKill() + 1);
                                            }

                                            System.out.println("Move complete!");
                                            System.out.println("------------------------");
                                            // Next player's turn
                                            whiteTurn = !whiteTurn;
                                            validMove = true;
                                        }
                                    }
                                    if (!validMove) {
                                        System.out.println("Move is not legal!");
                                    }
                                } else {
                                    System.out.println("Move is not legal!");
                                }
                            } catch (Exception e) {
                                System.out.println("Move is not legal!");
                            }
                        }
                    }
                }

            } else { // AI player's turn
                // todo finish AI player
                AI ai = new AI(game.board, player.isWhite(), 10, game.usedBoards);
                ChessNode firstNode = new ChessNode(game.board);
                // Record starting time
                long timeLimit = System.currentTimeMillis() / 1000;
                ai.runAI(firstNode);
                // Output finishing time
                long time = ((System.currentTimeMillis() / 1000) - timeLimit);
                System.out.println("Evaluation took: " + time + "s");

                // Safety check
                if (ai.getBestMoveBoard() != null) {
                    Move tempMove = game.board.moveFromDifferenceIn2Boards(ai.getBestMoveBoard());
                    System.out.println("AI Moving: " + tempMove.getOldField()[0] + "," + tempMove.getOldField()[1] + " -> " + tempMove.getNewField()[0] + "," + tempMove.getNewField()[1]);

                    // TODO: fix invalid pieces being printed/moved?
                    System.out.println("AI Moving Piece (start): "+ (char)game.board.getPiece(tempMove.getOldField()[0], tempMove.getOldField()[1]));
                    //System.out.println("AI Moving Piece (destination): "+game.board.getPiece(tempMove.getNewField()[0], tempMove.getNewField()[1]));
                    //System.out.println("AI Moving Piece (destination): "+tempMove.getContent());

                    // TODO: maybe we need to rethink this


                    // Write move to file todo: AI always writes 'o' in getContent()
                    writer.write("AI Moving: " + tempMove.getOldField()[0]+","+tempMove.getOldField()[1] + " ("+(char) tempMove.getPiece()+") -> " + tempMove.getNewField()[0]+","+tempMove.getNewField()[1]+" ("+((char) tempMove.getContent() == ' ' ? "' '" : (char) tempMove.getContent() )+")");

                    char pastSpot = game.board.getPiece(tempMove.newField[0], tempMove.newField[1]);

                    game.board.performMove(tempMove);

                    // Check if kill TODO: not working yet (isKill is never true)
                    int isKill = Game.checkLocation(player.isWhite(), pastSpot);

                    //boolean isKill = game.board.isEnemyPiece(player.isWhite(), game.board.getPiece(tempMove.getNewField()[0], tempMove.getNewField()[1]));
                    if (isKill == -1) {
                        /* Reset kill counter */
                        game.setTurnsSinceKill(0);
                    } else {
                        game.setTurnsSinceKill(game.getTurnsSinceKill() + 1);
                    }

                    System.out.println("Turns since kill: "+game.getTurnsSinceKill());
                }

//                if(false)
//                    tui.printBoard(game.board.getBoardArray(), false); // old - none jar
//                else
                    tui.jarPrintBoard(game.board.getBoardArray(), false); // new - works with jar
                whiteTurn = !whiteTurn;
            }
        }
//        if(false)
//            tui.printBoard(game.board.getBoardArray(), !whiteTurn);
//        else
            tui.jarPrintBoard(game.board.getBoardArray(), !whiteTurn);

        // todo check if game is over?
        //System.out.println(" "+(turn ? "Black" : "White" + " player has won the game"));
        sc.close();
        // endregion

        }

    }

    public static void arrayPrinter(ArrayList<int[]> input) {
        String output = "";

        for (int[] x : input) {
            output += x[0] + "," + x[1] + " ; ";
        }

        System.out.println(output);
    }

    // testing the Game.pieceMoveset function, for the 6 pieces, 1 time for black and 1 for white
    // first I made a test board and set the location of the piece that I want to test
    // I make a check list of all the moves, that I know that piece should be able to move to
    // last I compare the output from 'Game.pieceMoveset' against the checklist I made for that test
    // all 12 test are printed and they say if you pass or not, by writing true or false.
    public static void testMovesetFunctions() {
        // region white queen test
        int[] location1 = new int[]{3, 3};
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
        ArrayList<int[]> checkList1 = new ArrayList<int[]>();
        checkList1.add(new int[]{0, 3});
        checkList1.add(new int[]{1, 3});
        checkList1.add(new int[]{2, 3});
        checkList1.add(new int[]{4, 3});
        checkList1.add(new int[]{5, 3});
        checkList1.add(new int[]{6, 3});
        checkList1.add(new int[]{7, 3});

        checkList1.add(new int[]{3, 0});
        checkList1.add(new int[]{3, 1});
        checkList1.add(new int[]{3, 2});
        checkList1.add(new int[]{3, 4});
        checkList1.add(new int[]{3, 5});
        checkList1.add(new int[]{3, 6});
        checkList1.add(new int[]{3, 7});

        checkList1.add(new int[]{2, 4});
        checkList1.add(new int[]{4, 2});
        checkList1.add(new int[]{4, 4});
        checkList1.add(new int[]{5, 5});
        checkList1.add(new int[]{6, 6});
        checkList1.add(new int[]{2, 2});
        checkList1.add(new int[]{1, 1});
        checkList1.add(new int[]{0, 0});
        // endregion

        // region black queen test
        int[] location2 = new int[]{5, 6};
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
        // 1,6 ; 2,6 ; 3,6 ; 4,6 ; 6,6 ; 7,6
        // 5,0 ; 5,1 ; 5,2 ; 5,3 ; 5,4 ; 5,5 ; 5,7
        // 4,7
        // 6,5 ; 7,4 ;
        // 6,7 ;
        // 4,5 ;
        ArrayList<int[]> checkList2 = new ArrayList<int[]>();
        checkList2.add(new int[]{7, 6});
        checkList2.add(new int[]{2, 6});
        checkList2.add(new int[]{1, 6});
        checkList2.add(new int[]{3, 6});
        checkList2.add(new int[]{6, 6});
        checkList2.add(new int[]{4, 6});

        checkList2.add(new int[]{5, 0});
        checkList2.add(new int[]{5, 1});
        checkList2.add(new int[]{5, 2});
        checkList2.add(new int[]{5, 3});
        checkList2.add(new int[]{5, 4});
        checkList2.add(new int[]{5, 5});
        checkList2.add(new int[]{5, 7});

        checkList2.add(new int[]{4, 7});
        checkList2.add(new int[]{6, 7});
        checkList2.add(new int[]{6, 5});
        checkList2.add(new int[]{7, 4});
        checkList2.add(new int[]{4, 5});
        // endregion

        // region white tower test
        int[] location3 = new int[]{2, 2};
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

        ArrayList<int[]> checkList3 = new ArrayList<int[]>();
        checkList3.add(new int[]{0, 2});
        checkList3.add(new int[]{4, 2});
        checkList3.add(new int[]{1, 2});
        checkList3.add(new int[]{3, 2});


        checkList3.add(new int[]{2, 0});
        checkList3.add(new int[]{2, 1});
        checkList3.add(new int[]{2, 3});
        checkList3.add(new int[]{2, 4});
        // endregion

        // region black tower test
        int[] location4 = new int[]{2, 2};
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
        ArrayList<int[]> checkList4 = new ArrayList<int[]>();
        checkList4.add(new int[]{4, 2});
        checkList4.add(new int[]{5, 2});
        checkList4.add(new int[]{1, 2});
        checkList4.add(new int[]{3, 2});


        checkList4.add(new int[]{2, 0});
        checkList4.add(new int[]{2, 1});
        checkList4.add(new int[]{2, 3});
        checkList4.add(new int[]{2, 4});
        checkList4.add(new int[]{2, 5});
        // endregion

        // region white b test
        int[] location5 = new int[]{3, 2};
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
        ArrayList<int[]> checkList5 = new ArrayList<int[]>();
        checkList5.add(new int[]{5, 0});
        checkList5.add(new int[]{4, 1});
        checkList5.add(new int[]{2, 3});
        checkList5.add(new int[]{1, 4});
        checkList5.add(new int[]{0, 5});


        checkList5.add(new int[]{1, 0});
        checkList5.add(new int[]{2, 1});
        checkList5.add(new int[]{4, 3});
        checkList5.add(new int[]{5, 4});
        checkList5.add(new int[]{6, 5});
        // endregion

        // region black b test
        int[] location6 = new int[]{3, 2};
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
        ArrayList<int[]> checkList6 = new ArrayList<int[]>();
        checkList6.add(new int[]{5, 0});
        checkList6.add(new int[]{4, 1});
        checkList6.add(new int[]{2, 3});
        checkList6.add(new int[]{1, 4});


        checkList6.add(new int[]{1, 0});
        checkList6.add(new int[]{2, 1});
        checkList6.add(new int[]{4, 3});
        checkList6.add(new int[]{5, 4});
        checkList6.add(new int[]{6, 5});
        checkList6.add(new int[]{7, 6});
        // endregion

        // region white n test
        int[] location7 = new int[]{3, 2};
        char[][] tBoard7 =
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
        ArrayList<int[]> checkList7 = new ArrayList<int[]>();
        checkList7.add(new int[]{1, 3});
        checkList7.add(new int[]{5, 1});
        checkList7.add(new int[]{1, 1});
        checkList7.add(new int[]{4, 4});
        checkList7.add(new int[]{4, 0});
        checkList7.add(new int[]{2, 0});
        // endregion

        // region black n test
        int[] location8 = new int[]{3, 2};
        char[][] tBoard8 =
                {{' ', ' ', ' ', ' ', 'k', 'b', 'n', 'r'},
                        {' ', 'r', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', 'Q', ' ', ' ', ' '},
                        {' ', ' ', 'n', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', 'K', ' ', ' ', 'q', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'R', 'N', 'B', 'Q', ' ', 'B', 'N', 'R'}};
        // should give -
        // 1,3 ; 2,4 ; 4,4 ; 5,1 ; 5,3 ; 4,0 ; 2,0
        ArrayList<int[]> checkList8 = new ArrayList<int[]>();
        checkList8.add(new int[]{5, 1});
        checkList8.add(new int[]{5, 3});
        checkList8.add(new int[]{1, 3});
        checkList8.add(new int[]{4, 0});
        checkList8.add(new int[]{4, 4});
        checkList8.add(new int[]{2, 0});
        checkList8.add(new int[]{2, 4});
        // endregion

        // region black p test
        int[] location9 = new int[]{1, 2};
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
        // 2,3 ; 2,2 ; 3,2
        ArrayList<int[]> checkList9 = new ArrayList<int[]>();
        checkList9.add(new int[]{3, 2});
        checkList9.add(new int[]{2, 2});
        checkList9.add(new int[]{2, 3});
        // endregion

        // region white p test
        int[] location10 = new int[]{6, 2};
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
        // 5,2 ; 4,2 ; 5,1
        ArrayList<int[]> checkList10 = new ArrayList<int[]>();
        checkList10.add(new int[]{5, 2});
        checkList10.add(new int[]{4, 2});
        checkList10.add(new int[]{5, 1});
        // endregion

        // region white k test
        int[] location11 = new int[]{4, 3};
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
        ArrayList<int[]> checkList11 = new ArrayList<int[]>();
        checkList11.add(new int[]{3, 2});
        checkList11.add(new int[]{4, 2});
        checkList11.add(new int[]{3, 3});
        checkList11.add(new int[]{4, 4});
        checkList11.add(new int[]{3, 4});
        checkList11.add(new int[]{5, 3});
        checkList11.add(new int[]{5, 4});
        // TODO: king should not be able to go to - 3,2 ; 3,3 ; 4,4 ; 5,4 , since that put it in check mate
        // endregion

        // region black k test
        int[] location12 = new int[]{4, 3};
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
        ArrayList<int[]> checkList12 = new ArrayList<int[]>();
        checkList12.add(new int[]{3, 2});
        checkList12.add(new int[]{4, 2});
        checkList12.add(new int[]{3, 3});
        checkList12.add(new int[]{5, 3});
        checkList12.add(new int[]{4, 4});
        checkList12.add(new int[]{5, 2});
        checkList12.add(new int[]{5, 4});
        // TODO: king should not be able to go to - 4,2 ; 3,2 ; 5,3 ; 5,4 , since that put it in check mate
        // endregion

        ArrayList<int[]> testValues = new ArrayList<>();
        Board testBoard = new Board();
        boolean output = false;

        // region test 1 - white q
        testBoard.setBoardArray(tBoard1);
        testValues = Game.pieceMoveset('Q', location1, testBoard, true);
        output = Game.arrayListContainsAll(checkList1, testValues);
        System.out.println("white q test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white q check list : ");
        Main.arrayPrinter(checkList1);
        System.out.println("did white q pass the test? : " + output);
        // endregion

        // region test 2 - black q
        testBoard.setBoardArray(tBoard2);
        testValues = Game.pieceMoveset('q', location2, testBoard, false);
        output = Game.arrayListContainsAll(checkList2, testValues);
        System.out.println("black q test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black q check list : ");
        Main.arrayPrinter(checkList2);
        System.out.println("did black q pass the test? : " + output);
        // endregion

        // region test 3 - white r
        testBoard.setBoardArray(tBoard3);
        testValues = Game.pieceMoveset('R', location3, testBoard, true);
        output = Game.arrayListContainsAll(checkList3, testValues);
        System.out.println("white r test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white r check list : ");
        Main.arrayPrinter(checkList3);
        System.out.println("did white r pass the test? : " + output);
        // endregion

        // region test 4 - black r
        testBoard.setBoardArray(tBoard4);
        testValues = Game.pieceMoveset('r', location4, testBoard, false);
        output = Game.arrayListContainsAll(checkList4, testValues);
        System.out.println("black r test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black r check list : ");
        Main.arrayPrinter(checkList4);
        System.out.println("did black r pass the test? : " + output);
        // endregion

        // region test 5 - white b
        testBoard.setBoardArray(tBoard5);
        testValues = Game.pieceMoveset('B', location5, testBoard, true);
        output = Game.arrayListContainsAll(checkList5, testValues);
        System.out.println("white b test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white b check list : ");
        Main.arrayPrinter(checkList5);
        System.out.println("did white b pass the test? : " + output);
        // endregion

        // region test 6 - black b
        testBoard.setBoardArray(tBoard6);
        testValues = Game.pieceMoveset('b', location6, testBoard, false);
        output = Game.arrayListContainsAll(checkList6, testValues);
        System.out.println("black b test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black b check list : ");
        Main.arrayPrinter(checkList6);
        System.out.println("did black b pass the test? : " + output);
        // endregion

        // region test 7 - white n
        testBoard.setBoardArray(tBoard7);
        testValues = Game.pieceMoveset('N', location7, testBoard, true);
        output = Game.arrayListContainsAll(checkList7, testValues);
        System.out.println("white n test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white n check list : ");
        Main.arrayPrinter(checkList7);
        System.out.println("did white n pass the test? : " + output);
        // endregion

        // region test 8 - black n
        testBoard.setBoardArray(tBoard8);
        testValues = Game.pieceMoveset('n', location8, testBoard, false);
        output = Game.arrayListContainsAll(checkList8, testValues);
        System.out.println("black n test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black n check list : ");
        Main.arrayPrinter(checkList8);
        System.out.println("did black n pass the test? : " + output);
        // endregion

        // region test 9 - black p
        testBoard.setBoardArray(tBoard9);
        testValues = Game.pieceMoveset('p', location9, testBoard, false);
        output = Game.arrayListContainsAll(checkList9, testValues);
        System.out.println("black p test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black p check list : ");
        Main.arrayPrinter(checkList9);
        System.out.println("did black p pass the test? : " + output);
        // endregion

        // region test 10 - white p
        testBoard.setBoardArray(tBoard10);
        testValues = Game.pieceMoveset('P', location10, testBoard, true);
        output = Game.arrayListContainsAll(checkList10, testValues);
        System.out.println("white p test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white p check list : ");
        Main.arrayPrinter(checkList10);
        System.out.println("did white p pass the test? : " + output);
        // endregion

        // region test 11 - white k
        testBoard.setBoardArray(tBoard11);
        testValues = Game.pieceMoveset('K', location11, testBoard, true);
        output = Game.arrayListContainsAll(checkList11, testValues);
        System.out.println("white k test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("white k check list : ");
        Main.arrayPrinter(checkList11);
        System.out.println("did white k pass the test? : " + output);
        // endregion

        // region test 12 - black k
        testBoard.setBoardArray(tBoard12);
        testValues = Game.pieceMoveset('k', location12, testBoard, false);
        output = Game.arrayListContainsAll(checkList12, testValues);
        System.out.println("black k test values : ");
        Main.arrayPrinter(testValues);
        System.out.println("black k check list : ");
        Main.arrayPrinter(checkList12);
        System.out.println("did black k pass the test? : " + output);
        // endregion

    }

    // testing if the game can detect if the game have 3 identical boards
    // this is just to test the function, the boards does not need to make sense
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

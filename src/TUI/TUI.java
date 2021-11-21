package TUI;

import Helper.Fen;
import Interfaces.I_TUI;

import java.util.*;

public class TUI implements I_TUI {

    @Override
    public void resetBoard() {

    }

    @Override
    public void initBoard(String fen) {

    }

    @Override
    public void printBoard(char[][] board, boolean white) {

        boolean whiteField = true;

        if (white) {

            // Print column numbers
            System.out.println("   h  g  f  e  d  c  b  a ");
            for (int r = board.length - 1; r >= 0; r--) { //Rows

                // Print row numbers
                System.out.print(8 - r + " ");

                for (int c = board[r].length - 1; c >= 0; c--) { //Cols

                    if (whiteField) {
                        //System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                        System.out.print(WHITE_BACKGROUND + BLACK + " " + board[r][c] + " " + RESET);
                        whiteField = false;

                        continue;
                    }
                    if (!whiteField) {

                        System.out.print(BLACK_BACKGROUND + WHITE + " " + board[r][c] + " " + RESET);
                        whiteField = true;
                    }

                }

                if (whiteField) {
                    whiteField = false;
                } else {
                    whiteField = true;
                }

                // Print row numbers
                System.out.print(" " + (8 - r));

                System.out.println(); //new line.
            }
            System.out.println("   h  g  f  e  d  c  b  a ");

        } else {

            // Print column numbers
            System.out.println("   a  b  c  d  e  f  g  h ");
            for (int r = 0; r < board.length; r++) { //Rows

                // Print row numbers
                System.out.print(8 - r + " ");

                for (int c = 0; c < board[r].length; c++) { //Cols

                    if (whiteField) {
                        //System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                        System.out.print(WHITE_BACKGROUND + BLACK + " " + board[r][c] + " " + RESET);
                        whiteField = false;
                        continue;
                    }
                    if (!whiteField) {
                        System.out.print(BLACK_BACKGROUND + WHITE + " " + board[r][c] + " " + RESET);
                        whiteField = true;
                    }

                }

                if (whiteField) {
                    whiteField = false;
                } else {
                    whiteField = true;
                }

                // Print row numbers
                System.out.print(" " + (8 - r));
                System.out.println(); //new line.
            }
            System.out.println("   a  b  c  d  e  f  g  h ");

        }

    }

    @Override
    public int showStartMenu(Scanner sc) {

        int gameSelectionInt = -1;

        do {
            System.out.println("+-----------------------------------+");
            System.out.println("|  Welcome to Group 7's Chess game  |");
            System.out.println("+-----------------------------------+");
            System.out.println("""
                    Please select a game mode from below options:\s
                    1) Player vs AI.
                    2) Player vs Player.
                    3) AI vs AI. (Danger! Not done. Will run an infinite loop)
                    """);
            System.out.print("Input: ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter a number!\nInput: ");
                sc.next();
            }
            gameSelectionInt = sc.nextInt();
        } while (gameSelectionInt <= 0 || gameSelectionInt > 3);

        return gameSelectionInt;
    }

    @Override
    public Fen showResumeMenu(Scanner sc) {
        int resumeSelectionInt = -1;

        do {
            System.out.println("""
                    Do you want to resume a game by FEN?:\s
                    1) No
                    2) Yes
                    """);
            System.out.print("Input: ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter a number!\nInput: ");
                sc.next();
            }
            resumeSelectionInt = sc.nextInt();
        } while (resumeSelectionInt < 1 || resumeSelectionInt > 2);

        if (resumeSelectionInt == 2) {
            while (true) {
                System.out.println("\nPlease enter a FEN string\n ");
                System.out.print("Input: ");
                sc.useDelimiter("\n");// To make scanner accept white space
                String f = sc.next();
                Fen fen = parseFen(f);
                if (fen != null) {
                    return fen;
                } else {
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public void showEndFinished() {

    }

    @Override
    public int[] getMovePosition(Scanner sc) {

        String input;

        while (true) {

            System.out.print("Input: ");
            int x1 = -1, y1 = -1, x2 = -1, y2 = -1;

            try {
                sc.useDelimiter("\n");// To make scanner accept white space
                input = sc.next();
                char[] c = input.toCharArray();

                if (c.length == 5 && c[2] == ' ') {

                    if (Character.isLetter(c[0]) && Character.isLetter(c[3])) {
                        y1 = convertFieldToPos(c[0]);
                        y2 = convertFieldToPos(c[3]);
                    }

                    if (Character.isDigit(c[1]) && Character.isDigit(c[4])) {

                        x1 = Character.getNumericValue(c[1]);
                        x1 = 8 - x1;

                        x2 = Character.getNumericValue(c[4]);
                        x2 = 8 - x2;

                    }

                    if ((x1 >= 0 && x1 <= 7) && (y1 >= 0 && y1 <= 7)) {
                        return new int[]{x1, y1, x2, y2};
                    } else {
                        System.out.println("Error: Out of range");
                    }
                } else {
                    System.out.println("Error: Invalid input");
                }

            } catch (NumberFormatException ne) {
                System.out.println("Error: " + ne);
            }

        }

    }

    /**
     * Converts char to column index
     *
     * @param c
     * @return
     */

    int convertFieldToPos(char c) {

        int out = switch (c) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };

        return out;
    }

    @Override
    public void clearConsole() {
        System.out.println(System.lineSeparator().repeat(100));
    }

    @Override
    public void showResumeGameData(Fen f) {
        System.out.println("Resume Game info: ");
        System.out.println("Side to move: "+(f.getPlayerTurn() ? "White " : "Black ")+"Player");
        System.out.print("Castling ability: ");
        for (int i = 0; i<f.getCastling().size(); i++) {
            System.out.print(f.getCastling().get(i));
        }
        System.out.println("");
        System.out.println("En passant target square: "+f.getEnPassantTarget());
        System.out.println("Total Moves: "+f.getTotalTurns());
        System.out.println("Total Moves Since Kill: "+f.getTurnsSinceKill());
        System.out.println("------------------------");
        System.out.println("");
    }

    public static Fen parseFen(String fen) {

        Fen f = new Fen();
        try {

            char[][] out = new char[8][8];

            /* Convert FEN string individual "bits" using " " as the delimiter */
            String[] str_spaces = fen.split(" ");

            /**
             * Board Positions
             */

            /* Convert FEN string to rows with "/" as the delimiter */
            String[] str_board = str_spaces[0].split("/");

            String output = "";

            for (int i = 0; i<str_board.length; i++) {
                /* Split each item into its own and get the piece */
                String[] str = str_board[i].split("");

                for (int j = 0; j<str.length; j++) {
                    char c = str[j].charAt(0);
                    if (checkInt(str[j]) != -1) {
                        int num = Integer.parseInt(str[j]);
                        for (int k = 0; k < num; k++) {
                            output = output + " ";
                        }
                    } else {
                        output = output + c;
                    }
                }

            }

            // Convert string to 2d char array
            int offset = 0;
            for (int k = 0; k<8; k++) {
                for (int l = 0; l<8; l++) {
                    out[k][l] = output.charAt(offset++);
                }

            }

            // test output
            /*for (int k = 0; k<8; k++) {
                for (int l = 0; l<8; l++) {
                    System.out.print(out[k][l]);
                    if (l == 7) {
                        System.out.println("\n");
                    }
                }

            }*/

            f.setBoardLayout(out);

            /**
             * Player
             */

            if (str_spaces[1] != null) {
                String turn = str_spaces[1];
                f.setPlayerTurn(turn.charAt(0) == 'w' ? true : false);
            }

            /**
             * Castling
             */

            if (str_spaces[2] != null) {
                String castling = str_spaces[2];
                String[] castlingArr = castling.split("");
                ArrayList<String> c = new ArrayList();
                Collections.addAll(c, castlingArr);
                f.setCastling(c);
            }

            /**
             * En passant target square
             */

            if (str_spaces[3] != null) {
                f.setEnPassantTarget(str_spaces[3]);
            }

            /**
             * Moves Since Kill
             */

            if (str_spaces[4] != null) {
                f.setMovesKill(Integer.parseInt(str_spaces[4]));
            }

            /**
             * Total number of Moves
             */

            if (str_spaces[5] != null) {
               f.setTotalMoves(Integer.parseInt(str_spaces[5]));
            }

        } catch (Exception e) {
            System.out.println("Invalid FEN: "+e);
        }
        return f;
    }

    static int getPiece(String t) {

        return switch (t) {
            case "r" -> -2;
            case "n" -> -3;
            case "q" -> -5;
            case "k" -> -6;
            case "p" -> -1;
            case "b" -> -4;
            case "R" -> 2;
            case "N" -> 3;
            case "Q" -> 5;
            case "K" -> 6;
            case "P" -> 1;
            case "B" -> 4;
            default -> 0;
        };
    }

    //https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println //
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  //

    /* Check if string value is an integer */
    public static int checkInt(String s) {

        try{
            int n = Integer.parseInt(s);
            return n;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}

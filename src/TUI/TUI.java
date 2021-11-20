package TUI;

import Interfaces.I_TUI;

import java.awt.*;
import java.util.Scanner;

public class TUI implements I_TUI {

    public String charToSymbol(char piece) {
        String output = "";

        String blackColor = "\u001b[32;1m"; // green
        String whiteColor = "\u001b[31;1m"; // red

        switch (piece) {
            case ' ' -> output = String.valueOf((char) 9817);
            case 'p' -> {
                output += blackColor;
                output += String.valueOf((char) 9823);
            }
            case 'P' -> {
                output += whiteColor;
                output += String.valueOf((char) 9817);
            }
            case 'k' -> {
                output += blackColor;
                output += String.valueOf((char) 9818);
            }
            case 'K' -> {
                output += whiteColor;
                output += String.valueOf((char) 9812);
            }
            case 'Q' -> {
                output += whiteColor;
                output += String.valueOf((char) 9813);
            }
            case 'q' -> {
                output += blackColor;
                output += String.valueOf((char) 9819);
            }
            case 'r' -> {
                output += blackColor;
                output += String.valueOf((char) 9820);
            }
            case 'R' -> {
                output += whiteColor;
                output += String.valueOf((char) 9814);
            }
            case 'B' -> {
                output += whiteColor;
                output += String.valueOf((char) 9815);
            }
            case 'b' -> {
                output += blackColor;
                output += String.valueOf((char) 9821);
            }
            case 'N' -> {
                output += whiteColor;
                output += String.valueOf((char) 9816);
            }
            case 'n' -> {
                output += blackColor;
                output += String.valueOf((char) 9822);
            }
        }

        return output;
    }

    @Override
    public void resetBoard() {

    }

    @Override
    public void initBoard(String fen) {

        String[] out = fenFormatter(fen);
        boolean whiteField = true;

        // Print column numbers
        System.out.println("   a  b  c  d  e  f  g  h ");
        int row = 0;
        for (String s : out) {

            if (s.length() == 0)
                continue;

            // Print row numbers
            System.out.print(row + " ");
            row++;

            for (int i = 0; i < s.length(); i++) {
                if (whiteField) {
                    //System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                    System.out.print(WHITE_BACKGROUND + BLACK + " " + s.charAt(i) + " " + RESET);
                    whiteField = false;

                    if (i == s.length() - 1) { //If end of line, new line
                        System.out.println("");
                    }
                    continue;
                }
                if (!whiteField) {
                    System.out.print(BLACK_BACKGROUND + WHITE + " " + s.charAt(i) + " " + RESET);
                    whiteField = true;

                    if (i == s.length() - 1) { //If end of line, new line
                        System.out.println("");
                    }
                }
            }
            if (whiteField)
                whiteField = false;
            else whiteField = true;

            // Print row numbers
            System.out.print(row + " ");
            row++;

        }
        System.out.println("   a  b  c  d  e  f  g  h ");
        //System.out.println("Did the string color reset?");

    }

    @Override
    public void printBoard(char[][] board, boolean white) {

        boolean whiteField = true;

        if (white) {

            // Print column numbers
            System.out.println("  h   g   f   e   d   c   b   a  ");
            for (int r = board.length - 1; r >= 0; r--) { //Rows

                // Print row numbers
                System.out.print(8 - r + " ");

                for (int c = board[r].length - 1; c >= 0; c--) { //Cols

                    if (whiteField) {
                        // System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                        // System.out.print(WHITE_BACKGROUND + BLACK + " " + board[r][c] + " " + RESET);
                        System.out.print(WHITE_BACKGROUND + " " + WHITE + charToSymbol(board[r][c]) + " " + RESET);
                        whiteField = false;

                        continue;
                    }
                    if (!whiteField) {

                        // System.out.print(BLACK_BACKGROUND + WHITE + " " + board[r][c] + " " + RESET);
                        System.out.print(BLACK_BACKGROUND + " " + BLACK + charToSymbol(board[r][c]) + " " + RESET);
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
            System.out.println("  h   g   f   e   d   c   b   a ");

        } else {

            // Print column numbers
            System.out.println("  a   b   c   d   e   f   g   h ");
            for (int r = 0; r < board.length; r++) { //Rows

                // Print row numbers
                System.out.print(8 - r + " ");

                for (int c = 0; c < board[r].length; c++) { //Cols

                    if (whiteField) {
                        //System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                        // System.out.print(WHITE_BACKGROUND + BLACK + " " + board[r][c] + " " + RESET);
                        System.out.print(WHITE_BACKGROUND + " " + WHITE + charToSymbol(board[r][c]) + " " + RESET);
                        whiteField = false;

                        continue;
                    }
                    if (!whiteField) {

                        // System.out.print(BLACK_BACKGROUND + WHITE + " " + board[r][c] + " " + RESET);
                        System.out.print(BLACK_BACKGROUND + " " + BLACK + charToSymbol(board[r][c]) + " " + RESET);
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
            System.out.println("  a   b   c   d   e   f   g   h ");

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
    public String showResumeMenu(Scanner sc) {
        int resumeSelectionInt = 0;

        do {
            System.out.println("""
                    Do you want to resume a game by FEN?:\s
                    1) No.
                    2) Yes.
                    """);
            System.out.print("Input: ");
            while (!sc.hasNextInt()) {
                System.out.println("Please enter a number!\nInput: ");
                sc.next();
            }
            resumeSelectionInt = sc.nextInt();
        } while (resumeSelectionInt != 1 || resumeSelectionInt != 2);

        if (resumeSelectionInt == 2) {
            return sc.nextLine();
        }

        return "";
    }

    @Override
    public void showEndFinished() {

    }

    @Override
    public int[] getMovePosition(Scanner sc) {

        String input;

        while (true) {

            System.out.print("\nInput: ");
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

    public static String[] fenFormatter(String fen) {

        String[] out = new String[10];

        out[0] = ""; // Print these another place, so this method only handles the board
        out[9] = "";

        /* Convert FEN string to rows with "/" as the delimiter */
        String[] str = fen.split("/");

        /* Split each item into its own and get the piece */
        for (int i = 0; i < str.length; i++) {
            String[] chars = str[i].split("");
            String temp = "";
            for (String aChar : chars) {
                int code = getPiece(aChar);

                /* Check if we have a number (empty rows) instead of a chess piece position */
                if (code == 0) {
                    int num = Integer.parseInt("" + aChar);
                    for (int k = 0; k < num; k++) {
                        temp += " "; //temp += "___|";
                    }
                } else {
                    //temp += "_" + aChar + "_|";
                    temp += aChar;
                }
                out[i + 1] = temp;
            }
        }

        return out;
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
    public static final String RESET = "\u001B[0;1m";
    public static final String BLACK = "\u001B[37;1m";
    public static final String RED = "\u001B[31;1m";
    public static final String GREEN = "\u001B[32;1m";
    public static final String YELLOW = "\u001B[33;1m";
    public static final String BLUE = "\u001B[34;1m";
    public static final String PURPLE = "\u001B[35;1m";
    public static final String CYAN = "\u001B[36;1m";
    public static final String WHITE = "\u001B[30;1m";

    public static final String BLACK_BACKGROUND = "\u001B[47;1m";
    public static final String RED_BACKGROUND = "\u001B[41;1m";
    public static final String GREEN_BACKGROUND = "\u001B[42;1m";
    public static final String YELLOW_BACKGROUND = "\u001B[43;1m";
    public static final String BLUE_BACKGROUND = "\u001B[44;1m";
    public static final String PURPLE_BACKGROUND = "\u001B[45;1m";
    public static final String CYAN_BACKGROUND = "\u001B[46;1m";
    public static final String WHITE_BACKGROUND = "\u001B[40;1m";
    // -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  //

}

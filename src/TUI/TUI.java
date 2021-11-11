package TUI;

import Interfaces.I_TUI;

import java.awt.*;
import java.util.Scanner;

public class TUI implements I_TUI {

    @Override
    public void resetBoard() {

    }

    @Override
    public void updateBoard(String fen) {

        String[] out = fenFormatter(fen);
        boolean whiteField = true;

        for (String s : out) {

            if (s.length() == 0)
                continue;

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

        }

        //System.out.println("Did the string color reset?");

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
                    3) AI vs AI.
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
    public void showEndFinished() {

    }

    @Override
    public double getMovePosition(Scanner sc) {
        while (true) {
            System.out.print("\nInput: ");
            try {
                String input = sc.next();
                String[] parts = input.split(",|\\.");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                if ((x >= 0 && x <= 7) && (y >= 0 && y <= 7)) {
                    double position = Double.parseDouble(x + "." + y);
                    return position;
                } else {
                    System.out.println("Error: Out of range");
                }
            } catch (NumberFormatException ne) {
                System.out.println("Error: " + ne);
            }

        }
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

}

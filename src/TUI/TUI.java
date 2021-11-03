package TUI;

import Interfaces.I_TUI;

public class TUI implements I_TUI {

    @Override
    public void resetBoard() {

    }

    @Override
    public void updateBoard(String fen) {

        String[] out = fenFormatter(fen);

        for (String s : out) {
            System.out.println(s);
        }

    }

    @Override
    public void showStartMenu() {
        showMenu();
    }

    @Override
    public void showEndFinished() {

    }


    private static void showMenu(){
        System.out.println("+-----------------------------------+");
        System.out.println("|  Welcome to Group 7's Chess game  |");
        System.out.println("+-----------------------------------+");
        System.out.println("""
                Please select a game mode from below options:\s
                1) Player vs AI.
                2) Player vs Player.
                3) AI vs AI.
                """);
    }

    public static String[] fenFormatter(String fen) {

        String[] out = new String[10];

        out[0] = "_________________________________";
        out[9] = "_________________________________";

        /* Convert FEN string to rows with "/" as the delimiter */
        String[] str = fen.split("/");

        /* Split each item into its own and get the piece */
        for (int i = 0; i < str.length; i++) {
            String[] chars = str[i].split("");
            String temp = "|";
            for (String aChar : chars) {
                int code = getPiece(aChar);

                /* Check if we have a number instead of a chess piece */
                if (code == 0) {
                    int num = Integer.parseInt("" + aChar);
                    for (int k = 0; k < num; k++) {
                        temp += "___|";
                    }
                } else {
                    temp += "_" + aChar + "_|";
                }
                out[i + 1] = temp;
            }
        }

        return out;
    }

    static int getPiece(String t){

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

}

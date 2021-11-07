package TUI;

import Interfaces.I_TUI;

public class TUI implements I_TUI {

    @Override
    public void resetBoard() {

    }

    @Override
    public void updateBoard(String fen) {

        String[] out = fenFormatter(fen);
        boolean whiteField = false;

        for (String s : out) {

            if (s.length() == 0)
                continue;

            for (int i = 0; i < s.length(); i++) {

                if(whiteField){
                    System.out.print("\u001B[47m " + s.charAt(i) + " \033[0m");
                    whiteField = false;

                    if (i == s.length()-1){ //If end of line, new line
                        System.out.println("");
                    }
                    continue;
                }
                if(!whiteField){

                    System.out.print("\u001B[40m" + "\033[0;37m " + s.charAt(i) + " \033[0m");
                    whiteField = true;

                    if (i == s.length()-1){ //If end of line, new line
                        System.out.println("");
                    }
                }
            }
            if (whiteField)
                whiteField = false;
            else whiteField = true;

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

        out[0] = "";
        out[9] = "";

        /* Convert FEN string to rows with "/" as the delimiter */
        String[] str = fen.split("/");

        /* Split each item into its own and get the piece */
        for (int i = 0; i < str.length; i++) {
            String[] chars = str[i].split("");
            String temp = "";
            for (String aChar : chars) {
                int code = getPiece(aChar);

                /* Check if we have a number instead of a chess piece */
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

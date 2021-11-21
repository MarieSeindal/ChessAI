package Interfaces;

import Helper.Fen;
import java.util.Scanner;

public interface I_TUI {

    void resetBoard();

    void initBoard(String fen);

    void printBoard(char[][] board, boolean reverse);

    int showStartMenu(Scanner sc);

    Fen showResumeMenu(Scanner sc);

    void showEndFinished();

    int[] getMovePosition(Scanner sc);

    void clearConsole();

    void showResumeGameData(Fen f);

}

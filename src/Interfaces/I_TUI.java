package Interfaces;

import java.awt.*;
import java.util.Scanner;

public interface I_TUI {

    void resetBoard();

    void updateBoard(String fen);

    int showStartMenu(Scanner sc);

    void showEndFinished();

    double getMovePosition(Scanner sc);

}

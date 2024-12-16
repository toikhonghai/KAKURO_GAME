package KakuroController;

import KakuroController.KakuroGame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        KakuroGame KG = new KakuroGame(10);
//        KG.generateKakuroRandom();
//        KG.printBoard();
//        SwingUtilities.invokeLater(() -> {
        KakuroMenu menu = new KakuroMenu();
        menu.setVisible(true);
//        });
    }
}

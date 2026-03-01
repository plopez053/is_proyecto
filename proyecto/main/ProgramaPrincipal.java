package main;

import Model.GameBoard;
import viewController.PantallaPrincipal;

public class ProgramaPrincipal {
    public static void main(String[] args) {
        // MODELO //
        GameBoard.getGameBoard();

        // VISTA //
        @SuppressWarnings("unused")
        PantallaPrincipal pp = new PantallaPrincipal();
        pp.setVisible(true);
    }
}

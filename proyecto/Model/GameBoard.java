package Model;

import java.util.Observable;

public class GameBoard extends Observable {
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Casilla[][] matrix;
    private int posXInicio = 55;
    private int posYInicio = 50;
    private Casilla naveC;
    private Nave nave;

    private GameBoard() {
        matrix = new Casilla[height][width];
        clearBoard();
    }

    public static GameBoard getGameBoard() {
        if (miGameBoard == null) {
            miGameBoard = new GameBoard();
        }
        return miGameBoard;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Casilla getCasilla(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return matrix[y][x];
        }
        return null;
    }

    public void setCasilla(int x, int y, Casilla casilla) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            matrix[y][x] = casilla;
        }
    }

    public void clearBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = new Casilla(j, i);
            }
        }
    }

    public void crearTablero() {
        clearBoard();
        EnemigoManager.getEnemigoManager().spawnEnemies();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Casilla c = matrix[i][j];

                if (i == posYInicio && j == posXInicio) {
                    nave = new NaveJugador();
                    nave.setX(j);
                    nave.setY(i);
                    c.setContenido(nave);
                    naveC = c;
                }

                Enemigo e = EnemigoManager.getEnemigoManager().getEnemigoEn(j, i);
                if (e != null) {
                    c.setContenido(e);
                }
            }
        }
        EnemigoManager.getEnemigoManager().iniciarTimer();
        setChanged();
        notifyObservers(new int[] { 1, naveC.getY(), naveC.getX() });
    }

    public void actualizarTablero() {
        setChanged();
        if (naveC != null) {
            notifyObservers(new int[] { naveC.getY(), naveC.getX() });
        }
    }

    public void moverNave(int dx, int dy) {
        if (naveC == null)
            return;
        int nuevaX = naveC.getX() + dx;
        int nuevaY = naveC.getY() + dy;

        if (nuevaX >= 0 && nuevaX < width && nuevaY >= 0 && nuevaY < height) {
            Entidad ent = naveC.getContenido();
            if (ent != null) {
                ent.setX(nuevaX);
                ent.setY(nuevaY);
            }
            naveC.setContenido(null);
            naveC = matrix[nuevaY][nuevaX];
            naveC.setContenido(ent);
            actualizarTablero();
        }
    }

    public void eliminarEnemigoEn(int x, int y) {
        Enemigo e = EnemigoManager.getEnemigoManager().getEnemigoEn(x, y);
        if (e != null) {
            EnemigoManager.getEnemigoManager().removeEnemigo(e);
        }
    }

    public Nave getNave() {
        return nave;
    }
}

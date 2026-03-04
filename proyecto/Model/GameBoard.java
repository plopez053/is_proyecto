package Model;

import java.util.Observable;

public class GameBoard extends Observable {
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Casilla[][] matrix;
    private int posXInicio = 50;
    private int posYInicio = 55;
    private Nave naveC;

    private GameBoard() {
        matrix = new Casilla[height][width];
        clearBoard();
    }

    public static synchronized GameBoard getGameBoard() {
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
                matrix[i][j] = new Vacia(j, i); // j is x, i is y
            }
        }
    }

    public void crearTablero() {
        EnemigoManager.getEnemigoManager().spawnEnemies();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == posYInicio && j == posXInicio) {
                    naveC = new Nave(j, i);
                }
                Enemigo e = EnemigoManager.getEnemigoManager().getEnemigoEn(j, i);
                if (e != null) {
                    matrix[i][j] = e;
                } else {
                    matrix[i][j] = new Vacia(j, i);
                }
            }
        }
        EnemigoManager.getEnemigoManager().iniciarTimer();
        setChanged();
        // Avisamos con un código especial (1) para abrir la pantalla, y pasamos el
        // primer snapshot
        notifyObservers(new Object[] { 1, getBoardActual() });
    }

    public void actualizarTablero() {
        setChanged();
        notifyObservers(getBoardActual());
    }

    private int[][] getBoardActual() {
        int[][] snapshot = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Casilla c = matrix[i][j];
                if (c instanceof Enemigo) {
                    snapshot[i][j] = 0;
                } else if (c instanceof Nave) {
                    snapshot[i][j] = 1;
                } else if (c instanceof Disparo) {
                    snapshot[i][j] = 2;
                } else {
                    snapshot[i][j] = 3;
                }
            }
        }
        return snapshot;
    }

    public void moverNave(int direccion) {

        int nuevaX = naveC.getX() + direccion;

        if (dentroRango(nuevaX, naveC.getY())) {
            matrix[naveC.getY()][naveC.getX()] = new Vacia(naveC.getX(), naveC.getY());
            naveC.moverNave(nuevaX);
            matrix[naveC.getY()][naveC.getX()] = naveC;
            actualizarTablero();
        }
    }

    public void moverNaveV(int direccion) {

        int nuevaY = naveC.getY() + direccion;
        if (dentroRango(naveC.getX(), nuevaY)) {
            matrix[naveC.getY()][naveC.getX()] = new Vacia(naveC.getX(), naveC.getY());
            naveC.moverNaveV(nuevaY);
            matrix[naveC.getY()][naveC.getX()] = naveC;
            actualizarTablero();

        }

    }

    private boolean dentroRango(int x, int y) {
        boolean dentro = false;
        if (x >= 0 && x < width && y >= 0 && y < height) {
            dentro = true;
        }
        return dentro;
    }

    public void disparar() {
        if (naveC != null) {
            naveC.disparar();
        }
    }

    public void actualizarPosicionDisparo(int oldX, int oldY, Disparo d) {
        if (matrix[oldY][oldX] instanceof Disparo) {
            matrix[oldY][oldX] = new Vacia(oldX, oldY);
        }
        if (d.getY() >= 0 && d.getY() < height) {
            matrix[d.getY()][d.getX()] = d;
        }
        actualizarTablero();
    }

    public void eliminarDisparo(int x, int y) {
        if (y >= 0 && y < height && x >= 0 && x < width) {
            if (matrix[y][x] instanceof Disparo) {
                matrix[y][x] = new Vacia(x, y);
            }
        }
        actualizarTablero();
    }
}

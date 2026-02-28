package Model;

import java.util.Observable;

public class GameBoard extends Observable{
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Casilla[][] matrix;

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
    	for (int i = 0; i<height;i++) {
    		for (int j = 0; j<width;j++) {
    			Casilla c = new Casilla(j,i);
    			matrix[i][j] = c;
    		}
    	}
    	setChanged();
    	//Se crea el tablero inicializando la nave y creando los enemigos
    	notifyObservers(new int[] {1});
    }
}

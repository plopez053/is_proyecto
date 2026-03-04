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
            	Casilla c = new Casilla (j,i);
            	matrix[i][j] = c;
            	if (i == posYInicio && j == posXInicio) {
            		naveC = new Nave(j,i); // Crea un objeto que guarde las coordenadas de la nave
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
        // Se crea el tablero inicializando la nave y creando los enemigos
        notifyObservers(new int[] {1,naveC.getY(),naveC.getX()});
    }

    public void actualizarTablero() {
        setChanged();
        notifyObservers(new int[] {naveC.getY(),naveC.getX()});
    }
    
    public void moverNave(int direccion) {

        int nuevaX = naveC.getX() + direccion;

        
        if (dentroRango(nuevaX,naveC.getY())) {
        	matrix[naveC.getY()][naveC.getX()] = new Vacia(naveC.getX(), naveC.getY());
        	naveC.moverNave(nuevaX);
        	matrix[naveC.getY()][naveC.getX()] = naveC;
            actualizarTablero();
        }
    }
    public void moverNaveV(int direccion) {

        int nuevaY = naveC.getY() + direccion;
        if (dentroRango(naveC.getX(),nuevaY)) {
        	matrix[naveC.getY()][naveC.getX()] = new Vacia(naveC.getX(), naveC.getY());
        	naveC.moverNaveV(nuevaY);
        	matrix[naveC.getY()][naveC.getX()] = naveC;
        	actualizarTablero();
        	
        }
        
    }
    
    private boolean dentroRango(int x, int y) {
    	boolean dentro = false;
    	if (x>=0 && x<width && y>=0 && y<height) {
    		dentro = true;
    	}
    	return dentro;
    }
}

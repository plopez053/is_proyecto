package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends Observable {
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Pixel[][] matrix;
    private int posXInicio = 50;
    private int posYInicio = 55;
    private Timer timer;

    private GameBoard() {
        matrix = new Pixel[height][width];
        for (int i=0;i<height;i++) {
        	for(int j=0;j<width;j++) {
        		Pixel p = new Pixel(j,i,new casillaVacia());
        		matrix[i][j] = p;
        	}
        }
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

    public Pixel getCasilla(int x, int y) {
        if (esPosicionValida(x, y)) {
            return matrix[y][x];
        }
        return null;
    }

    public void setCasilla(int x, int y, Pixel casilla) {
        if (esPosicionValida(x, y)) {
            matrix[y][x] = casilla;
        }
    }

    public void clearBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //matrix[i][j] = new Vacia(j, i);
            	matrix[i][j].cambiarEstado(new casillaVacia());
            }
        }
    }

    public void crearTablero() {
        JugadorManager.getJugador().inicializarJugador(posXInicio, posYInicio);
        EnemigoManager.getEnemigoManager().spawnEnemies();
        
        clearBoard();
        
        // Mostrar inicialmente las entidades
        Nave naveC = JugadorManager.getJugador().getNave();
        if (naveC != null && naveC.getCuerpo() != null) {
            for (Pixel p : naveC.getCuerpo().getCasillasOcupadas()) {
                    setCasilla(p.getX(), p.getY(), p);
            }
        }
        
        for (Enemigo e : EnemigoManager.getEnemigoManager().getEnemigos()) {
            if (e.getNave().getCuerpo() != null) {
                for (Pixel p : e.getNave().getCuerpo().getCasillasOcupadas()) {
                        setCasilla(p.getX(), p.getY(), p);
                }
            }
        }
        iniciarTimer();
        setChanged();
        notifyObservers(new Object[] { 1, getBoardActual() });
    }

    public void actualizarTablero() {
        setChanged();
        notifyObservers(new Object[] { 0, getBoardActual() });
    }

    private int[][] getBoardActual() {
        int[][] snapshot = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Pixel p = matrix[i][j];
                if (p.esEnemigo()) {
                    snapshot[i][j] = 0;
                } else if (p.esNave()) {
                    Nave naveInfo = JugadorManager.getJugador().getNave();
                    if (naveInfo != null && !naveInfo.estaViva()) {
                    	snapshot[i][j] = 3; // Antes era 0, lo que hacía que se dibujara verde
                    } else {
                        snapshot[i][j] = 1; 
                    }
                } else if (p.esDisparo()) {
                    snapshot[i][j] = 2;
                } else {
                	snapshot[i][j] = 3;
                }
            }
        }
        return snapshot;
    }

    public boolean esPosicionValida(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void actualizarPosicion(int viejaX, int viejaY, Pixel casilla) {
        if (esPosicionValida(viejaX, viejaY)) {
            // Si la casilla antigua aún sigue apuntando al mismo objeto, la vaciamos
            if (matrix[viejaY][viejaX] == casilla) {
                matrix[viejaY][viejaX].cambiarEstado(new casillaVacia());;
            }
        }
        if (esPosicionValida(casilla.getX(), casilla.getY())) {
            // Sobre-escribe la nueva posicion (sin lógica de colisiones final aún)
            matrix[casilla.getY()][casilla.getX()] = casilla;
        }
    }

    public void actualizarPosicionDisparo(int oldX, int oldY, Disparo d) {
        if (esPosicionValida(oldX, oldY)) {
            if (matrix[oldY][oldX].esDisparo()) { //Se marca como casilla vacía
                matrix[oldY][oldX].cambiarEstado(new casillaVacia());
            }
        }
        if (esPosicionValida(d.getX(), d.getY())) {
            matrix[d.getY()][d.getX()].cambiarEstado(new casillaDisparo());;
        }
        actualizarTablero();
    }

    public void eliminarDisparo(int x, int y) {
        if (esPosicionValida(x, y)) {
            if (matrix[y][x].esDisparo()) {
                matrix[y][x].cambiarEstado(new casillaVacia());;
            }
        }
        actualizarTablero();
    }

    public void iniciarTimer() {
        detenerTimer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                moverEnemigos();
            }
        };
        timer = new Timer();
        timer.schedule(task, 1000, 200);
    }

    public void detenerTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void evaluarEstadoJuego() {
        Nave naveC = JugadorManager.getJugador().getNave();
        if (naveC != null && !naveC.estaViva()) {
            detenerTimer();
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        }
    }

    private void moverEnemigos() {
        Nave naveC = JugadorManager.getJugador().getNave();
        EnemigoManager.getEnemigoManager().moveEnemies();

        boolean fin = false;
        for (Enemigo e : EnemigoManager.getEnemigoManager().getEnemigos()) {
        	for (Pixel p: e.getNave().getCuerpo().getCasillasOcupadas()) { //Se recorre la lista de casillas de cada enemigo
        		 if (p.getY() >= height) {
 	                fin = true;
 	                break;
 	            }
        	}    
        }
        
        if (naveC != null && !naveC.estaViva()) {
            fin = true;
        }

        if (fin) {
            detenerTimer();
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        } else {
            actualizarTablero();
        }
    }
}

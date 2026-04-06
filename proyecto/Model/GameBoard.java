package Model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoard extends Observable {
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Casilla[][] matrix;
    private int posXInicio = 50;
    private int posYInicio = 55;
    private Timer timer;

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
        if (esPosicionValida(x, y)) {
            return matrix[y][x];
        }
        return null;
    }

    public void setCasilla(int x, int y, Casilla casilla) {
        if (esPosicionValida(x, y)) {
            matrix[y][x] = casilla;
        }
    }

    public void clearBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = new Vacia(j, i);
            }
        }
    }

    public void crearTablero() {
        JugadorManager.getInstance().inicializarJugador(posXInicio, posYInicio);
        EnemigoManager.getEnemigoManager().spawnEnemies();
        
        clearBoard();
        
        // Mostrar inicialmente las entidades
        Nave naveC = JugadorManager.getInstance().getNave();
        if (naveC != null && naveC.getCuerpo() != null) {
            for (Casilla c : naveC.getCuerpo().getCasillasOcupadas()) {
                if (c instanceof Pixel) {
                    setCasilla(c.getX(), c.getY(), c);
                }
            }
        }
        
        for (Enemigo e : EnemigoManager.getEnemigoManager().getEnemigos()) {
            if (e.getNave().getCuerpo() != null) {
                for (Casilla c : e.getNave().getCuerpo().getCasillasOcupadas()) {
                    if (c instanceof Pixel) {
                        setCasilla(c.getX(), c.getY(), c);
                    }
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
                Casilla c = matrix[i][j];
                if (c instanceof Pixel) {
                    Pixel p = (Pixel) c;
                    if (p.getEntityType() == 0) {
                        snapshot[i][j] = 0;
                    } else if (p.getEntityType() == 1) {
                        Nave naveInfo = JugadorManager.getInstance().getNave();
                        if (naveInfo != null && !naveInfo.estaViva()) {
                            snapshot[i][j] = 1; // Antes era 0, lo que hacía que se dibujara verde
                        } else {
                            snapshot[i][j] = 1; 
                        }
                    }
                } else if (c instanceof Disparo) {
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

    public void actualizarPosicion(int viejaX, int viejaY, Casilla casilla) {
        if (esPosicionValida(viejaX, viejaY)) {
            // Si la casilla antigua aún sigue apuntando al mismo objeto, la vaciamos
            if (matrix[viejaY][viejaX] == casilla) {
                matrix[viejaY][viejaX] = new Vacia(viejaX, viejaY);
            }
        }
        if (esPosicionValida(casilla.getX(), casilla.getY())) {
            // Sobre-escribe la nueva posicion (sin lógica de colisiones final aún)
            matrix[casilla.getY()][casilla.getX()] = casilla;
        }
    }

    public void actualizarPosicionDisparo(int oldX, int oldY, Disparo d) {
        if (esPosicionValida(oldX, oldY)) {
            if (matrix[oldY][oldX] instanceof Disparo) {
                matrix[oldY][oldX] = new Vacia(oldX, oldY);
            }
        }
        if (esPosicionValida(d.getX(), d.getY())) {
            matrix[d.getY()][d.getX()] = d;
        }
        actualizarTablero();
    }

    public void eliminarDisparo(int x, int y) {
        if (esPosicionValida(x, y)) {
            if (matrix[y][x] instanceof Disparo) {
                matrix[y][x] = new Vacia(x, y);
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
        Nave naveC = JugadorManager.getInstance().getNave();
        if (naveC != null && !naveC.estaViva()) {
            detenerTimer();
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        }
    }

    private void moverEnemigos() {
        Nave naveC = JugadorManager.getInstance().getNave();
        EnemigoManager.getEnemigoManager().moveEnemies();

        boolean fin = false;
        for (Enemigo e : EnemigoManager.getEnemigoManager().getEnemigos()) {
            if (e.getNave().getY() >= height) {
                fin = true;
                break;
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

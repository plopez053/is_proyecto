package Model;

import java.util.Observable;

public class GameBoard extends Observable {
    private static GameBoard miGameBoard;
    private final int width = 100;
    private final int height = 60;
    private Pixel[][] matrix;
    private int posXInicio = 50;
    private int posYInicio = 55;
    private boolean juegoFinalizado = false;
    private String tipoNave;

    private GameBoard() {
        matrix = new Pixel[height][width];
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

    public Pixel getPixel(int x, int y) {
        if (esPosicionValida(x, y)) {
            return matrix[y][x];
        }
        return null;
    }

    // Para compatibilidad con código de compañeros
    public Pixel getCasilla(int x, int y) {
        return getPixel(x, y);
    }

    public void setPixel(int x, int y, Pixel p) {
        if (esPosicionValida(x, y)) {
            matrix[y][x] = p;
        }
    }

    // Para compatibilidad con código de compañeros
    public void setCasilla(int x, int y, Pixel p) {
        setPixel(x, y, p);
    }

    public void clearBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = null;
            }
        }
    }

    public void crearTablero() {
        clearBoard();
        juegoFinalizado = false;
        JugadorManager.getJugador().setTipoNave(tipoNave);
        JugadorManager.getInstance().inicializarJugador(posXInicio, posYInicio);
        EnemigoManager.getEnemigoManager().spawnEnemies();

        EnemigoManager.getEnemigoManager().iniciarTimerEnemigos();

        setChanged();
        notifyObservers(new Object[] { 1, getBoardActual() });
    }

    public void actualizarTablero() {
        if (!juegoFinalizado) {
            setChanged();
            notifyObservers(new Object[] { 0, getBoardActual() });
        }
    }

    private int[][] getBoardActual() {
        int[][] snapshot = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Pixel p = matrix[i][j];
                if (p == null) {
                    snapshot[i][j] = 3;
                } else if (p.esDisparo()) {
                    snapshot[i][j] = 2;
                } else if (p.esEnemigo()) {
                    snapshot[i][j] = 0;
                } else if (p.esNave()) {
                	String tipoN = getTipoNave();
                	if("BUENO_RED".equals(tipoN)) {
                		snapshot[i][j] = 1;
                	}else if("BUENO_GREEN".equals(tipoN)) {
                		snapshot[i][j] = 4;
                	}else if ("BUENO_BLUE".equals(tipoN)) {
                		snapshot[i][j] = 5;
                	}else {
                    snapshot[i][j] = 1; //caso default
                	}
                } else {
                    snapshot[i][j] = 3;
                }
            }
        }
        return snapshot;
    }

    public boolean gestionarColision(Pixel movido, Pixel ocupante) {
        if (ocupante == null) return false;

        // Caso 1: Enemigo choca con Disparo
        if (movido.esEnemigo() && ocupante.esDisparo()) {
            Object bullet = ocupante.getOwner();
            if (bullet instanceof Disparo) {
                JugadorManager.getInstance().eliminarDisparoActivo((Disparo) bullet);
            }
            EnemigoManager.getEnemigoManager().matarEnemigoEnCoordenada(movido.getX(), movido.getY());
            return true;
        }
        
        // Caso 2: Disparo choca con Enemigo
        if (movido.esDisparo() && ocupante.esEnemigo()) {
            EnemigoManager.getEnemigoManager().matarEnemigoEnCoordenada(ocupante.getX(), ocupante.getY());
            if (movido.getOwner() instanceof Disparo) {
                JugadorManager.getInstance().eliminarDisparoActivo((Disparo) movido.getOwner());
            }
            return true;
        }

        // Caso 3: Choque con Jugador (Game Over)
        if ((movido.esEnemigo() && ocupante.esNave()) || 
            (movido.esNave() && ocupante.esEnemigo())) {
            Nave n = JugadorManager.getInstance().getNave();
            if (n != null) n.removeNave();
            evaluarEstadoJuego();
            return true;
        }

        return false;
    }

    public boolean esPosicionValida(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void actualizarPosicion(int viejaX, int viejaY, Pixel p) {
        if (esPosicionValida(viejaX, viejaY)) {
            if (matrix[viejaY][viejaX] == p) {
                matrix[viejaY][viejaX] = null;
            }
        }
        if (esPosicionValida(p.getX(), p.getY())) {
            matrix[p.getY()][p.getX()] = p;
        }
    }

    public void evaluarEstadoJuego() {
        Nave naveC = JugadorManager.getInstance().getNave();
        if (naveC != null && !naveC.estaViva()) {
            juegoFinalizado = true;
            EnemigoManager.getEnemigoManager().detenerTimerEnemigos();
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        }
    }

    public void evaluarTickEnemigos() {
        Nave naveC = JugadorManager.getInstance().getNave();

        boolean fin = false;
        for (Enemigo e : EnemigoManager.getEnemigoManager().getEnemigos()) {
            if (e.getNave() != null) {
                for (Pixel p : e.getNave().getPixelesOcupados()) {
                    if (p.getY() >= height - 1) {
                        fin = true;
                        break;
                    }
                }
            }
            if (fin)
                break;
        }

        if (naveC != null && !naveC.estaViva()) {
            fin = true;
        }

        if (fin) {
            juegoFinalizado = true;
            EnemigoManager.getEnemigoManager().detenerTimerEnemigos();
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        } else {
            actualizarTablero();
        }
    }
    
    public void setTipoNave(String tipo) {
        this.tipoNave = tipo;
    }

	public String getTipoNave() {
		
		return this.tipoNave;
	}
}

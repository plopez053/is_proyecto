package Model;

import java.util.Observable;

/**
 * GameBoard (Mediator / Observable)
 * Centraliza el estado del tablero, detecta colisiones y notifica a la vista.
 */
public class GameBoard extends Observable {

    // --- Atributos Estáticos (Singleton) ---
    private static GameBoard miGameBoard;

    // --- Atributos de Instancia (Configuración y Estado) ---
    private final int width = 100;
    private final int height = 60;
    private Pixel[][] matrix;

    private int posXInicio = 50;
    private int posYInicio = 55;
    private String tipoNave;
    private boolean juegoFinalizado = false;

    // --- Constructor y Singleton ---
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

    // --- Getters y Setters Básicos ---
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean esJuegoFinalizado() {
        return juegoFinalizado;
    }

    public void setTipoNave(String tipo) {
        this.tipoNave = tipo;
    }

    public String getTipoNave() {
        return this.tipoNave;
    }

    // --- Gestión de la Matriz (Píxeles) ---
    public Pixel getPixel(int x, int y) {
        if (esPosicionValida(x, y))
            return matrix[y][x];
        return null;
    }

    public void setPixel(int x, int y, Pixel p) {
        if (esPosicionValida(x, y))
            matrix[y][x] = p;
    }


    // --- Ciclo de Vida y Control del Juego ---
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

        // Inicialización de Managers
        JugadorManager.getInstance().setTipoNave(tipoNave);
        JugadorManager.getInstance().inicializarJugador(posXInicio, posYInicio);
        EnemigoManager.getEnemigoManager().spawnEnemies();
        EnemigoManager.getEnemigoManager().iniciarTimerEnemigos();

        // Notificación inicial (Tipo 1: Setup)
        setChanged();
        notifyObservers(new Object[] { 1, getBoardActual() });
    }

    public void finalizarJuego() {
        if (!juegoFinalizado) {
            juegoFinalizado = true;
            EnemigoManager.getEnemigoManager().detenerTimerEnemigos();

            // Notificación final (Tipo 2: Game Over)
            setChanged();
            notifyObservers(new Object[] { 2, getBoardActual() });
        }
    }

    public void actualizarTablero() {
        if (!juegoFinalizado) {
            setChanged();
            notifyObservers(new Object[] { 0, getBoardActual() });
        }
    }

    /**
     * Orquesta la destrucción iniciada por un `Pixel`.
     * Ejecuta el impacto (delegado al Estado), permite que los managers
     * hagan las eliminaciones necesarias y finalmente notifica a la vista.
     */
    public void procesarDestruccionDesdePixel(Pixel p) {
        synchronized (this) {
            // Delegar la lógica de impacto (el Estado y los managers harán el resto)
            p.impactar();

            // Una vez completada la lógica de destrucción/borrado, actualizar vista
            actualizarTablero();
        }
    }

    /** Publica un evento de destrucción a los observers (managers, etc.). */
    public void publishDestruction(Pixel p) {
        setChanged();
        // Notificamos directamente el Pixel (los observers comprobarán el owner
        // a través del Composite padre).
        notifyObservers(p);
    }

    // --- Lógica de Juego (Colisiones y Movimiento) ---

    /**
     * Actúa como mediador de colisiones.
     * Retorna true si hay colisión (frena el movimiento del 'movido').
     */
    public boolean gestionarColision(Pixel movido, Pixel ocupante) {
        // 1. Validaciones previas
        if (ocupante == null || !ocupante.getEstado().estaOcupada())
            return false;
        Composite pm = movido.getParentComposite();
        Composite po = ocupante.getParentComposite();
        if (pm != null && po != null) {
            Object ownerMov = pm.getOwner();
            Object ownerOcc = po.getOwner();
            if (ownerMov != null && ownerMov == ownerOcc)
                return false;
        }

        // Regla especial: disparos se ignoran entre sí
        EstadoCasilla.TipoCasilla tipoMovido = movido.getEstado().getTipo();
        EstadoCasilla.TipoCasilla tipoOcupante = ocupante.getEstado().getTipo();

        if (tipoMovido == EstadoCasilla.TipoCasilla.DISPARO && tipoOcupante == EstadoCasilla.TipoCasilla.DISPARO)
            return false;

        // 2. Detección de estado previo (para fin de juego)
        boolean naveInvolucrada = (tipoMovido == EstadoCasilla.TipoCasilla.NAVE) || (tipoOcupante == EstadoCasilla.TipoCasilla.NAVE);

        // 3. Ejecución de la destrucción (la actualización la realizará GameBoard)
        ocupante.procesarDestruccion();

        // 4. Consecuencias de la colisión
        if (naveInvolucrada) {
            finalizarJuego();
        }

        return true;
    }

    public void actualizarPosicion(int viejaX, int viejaY, Pixel p) {
        // Borrar de la posición antigua
        if (esPosicionValida(viejaX, viejaY)) {
            if (matrix[viejaY][viejaX] == p) {
                matrix[viejaY][viejaX] = null;
            }
        }

        // Colocar en la nueva posición
        if (esPosicionValida(p.getX(), p.getY())) {
            matrix[p.getY()][p.getX()] = p;

            // Condición de derrota: enemigo llega al fondo
            if (p.getEstado().getTipo() == EstadoCasilla.TipoCasilla.ENEMIGO && p.getY() >= height - 1) {
                finalizarJuego();
            }
        }
    }

    public boolean esPosicionValida(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Genera un volcado numérico del tablero para que la vista lo dibuje.
     */
    private int[][] getBoardActual() {
        int[][] snapshot = new int[height][width];
        String tipoN = getTipoNave();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Pixel p = matrix[i][j];

                EstadoCasilla.TipoCasilla tipo = (p == null) ? EstadoCasilla.TipoCasilla.VACIA : p.getEstado().getTipo();

                if (tipo == EstadoCasilla.TipoCasilla.VACIA) {
                    snapshot[i][j] = 3; // Vacío
                } else if (tipo == EstadoCasilla.TipoCasilla.DISPARO) {
                    snapshot[i][j] = 2; // Disparo
                } else if (tipo == EstadoCasilla.TipoCasilla.ENEMIGO) {
                    snapshot[i][j] = 0; // Enemigo
                } else if (tipo == EstadoCasilla.TipoCasilla.NAVE) {
                    // Colores de la nave del jugador
                    if ("BUENO_RED".equals(tipoN))
                        snapshot[i][j] = 1;
                    else if ("BUENO_GREEN".equals(tipoN))
                        snapshot[i][j] = 4;
                    else if ("BUENO_BLUE".equals(tipoN))
                        snapshot[i][j] = 5;
                    else
                        snapshot[i][j] = 1;
                } else {
                    snapshot[i][j] = 3;
                }
            }
        }
        return snapshot;
    }
}

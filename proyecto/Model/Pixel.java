package Model;

import java.util.Collections;
import java.util.List;

/**
 * CONTEXTO del patrón State.
 * Equivale a Square en minesWeeper o Vending en vending.
 * Tiene changeState() y delega las acciones al estado actual.
 */
public class Pixel implements Entidad {
    protected int x;
    protected int y;
    protected EstadoCasilla estado;
    private Destructible owner;

    public Pixel(int x, int y, EstadoCasilla estado) {
        this.x = x;
        this.y = y;
        this.estado = estado;
    }

    public Destructible getOwner() {
        return owner;
    }

    public void setOwner(Destructible owner) {
        this.owner = owner;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public EstadoCasilla getEstado() { return estado; }
    public void setEstado(EstadoCasilla estado) { this.estado = estado; }

    /**
     * changeState: el núcleo del patrón State.
     */
    public void changeState(EstadoCasilla nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     * impactar: delega al estado actual (patrón State puro).
     */
    public void impactar() {
        this.estado.impactar(this);
    }

    public boolean esEnemigo()   { return estado.esEnemigo(); }
    public boolean esNave()      { return estado.esNave(); }
    public boolean esDisparo()   { return estado.esDisparo(); }
    public boolean estaOcupada() { return estado.estaOcupada(); }

    @Override
    public boolean canMove(int dx, int dy) {
        return GameBoard.getGameBoard().esPosicionValida(x + dx, y + dy);
    }

    @Override
    public void dibujar(GameBoard gb) {
        synchronized (gb) {
            gb.setPixel(x, y, this);
        }
    }

    @Override
    public void borrar(GameBoard gb) {
        synchronized (gb) {
            if (gb.getPixel(x, y) == this) {
                gb.setPixel(x, y, null);
            }
        }
    }

    @Override
    public void mover(int dx, int dy) {
        int oldX = getX();
        int oldY = getY();
        int newX = oldX + dx;
        int newY = oldY + dy;

        GameBoard board = GameBoard.getGameBoard();

        if (!board.esPosicionValida(newX, newY)) return;

        Pixel ocupante = board.getPixel(newX, newY);

        if (board.gestionarColision(this, ocupante)) {
            // El Pixel delega en el estado para lógica de negocio (avisar owner, cambiar estado)
            this.impactar();
            // El PÍXEL (Contexto) se encarga de la limpieza física del tablero
            this.borrar(board);
            return;
        }

        setX(newX);
        setY(newY);
        board.actualizarPosicion(oldX, oldY, this);
        board.actualizarTablero();
    }

    @Override
    public List<Pixel> getPixelesOcupados() {
        return Collections.singletonList(this);
    }

    @Override
    public void procesarDestruccion() {
        // Delegamos al estado actual y nos borramos
        this.impactar();
        this.borrar(GameBoard.getGameBoard());
    }

    public List<Pixel> getCasillasOcupadas() {
        return getPixelesOcupados();
    }
}

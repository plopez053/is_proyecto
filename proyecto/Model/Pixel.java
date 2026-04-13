package Model;

import java.util.Collections;
import java.util.List;
import java.util.Observable;

/**
 * CONTEXTO del patrón State.
 * Equivale a Square en minesWeeper o Vending en vending.
 * Tiene changeState() y delega las acciones al estado actual.
 */
public class Pixel extends Observable implements Entidad {
    protected int x;
    protected int y;
    protected EstadoCasilla estado;
    

    public Pixel(int x, int y, EstadoCasilla estado) {
        this.x = x;
        this.y = y;
        this.estado = estado;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public EstadoCasilla getEstado() {
        return estado;
    }

    public void setEstado(EstadoCasilla estado) {
        this.estado = estado;
    }

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

    
    // o delegación comportamental (impactar).

    @Override
    public boolean canMove(int dx, int dy) {
        return GameBoard.getGameBoard().esPosicionValida(x + dx, y + dy);
    }

    @Override
    public void asignar() {
        GameBoard gb = GameBoard.getGameBoard();
        synchronized (gb) {
            gb.setPixel(x, y, this);
        }
    }

    @Override
    public void borrar() {
        GameBoard gb = GameBoard.getGameBoard();
        synchronized (gb) {
            // Defensive deletion: only clear if position valid and this instance
            // is the one currently stored at the coordinates.
            if (gb.esPosicionValida(x, y) && gb.getPixel(x, y) == this) {
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

        // If target goes out of bounds, the pixel suffers the impact
        if (!board.esPosicionValida(newX, newY)) {
            this.impactar();
            return;
        }

        Pixel ocupante = board.getPixel(newX, newY);

        if (board.gestionarColision(this, ocupante)) {
            // El PÍXEL sufre el impacto directamente
            this.impactar();
            return;
        }

        setX(newX);
        setY(newY);
        board.actualizarPosicion(oldX, oldY, this);
        // La actualización visual del tablero debe hacerse solo desde GameBoard tras movimientos completos.
    }

    @Override
    public List<Pixel> getPixelesOcupados() {
        return Collections.singletonList(this);
    }



    public void notificarDestruccion() {
        // Notificar a los observers registrados en ESTE píxel (managers) pasando solo coordenadas.
        setChanged();
        notifyObservers(new int[]{x, y});
    }


}

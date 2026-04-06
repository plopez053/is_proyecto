package Model;

import java.util.Collections;
import java.util.List;

public class Pixel extends Casilla implements Entidad {
    private int entityType; // 0 para Enemigo, 1 para Nave, 2 para Disparo

    public Pixel(int x, int y, int entityType) {
        super(x, y);
        this.entityType = entityType;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    @Override
    public void mover(int dx, int dy) {
        int oldX = getX();
        int oldY = getY();
        int newX = oldX + dx;
        int newY = oldY + dy;

        GameBoard board = GameBoard.getGameBoard();
        if (board.esPosicionValida(newX, newY)) {
            setX(newX);
            setY(newY);
            board.actualizarPosicion(oldX, oldY, this);
        }
    }

    @Override
    public List<Casilla> getCasillasOcupadas() {
        return Collections.singletonList(this);
    }
}

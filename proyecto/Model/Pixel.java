package Model;

import java.util.Collections;
import java.util.List;

public class Pixel implements Entidad {
    protected int x;
    protected int y;
    protected EstadoCasilla estado;
    private Object owner;

    public Pixel(int x, int y, EstadoCasilla estado) {
        this.x = x;
        this.y = y;
        this.estado = estado;
    }

    public Object getOwner() { return owner; }
    public void setOwner(Object owner) { this.owner = owner; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public EstadoCasilla getEstado() {
        return estado;
    }

    public void setEstado(EstadoCasilla estado) {
        this.estado = estado;
    }

    public void cambiarEstado(EstadoCasilla pEstado) {
        this.estado = pEstado;
    }

    public boolean esEnemigo() { return estado.esEnemigo(); }
    public boolean esNave() { return estado.esNave(); }
    public boolean esDisparo() { return estado.esDisparo(); }
    public boolean estaOcupada() { return estado.estaOcupada(); }

    @Override
    public boolean canMove(int dx, int dy) {
        return GameBoard.getGameBoard().esPosicionValida(x + dx, y + dy);
    }

    @Override
    public void dibujar(GameBoard gb) {
        synchronized(gb) {
            gb.setPixel(x, y, this);
        }
    }

    @Override
    public void borrar(GameBoard gb) {
        synchronized(gb) {
            gb.setPixel(x, y, null);
        }
    }

    @Override
    public void mover(int dx, int dy) {
        int oldX = getX();
        int oldY = getY();
        int newX = oldX + dx;
        int newY = oldY + dy;

        GameBoard board = GameBoard.getGameBoard();
        Pixel ocupante = board.getPixel(newX, newY);
        
        // Delegamos la colisión al GameBoard
        if (board.gestionarColision(this, ocupante)) {
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
    
    // Método para compatibilidad con código de compañeros que use getCasillasOcupadas
    public List<Pixel> getCasillasOcupadas() {
        return getPixelesOcupados();
    }
}

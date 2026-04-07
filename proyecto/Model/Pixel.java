package Model;

import java.util.Collections;
import java.util.List;

public class Pixel implements Entidad {
	private int x, y;
    private EstadoCasilla estado; // Patron state

    public Pixel(int pX, int pY, EstadoCasilla pEstado) {
    	x = pX;
    	y = pY;
        this.estado = pEstado;
    }

    public void cambiarEstado(EstadoCasilla pEstado) {
    	this.estado = pEstado;
    	
    }
    public boolean estaOcupada() {
    	return estado.estaOcupada();
    }
    
    public boolean esNave() {
    	return estado.esNave();
    }
    
    public boolean esEnemigo() {
    	return estado.esEnemigo();
    }
    
    public boolean esDisparo() {
    	return estado.esDisparo();
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

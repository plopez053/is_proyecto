package Model;

import java.util.Timer;
import java.util.TimerTask;

public class Disparo implements Entidad {
    private int x, y;
    private Timer timer;
    private Nave owner;

    public Disparo(int x, int y, Nave owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        iniciarMovimiento();
    }

    private void iniciarMovimiento() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mover();
            }
        }, 0, 50);
    }

    private void mover() {
        GameBoard board = GameBoard.getGameBoard();
        // Limpiar posición actual
        Casilla actual = board.getCasilla(x, y);
        if (actual != null && actual.getContenido() == this) {
            actual.setContenido(null);
        }

        y--;

        if (y < 0) {
            detener();
            return;
        }

        Casilla nueva = board.getCasilla(x, y);
        if (nueva != null) {
            Entidad contenido = nueva.getContenido();
            if (contenido instanceof Enemigo) {
                // Colisión con enemigo
                board.eliminarEnemigoEn(x, y);
                detener();
            } else {
                nueva.setContenido(this);
                board.actualizarTablero();
            }
        }
    }

    public void detener() {
        if (timer != null) {
            timer.cancel();
        }
        owner.eliminarDisparo(this);
        GameBoard board = GameBoard.getGameBoard();
        Casilla c = board.getCasilla(x, y);
        if (c != null && c.getContenido() == this) {
            c.setContenido(null);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

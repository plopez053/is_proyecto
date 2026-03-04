package Model;

import java.util.Timer;
import java.util.TimerTask;

public class Disparo extends Casilla {
    private Timer timer;

    public Disparo(int x, int y) {
        super(x, y);
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
        GameBoard gb = GameBoard.getGameBoard();
        int oldX = getX();
        int oldY = getY();
        int newY = oldY - 1;

        if (newY < 0) {
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Comprobar colisión
        Casilla ocupante = gb.getCasilla(oldX, newY);
        if (ocupante instanceof Enemigo) {
            EnemigoManager.getEnemigoManager().removeEnemigo((Enemigo) ocupante);
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Actualizar posición en la matriz
        setY(newY);
        gb.actualizarPosicionDisparo(oldX, oldY, this);
    }

    private void detenerYBorrar(int x, int y) {
        if (timer != null) {
            timer.cancel();
        }
        GameBoard.getGameBoard().eliminarDisparo(x, y);
    }
}

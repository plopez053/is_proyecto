package Model;

import java.util.Timer;
import java.util.TimerTask;

public class Disparo {
    private Timer timer;

    public Disparo() {
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
        int oldX = getX(); //TODO creo que esto sobra, porque puede haber mas de un disparo y habría q actualizar todas
        int oldY = getY(); //TODO esto tb
        int newY = oldY - 1;

        if (newY < 0) {
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Comprobar colisión
        Pixel ocupante = gb.getCasilla(oldX, newY);
        if (ocupante.esEnemigo()) {
            EnemigoManager.getEnemigoManager().matarEnemigoEnCoordenada(ocupante.getX(), ocupante.getY());
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Actualizar posición en la matriz
        setY(newY);//TODO esto tb
        gb.actualizarPosicionDisparo(oldX, oldY, this);
    }

    private void detenerYBorrar(int x, int y) {
        if (timer != null) {
            timer.cancel();
        }
        GameBoard.getGameBoard().eliminarDisparo(x, y);
    }
}

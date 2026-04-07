package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JugadorManager {
    private static JugadorManager miJugador = null;
    private Bueno nave; // Ahora es Bueno
    private List<Disparo> activos;
    private Timer timerDisparos;

    private JugadorManager() {
        activos = new ArrayList<>();
    }

    public static JugadorManager getJugador() {
        if (miJugador == null) {
            miJugador = new JugadorManager();
        }
        return miJugador;
    }

    public void inicializarJugador(int x, int y) {
        // Casteamos porque sabemos que "Bueno" devuelve un Bueno
        nave = (Bueno) NaveFactory.getNaveFactory().crearNave("Bueno", x, y);
        activos.clear();
        iniciarTimerDisparos();
    }

    public Bueno getNave() {
        return nave;
    }

    public List<Disparo> getDisparosActivos() {
        return activos;
    }

    public void moverNave(int dx, int dy) {
        if (nave != null && nave.estaViva()) {
            nave.mover(dx, dy);
            if (!nave.estaViva()) {
                GameBoard.getGameBoard().evaluarEstadoJuego();
            }
        }
    }

    public void disparar() {
        if (nave != null && nave.estaViva()) {
            List<Disparo> nuevosDisparos = nave.disparar(); // Bueno tiene el método disparar
            if (nuevosDisparos != null) {
                activos.addAll(nuevosDisparos);
            }
        }
    }

    public void eliminarDisparoActivo(Disparo d) {
        if (activos.contains(d)) {
            activos.remove(d);
            GameBoard.getGameBoard().eliminarDisparo(d.getX(), d.getY());
        }
    }

    private void iniciarTimerDisparos() {
        if (timerDisparos != null) {
            timerDisparos.cancel();
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                moverDisparos();
            }
        };
        timerDisparos = new Timer();
        timerDisparos.schedule(task, 0, 50); // Mueve los disparos cada 50ms
    }

    private void moverDisparos() {
        List<Disparo> disparosParaEliminar = new ArrayList<>();
        GameBoard board = GameBoard.getGameBoard();

        for (Disparo d : activos) {
            int oldX = d.getX();
            int oldY = d.getY();
            int newY = oldY - 1; // El disparo sube

            if (newY < 0) {
                disparosParaEliminar.add(d);
                board.eliminarDisparo(oldX, oldY);
            } else {
                Casilla destino = board.getCasilla(oldX, newY);
                if (destino instanceof Pixel && ((Pixel) destino).getEntityType() == 0) {
                    EnemigoManager.getEnemigoManager().matarEnemigoEnCoordenada(oldX, newY);
                    disparosParaEliminar.add(d);
                    board.eliminarDisparo(oldX, oldY);
                } else {
                    d.setY(newY);
                    board.actualizarPosicionDisparo(oldX, oldY, d);
                }
            }
        }
        activos.removeAll(disparosParaEliminar);
    }
}

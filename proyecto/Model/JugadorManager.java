package Model;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

public class JugadorManager {
    private static JugadorManager instance;
    private Nave nave;
    private List<Disparo> disparosActivos;
    private Timer timerDisparos;

    private JugadorManager() {
        disparosActivos = new ArrayList<>();
    }

    public static JugadorManager getInstance() {
        if (instance == null) {
            instance = new JugadorManager();
        }
        return instance;
    }

    public void inicializarJugador(int x, int y) {
        nave = NaveFactory.getInstance().crearNave("Bueno", x, y);
        disparosActivos = new ArrayList<>();
        if (nave != null && nave.getCuerpo() != null) {
            nave.getCuerpo().dibujar(GameBoard.getGameBoard());
        }
        iniciarTimerDisparos();
    }

    public Nave getNave() {
        return nave;
    }


    public List<Disparo> getDisparosActivos() {
        return disparosActivos;
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
            List<Disparo> nuevosDisparos = nave.disparar();
            if (nuevosDisparos != null) {
                disparosActivos.addAll(nuevosDisparos);
                for (Disparo d : nuevosDisparos) {
                    d.dibujar(GameBoard.getGameBoard());
                }
            }
        }
    }

    public void eliminarDisparoActivo(Disparo d) {
        if (disparosActivos != null) {
            disparosActivos.remove(d);
            d.borrar(GameBoard.getGameBoard());
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
        timerDisparos.schedule(task, 0, 50);
    }

    private void moverDisparos() {
        List<Disparo> copia = new ArrayList<>(disparosActivos);
        for (Disparo d : copia) {
            d.mover(0, -1);
        }
    }
}

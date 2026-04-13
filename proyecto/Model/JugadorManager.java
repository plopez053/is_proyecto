package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class JugadorManager implements Observer {
    private static JugadorManager instance;
    private Nave nave;
    private List<Disparo> disparosActivos;
    private Timer timerDisparos;
    private String tipoNave;

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
        if (tipoNave == null) {
            tipoNave = "BUENO_RED"; // default
        }
        nave = NaveFactory.getInstance().crearNave(tipoNave, x, y);
        disparosActivos = new ArrayList<>();
        if (nave != null && nave.getCuerpo() != null) {
            // Registrar píxeles del cuerpo en este manager antes de asignar en el tablero
            registerComposite(nave.getCuerpo());
            nave.getCuerpo().asignar();
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
            evaluarEstadoJuego();
            GameBoard.getGameBoard().actualizarTablero(); // Repintado único
        }
    }

    public void evaluarEstadoJuego() {
        // La lógica de fin de juego se gestiona ahora de forma reactiva en GameBoard
    }

    public void notificarDestruccionNave() {
        if (nave != null && nave.estaViva()) {
            nave.removeNave();
            if (nave.getCuerpo() != null) {
                nave.getCuerpo().borrar();
            }
            // El fin de juego se gestiona en GameBoard tras la colisión
        }
    }

    public void disparar() {
        if (nave != null && nave.estaViva()) {
            List<Disparo> nuevosDisparos = nave.disparar();
            if (nuevosDisparos != null) {
                disparosActivos.addAll(nuevosDisparos);
                for (Disparo d : nuevosDisparos) {
                    d.asignar();
                    // Registrar píxeles del disparo en este manager
                    registerDisparo(d);
                }
            }
        }
    }

    public void eliminarDisparoActivo(Disparo d) {
        if (disparosActivos != null && disparosActivos.contains(d)) {
            disparosActivos.remove(d);
            d.markAsDestroyed();
            d.borrar();
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
            // El movimiento del proyectil ahora disparará la lógica de colisiones en
            // GameBoard
            d.mover(0, -1);
        }
        GameBoard.getGameBoard().actualizarTablero(); // Repintado por lote (Batch)
    }

    public void setTipoNave(String tipo) {
        this.tipoNave = tipo;
    }

    /**
     * Registrar todos los píxeles de un Composite para que este manager reciba
     * notificaciones de destrucción desde los píxeles.
     */
    public void registerComposite(Composite c) {
        if (c == null)
            return;
        for (Pixel p : c.getPixelesOcupados()) {
            if (p != null)
                p.addObserver(this);
        }
    }

    /** Registrar un solo Disparo (sus píxeles) */
    public void registerDisparo(Disparo d) {
        if (d == null || d.getCuerpo() == null)
            return;
        registerComposite(d.getCuerpo());
    }

    public void cambiarArma() {
        if (nave != null && nave.estaViva()) {
            nave.cambiarArma();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof int[]) {
            int[] coords = (int[]) arg;
            int x = coords[0];
            int y = coords[1];
            // Buscar y eliminar disparo activo que contenga ese pixel
            Disparo disparoAEliminar = null;
            for (Disparo d : disparosActivos) {
                if (d != null && d.getCuerpo() != null) {
                    for (Pixel px : d.getCuerpo().getPixelesOcupados()) {
                        if (px.getX() == x && px.getY() == y) {
                            disparoAEliminar = d;
                            break;
                        }
                    }
                }
                if (disparoAEliminar != null) break;
            }
            if (disparoAEliminar != null) {
                eliminarDisparoActivo(disparoAEliminar);
            }
            // Buscar si la nave tiene ese pixel
            if (nave != null && nave.getCuerpo() != null) {
                for (Pixel px : nave.getCuerpo().getPixelesOcupados()) {
                    if (px.getX() == x && px.getY() == y) {
                        notificarDestruccionNave();
                        break;
                    }
                }
            }
        }
    }
}

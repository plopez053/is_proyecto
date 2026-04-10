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

    // Alias para compatibilidad con el código de los compañeros
    public static JugadorManager getJugador() {
        return getInstance();
    }

    public void inicializarJugador(int x, int y) {
        if (tipoNave == null) {
            tipoNave = "BUENO_RED"; // default
        }
        nave = NaveFactory.getInstance().crearNave(tipoNave, x, y);
        disparosActivos = new ArrayList<>();
        if (nave != null && nave.getCuerpo() != null) {
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
    }

    public void setTipoNave(String tipo) {
        this.tipoNave = tipo;
    }

    public void cambiarArma() {
        if (nave instanceof Bueno && nave.estaViva()) {
            ((Bueno) nave).cambiarArma();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Pixel) {
            Pixel p = (Pixel) arg;
            Composite parent = p.getParentComposite();
            Object owner = (parent != null) ? parent.getOwner() : null;
            if (owner instanceof Disparo) {
                eliminarDisparoActivo((Disparo) owner);
            } else if (owner instanceof Nave) {
                if (nave != null && nave == owner) {
                    notificarDestruccionNave();
                }
            }
        }
    }
}

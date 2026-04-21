package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class JugadorManager implements Observer {
    private static JugadorManager miJugadorManager;
    private Nave nave;
    private List<Composite> disparosActivos;
    private Timer timerDisparos;
    private String tipoNave;

    private JugadorManager() {
        disparosActivos = new ArrayList<>();
    }

    public static JugadorManager getJugadorManager() {
        if (miJugadorManager == null) {
            miJugadorManager = new JugadorManager();
        }
        return miJugadorManager;
    }

    public void inicializarJugador(int x, int y) {
        if (tipoNave == null) {
            tipoNave = "BUENO_RED"; // default
        }
        nave = NaveFactory.getNaveFactory().crearNave(tipoNave, x, y);
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

    public List<Composite> getDisparosActivos() {
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
            Composite nuevoDisparo = nave.disparar();
            if (nuevoDisparo != null) {
                disparosActivos.add(nuevoDisparo);
                registerComposite(nuevoDisparo);
                nuevoDisparo.asignar();
            }
        }
    }

    public void eliminarDisparoActivo(Composite c) {
        if (disparosActivos != null && disparosActivos.contains(c)) {
            disparosActivos.remove(c);
            c.borrar();
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
        List<Composite> copia = new ArrayList<>(disparosActivos);
        for (Composite c : copia) {
            // El movimiento del proyectil ahora disparará la lógica de colisiones en
            // GameBoard
            c.mover(0, -1);
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
    public void registerDisparo(Composite c) {
        if (c == null)
            return;
        registerComposite(c);
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
            Composite disparoAEliminar = null;
            for (Composite c : disparosActivos) {
                if (c != null) {
                    for (Pixel px : c.getPixelesOcupados()) {
                        if (px.getX() == x && px.getY() == y) {
                            disparoAEliminar = c;
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
    
    public String getNombreArmaActual() {
        if (nave instanceof Bueno) {
            Bueno bueno = (Bueno) nave;
            if (bueno.getArmaActual() != null) {
                return bueno.getArmaActual().getNombre();
            }
        }
        return "-";
    }

    public String getMunicionArmaActual() {
        if (nave instanceof Bueno) {
            Bueno bueno = (Bueno) nave;
            if (bueno.getArmaActual() != null) {
                int municion = bueno.getArmaActual().getMunicion();
                return municion == -1 ? "Municion Ilimitada" : String.valueOf(municion);
            }
        }
        return "-";
    }
}

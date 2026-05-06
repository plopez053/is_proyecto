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
        }
    }

    public void disparar() {
        if (nave != null && nave.estaViva()) {
            Composite nuevoDisparo = nave.disparar();
            if (nuevoDisparo != null) {
                disparosActivos.add(nuevoDisparo);
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
        new ArrayList<>(disparosActivos).stream().forEach(c -> c.mover(0, -1));
    }

    public void setTipoNave(String tipo) {
        this.tipoNave = tipo;
    }

    public void cambiarArma() {
        if (nave != null && nave.estaViva()) {
            nave.cambiarArma();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Object[]) {
            Object[] coords = (Object[]) arg;
            int px = (int) coords[0];
            int py = (int) coords[1];

            // Buscar y eliminar disparo activo que contenga ese pixel
            disparosActivos.stream()
                    .filter(c -> c != null && c.ocupaCoordenada(px, py))
                    .findFirst()
                    .ifPresent(this::eliminarDisparoActivo);

            // Buscar si la nave tiene ese pixel
            if (nave != null && nave.getCuerpo() != null) {
                if (nave.getCuerpo().ocupaCoordenada(px, py)) {
                    notificarDestruccionNave();
                }
            }
        }
    }

    public String getNombreArmaActual() {
        if (nave != null && nave.getArmaActual() != null) {
            return nave.getArmaActual().getNombre();
        }
        return "-";
    }

    public String getMunicionArmaActual() {
        if (nave != null && nave.getArmaActual() != null) {
            int municion = nave.getArmaActual().getMunicion();
            return municion == -1 ? "Municion Ilimitada" : String.valueOf(municion);
        }
        return "-";
    }
}

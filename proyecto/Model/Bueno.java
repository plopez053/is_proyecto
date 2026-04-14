package Model;

import java.util.ArrayList;
import java.util.List;

public class Bueno extends Nave {
    private List<EstrategiaDisparo> armasDisponibles = new ArrayList<>();
    private int armaIndice = 0;

    public Bueno(int x, int y) {
        super(x, y);
    }

    public void setArmasDisponibles(List<EstrategiaDisparo> armas) {
        this.armasDisponibles = armas;
        this.armaIndice = 0;
    }

    public void setArmaActual(EstrategiaDisparo arma) {
        // Para compatibilidad
        if (armasDisponibles.isEmpty()) {
            armasDisponibles.add(arma);
        } else {
            armasDisponibles.set(0, arma);
        }
    }

    public EstrategiaDisparo getArmaActual() {
        if (!armasDisponibles.isEmpty()) {
            return armasDisponibles.get(armaIndice);
        }
        return null;
    }

    public void cambiarArma() {
        if (armasDisponibles.size() > 1) {
            armaIndice = (armaIndice + 1) % armasDisponibles.size();
            System.out.println("Cambiando arma a: " + getArmaActual().getNombre());
        }
    }

    public Composite disparar() {
        EstrategiaDisparo arma = getArmaActual();
        if (arma != null) {
            // Si es un arma limitada y no tiene munición, intentar volver a la primera
            // (Pixel)
            if (arma.getMunicion() == 0) {
                armaIndice = 0;
                arma = getArmaActual();
            }
            return arma.disparar(getX(), getY());
        }
        return null;
    }

    public void notificarDestruccion() {
        JugadorManager.getInstance().notificarDestruccionNave();
    }
}

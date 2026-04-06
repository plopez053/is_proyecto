package Model;

import java.util.List;

public class Bueno extends Nave {
    private EstrategiaDisparo armaActual;

    public Bueno(int x, int y) {
        super(x, y);
    }

    public void setArmaActual(EstrategiaDisparo arma) {
        this.armaActual = arma;
    }

    public List<Disparo> disparar() {
        if (armaActual != null) {
            return armaActual.disparar(getX(), getY());
        }
        return null;
    }
}

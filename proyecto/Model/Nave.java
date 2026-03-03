package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Nave implements Entidad {
    // Aquí irán atributos comunes como vida, potencia, etc.
    protected List<Disparo> disparos = new ArrayList<>();
    protected int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void disparar() {
        Disparo d = new Disparo(this.x, this.y - 2, this);
        disparos.add(d);
    }

    public void eliminarDisparo(Disparo d) {
        disparos.remove(d);
    }

    public List<Disparo> getDisparos() {
        return disparos;
    }
}

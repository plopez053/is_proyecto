package Model;

import java.util.List;

public interface Entidad {
    void mover(int dx, int dy);
    List<Casilla> getCasillasOcupadas();
}

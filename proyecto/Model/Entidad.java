package Model;

import java.util.List;

public interface Entidad {
    public void mover(int dx, int dy);
    public List<Pixel> getCasillasOcupadas();
}

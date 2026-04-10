package Model;

import java.util.List;

public interface Entidad {
    void mover(int dx, int dy);

    boolean canMove(int dx, int dy);

    void asignar();

    void borrar();

    List<Pixel> getPixelesOcupados();



    // Alias para compatibilidad
    default List<Pixel> getCasillasOcupadas() {
        return getPixelesOcupados();
    }
}

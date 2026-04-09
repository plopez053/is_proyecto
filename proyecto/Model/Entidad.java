package Model;

import java.util.List;

public interface Entidad {
    void mover(int dx, int dy);

    boolean canMove(int dx, int dy);

    void dibujar(GameBoard gb);

    void borrar(GameBoard gb);

    List<Pixel> getPixelesOcupados();

    void procesarDestruccion();

    // Alias para compatibilidad
    default List<Pixel> getCasillasOcupadas() {
        return getPixelesOcupados();
    }
}

package Model;

public interface Entidad {
    void mover(int dx, int dy);

    boolean canMove(int dx, int dy);

    void dibujar(GameBoard gb);

    void borrar(GameBoard gb);

    java.util.List<Pixel> getPixelesOcupados();
}

package Model;

public class Casilla {
    private int x;
    private int y;
    private Entidad contenido;

    public Casilla(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Entidad getContenido() {
        return contenido;
    }

    public void setContenido(Entidad contenido) {
        this.contenido = contenido;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
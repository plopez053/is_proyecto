package Model;

public class Disparo {
    private Composite cuerpo;

    public Disparo(int x, int y) {
        this.cuerpo = new Composite();
        Pixel p = new Pixel(x, y, new casillaDisparo());
        p.setOwner(this);
        this.cuerpo.addComponente(p); // Proyectil
    }

    public int getX() {
        if (cuerpo != null && !cuerpo.getPixelesOcupados().isEmpty()) {
            return cuerpo.getPixelesOcupados().get(0).getX();
        }
        return -1;
    }

    public int getY() {
        if (cuerpo != null && !cuerpo.getPixelesOcupados().isEmpty()) {
            return cuerpo.getPixelesOcupados().get(0).getY();
        }
        return -1;
    }

    public void mover(int dx, int dy) {
        if (!canMove(dx, dy)) {
            JugadorManager.getInstance().eliminarDisparoActivo(this);
            return;
        }

        // El movimiento del cuerpo invocará Pixel.mover(), 
        // el cual delegará la colisión al GameBoard.
        cuerpo.mover(dx, dy);
    }

    public void dibujar(GameBoard gb) {
        if (cuerpo != null) {
            cuerpo.dibujar(gb);
        }
    }

    public void borrar(GameBoard gb) {
        if (cuerpo != null) {
            cuerpo.borrar(gb);
        }
    }

    public boolean canMove(int dx, int dy) {
        if (cuerpo != null) {
            return cuerpo.canMove(dx, dy);
        }
        return false;
    }

    public Composite getCuerpo() {
        return cuerpo;
    }
}

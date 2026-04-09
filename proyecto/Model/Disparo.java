package Model;

import java.util.Collections;
import java.util.List;

public class Disparo implements Destructible {
    private Composite cuerpo;
    private boolean destruido = false;

    public Disparo(int x, int y) {
        this.cuerpo = new Composite();
        Pixel p = new Pixel(x, y, new casillaDisparo());
        p.setOwner(this);
        this.cuerpo.addComponente(p);
    }

    public Disparo(int x, int y, int[][] desplazamientos) {
        this.cuerpo = new Composite();
        for (int[] dRelativo : desplazamientos) {
            Pixel p = new Pixel(x + dRelativo[0], y + dRelativo[1], new casillaDisparo());
            p.setOwner(this);
            this.cuerpo.addComponente(p);
        }
    }

    public int getX() {
        List<Pixel> pixeles = getPixelesOcupados();
        if (!pixeles.isEmpty()) {
            return pixeles.get(0).getX();
        }
        return -1;
    }

    public int getY() {
        List<Pixel> pixeles = getPixelesOcupados();
        if (!pixeles.isEmpty()) {
            return pixeles.get(0).getY();
        }
        return -1;
    }

    public void mover(int dx, int dy) {
        if (destruido) return;
        
        if (cuerpo != null && !cuerpo.canMove(dx, dy)) {
            procesarDestruccion();
            return;
        }

        if (cuerpo != null) {
            cuerpo.mover(dx, dy);
        }
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

    public List<Pixel> getPixelesOcupados() {
        if (cuerpo != null) {
            return cuerpo.getPixelesOcupados();
        }
        return Collections.emptyList();
    }

    public boolean estaVivo() {
        return !destruido;
    }

    @Override
    public void procesarDestruccion() {
        if (!destruido) {
            destruido = true;
            JugadorManager.getInstance().eliminarDisparoActivo(this);
        }
    }

    public Composite getCuerpo() {
        return cuerpo;
    }
}

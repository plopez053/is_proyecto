package Model;

import java.util.Collections;
import java.util.List;

public class Disparo {
    private Composite cuerpo;
    private boolean destruido = false;
    
    public void markAsDestroyed() {
        this.destruido = true;
    }

    public Disparo(int x, int y) {
        this.cuerpo = new Composite();
        Pixel p = new Pixel(x, y, new casillaDisparo());
        p.addObserver(JugadorManager.getInstance());
        this.cuerpo.addComponente(p);
        this.cuerpo.setOwner(this);
    }

    public Disparo(int x, int y, int[][] desplazamientos) {
        this.cuerpo = new Composite();
        for (int[] dRelativo : desplazamientos) {
            Pixel p = new Pixel(x + dRelativo[0], y + dRelativo[1], new casillaDisparo());
            p.addObserver(JugadorManager.getInstance());
            this.cuerpo.addComponente(p);
        }
        this.cuerpo.setOwner(this);
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
            destruido = true;
            JugadorManager.getInstance().eliminarDisparoActivo(this);
            return;
        }

        if (cuerpo != null) {
            cuerpo.mover(dx, dy);
        }
    }

    public void asignar() {
        if (cuerpo != null) {
            cuerpo.asignar();
        }
    }

    public void borrar() {
        if (cuerpo != null) {
            cuerpo.borrar();
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

    public Composite getCuerpo() {
        return cuerpo;
    }

    public void notificarDestruccion() {
        JugadorManager.getInstance().eliminarDisparoActivo(this);
    }
}

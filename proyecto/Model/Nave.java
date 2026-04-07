package Model;

import java.util.Collections;
import java.util.List;

public abstract class Nave {
    protected boolean viva = true;
    protected Composite cuerpo;

    public Nave(int x, int y) {
        // Inicializamos sin guardar x,y locales
    }

    public int getX() {
        List<Pixel> pixeles = getPixelesOcupados();
        if (!pixeles.isEmpty()) {
            return pixeles.get(0).getX();
        }
        return 0;
    }

    public int getY() {
        List<Pixel> pixeles = getPixelesOcupados();
        if (!pixeles.isEmpty()) {
            return pixeles.get(0).getY();
        }
        return 0;
    }

    public void setCuerpo(Composite cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Composite getCuerpo() {
        return cuerpo;
    }

    public void mover(int dx, int dy) {
        if (!viva)
            return;

        // Delegamos la validación al Composite (siguiendo el diagrama UML)
        if (cuerpo != null && !cuerpo.canMove(dx, dy)) {
            return;
        }

        if (cuerpo != null) {
            cuerpo.mover(dx, dy);
        }
    }

    public List<Pixel> getPixelesOcupados() {
        if (cuerpo != null) {
            return cuerpo.getPixelesOcupados();
        }
        return Collections.emptyList();
    }
    
    // Alias para compatibilidad con código de compañeros
    public List<Pixel> getCasillasOcupadas() {
        return getPixelesOcupados();
    }

    public void removeNave() {
        viva = false;
    }

    public boolean estaViva() {
        return viva;
    }

    public List<Disparo> disparar() {
        return null;
    }
}

package Model;

import java.util.ArrayList;
import java.util.List;

public class Composite implements Entidad {
    private List<Entidad> componentes = new ArrayList<>();
    private Object owner;

    public void addComponente(Entidad ev) {
        componentes.add(ev);
        if (ev instanceof Pixel) {
            ((Pixel) ev).setParentComposite(this);
        }
    }

    public void removeComponente(Entidad ev) {
        componentes.remove(ev);
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public Object getOwner() {
        return owner;
    }

    public List<Entidad> getComponentes() {
        return componentes;
    }

    @Override
    public boolean canMove(int dx, int dy) {
        for (Entidad ev : componentes) {
            if (ev != null && !ev.canMove(dx, dy)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void asignar() {

        for (Entidad ev : componentes) {
            if (ev != null)
                ev.asignar();
        }
    }

    @Override
    public void borrar() {
        for (Entidad ev : componentes) {
            if (ev != null)
                ev.borrar();
        }
    }

    @Override
    public void mover(int dx, int dy) {
        List<Entidad> copia = new ArrayList<>(componentes);
        for (Entidad ev : copia) {
            if (ev != null) {
                ev.mover(dx, dy);

                // Si tras mover este píxel el dueño (Nave o Disparo) ha muerto,
                // abortamos inmediatamente para no dejar píxeles fantasma.
                if (ev instanceof Pixel) {
                    Pixel p = (Pixel) ev;
                    Composite parent = p.getParentComposite();
                    Object owner = (parent != null) ? parent.getOwner() : null;

                    if (owner instanceof Nave) {
                        if (!((Nave) owner).estaViva())
                            break;
                    } else if (owner instanceof Disparo) {
                        if (!((Disparo) owner).estaVivo())
                            break;
                    }
                }
            }
        }
    }

    @Override
    public List<Pixel> getPixelesOcupados() {
        List<Pixel> ocupadas = new ArrayList<>();
        List<Entidad> copia = new ArrayList<>(componentes);
        for (Entidad ev : copia) {
            if (ev != null) {
                ocupadas.addAll(ev.getPixelesOcupados());
            }
        }
        return ocupadas;
    }

}

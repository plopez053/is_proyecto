package Model;

import java.util.ArrayList;
import java.util.List;

public class Composite implements Entidad {
    private List<Entidad> componentes = new ArrayList<>();

    public void addComponente(Entidad ev) {
        componentes.add(ev);
    }

    public void removeComponente(Entidad ev) {
        componentes.remove(ev);
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
    public void dibujar(GameBoard gb) {
        synchronized (gb) {
            for (Entidad ev : componentes) {
                if (ev != null)
                    ev.dibujar(gb);
            }
        }
    }

    @Override
    public void borrar(GameBoard gb) {
        synchronized (gb) {
            for (Entidad ev : componentes) {
                if (ev != null)
                    ev.borrar(gb);
            }
        }
    }

    @Override
    public void mover(int dx, int dy) {
        List<Entidad> copia = new ArrayList<>(componentes);
        for (Entidad ev : copia) {
            if (ev != null) {
                ev.mover(dx, dy);
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

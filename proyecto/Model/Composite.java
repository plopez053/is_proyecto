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

    // Owner removed: liveness checks moved to managers/GameBoard

    public List<Entidad> getComponentes() {
        return componentes;
    }

    @Override
    public boolean canMove(int dx, int dy) {
        if (componentes.isEmpty())
            return false;
        for (Entidad ev : componentes) {
            if (ev != null && !ev.canMove(dx, dy)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void asignar() {
        if (componentes.isEmpty())
            return;

        for (Entidad ev : componentes) {
            if (ev != null)
                ev.asignar();
        }
    }

    @Override
    public void borrar() {
        List<Entidad> copia = new ArrayList<>(componentes);
        for (Entidad ev : copia) {
            if (ev != null) ev.borrar();
        }
        componentes.clear(); // Vaciamos el composite al destruirlo
    }

    @Override
    public void mover(int dx, int dy) {
        List<Entidad> copia = new ArrayList<>(componentes);
        for (Entidad ev : copia) {
            // Si la lista principal se vació durante el choque, abortamos para evitar píxeles fantasma
            if (componentes.isEmpty()) break;

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

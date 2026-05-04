package Model;
 
import java.util.ArrayList;
import java.util.Iterator;
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
        if (componentes.isEmpty())
            return false;
        
        Iterator<Entidad> it = componentes.iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (ev != null && !ev.canMove(dx, dy)) {
                return false;
            }
        }
        return true;
    }
 
    @Override
    public void asignar() {
        Iterator<Entidad> it = componentes.iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (ev != null)
                ev.asignar();
        }
    }
 
    @Override
    public void borrar() {
        Iterator<Entidad> it = new ArrayList<>(componentes).iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (ev != null) ev.borrar();
        }
        componentes.clear();
    }
 
    @Override
    public void vaciar() {
        Iterator<Entidad> it = componentes.iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (ev != null) ev.vaciar();
        }
    }
 
    @Override
    public void mover(int dx, int dy) {
        Iterator<Entidad> it = new ArrayList<>(componentes).iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (componentes.isEmpty()) break;
            if (ev != null) {
                ev.mover(dx, dy);
            }
        }
    }
 
    public List<Pixel> getPixelesOcupados() {
        List<Pixel> ocupadas = new ArrayList<>();
        Iterator<Entidad> it = new ArrayList<>(componentes).iterator();
        while (it.hasNext()) {
            Entidad ev = it.next();
            if (ev instanceof Pixel) {
                ocupadas.addAll(((Pixel) ev).getPixelesOcupados());
            } else if (ev instanceof Composite) {
                ocupadas.addAll(((Composite) ev).getPixelesOcupados());
            }
        }
        return ocupadas;
    }
}

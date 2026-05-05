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
        
        return componentes.stream().allMatch(ev -> ev == null || ev.canMove(dx, dy));
    }
 
    @Override
    public void asignar() {
        componentes.stream().filter(ev -> ev != null).forEach(Entidad::asignar);
    }
 
    @Override
    public void borrar() {
        new ArrayList<>(componentes).stream().filter(ev -> ev != null).forEach(Entidad::borrar);
        componentes.clear();
    }
 

 
    @Override
    public void mover(int dx, int dy) {
        // Usamos Java 8 para iterar sobre una copia, pero filtrando si el componente sigue activo
        new ArrayList<>(componentes).stream()
            .filter(ev -> ev != null && componentes.contains(ev))
            .forEach(ev -> ev.mover(dx, dy));
    }

    @Override
    public boolean ocupaCoordenada(int x, int y) {
        return componentes.stream()
                .filter(c -> c != null)
                .anyMatch(c -> c.ocupaCoordenada(x, y));
    }
 

}

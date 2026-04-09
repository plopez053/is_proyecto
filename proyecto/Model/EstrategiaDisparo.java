package Model;

import java.util.List;

public interface EstrategiaDisparo {
    List<Disparo> disparar(int xCentral, int yCentral);
    int getMunicion();
    void setMunicion(int m);
    String getNombre();
}

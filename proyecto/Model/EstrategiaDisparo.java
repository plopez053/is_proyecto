package Model;

public interface EstrategiaDisparo {
    Composite disparar(int xCentral, int yCentral);
    int getMunicion();
    void setMunicion(int m);
    String getNombre();
}

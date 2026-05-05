package Model;

public class DisparoDobleBoss implements EstrategiaDisparo {

    @Override
    public Composite disparar(int xCentral, int yCentral) {
        int shotY = yCentral + 4; // Nace debajo del Boss
        
        Composite cuerpo = new Composite();
        // Disparo doble izquierdo y derecho
        Pixel p1 = new Pixel(xCentral - 5, shotY, new casillaDisparo());
        p1.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p1);

        Pixel p2 = new Pixel(xCentral + 5, shotY, new casillaDisparo());
        p2.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p2);
        
        return cuerpo;
    }

    @Override
    public int getMunicion() {
        return -1; // Ilimitada para el Boss
    }

    @Override
    public void setMunicion(int m) { }

    @Override
    public String getNombre() {
        return "Doble Boss";
    }
}

package Model;

public class DisparoPixelStrategy implements EstrategiaDisparo {
    @Override
    public Composite disparar(int xCentral, int yCentral) {
        int shotY = yCentral - 4;
        if (shotY >= 0) {
            Composite cuerpo = new Composite();
            cuerpo.addComponente(new Pixel(xCentral, shotY, new casillaDisparo()));
            return cuerpo;
        }
        return null;
    }

    @Override
    public int getMunicion() {
        return -1; // Ilimitado
    }

    @Override
    public void setMunicion(int m) {
        // no-op
    }

    @Override
    public String getNombre() {
        return "Pixel";
    }
}

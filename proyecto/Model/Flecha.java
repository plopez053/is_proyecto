package Model;

public class Flecha implements EstrategiaDisparo {
    private int municion = 30;

    @Override
    public Composite disparar(int xCentral, int yCentral) {
        if (municion > 0) {
            municion--;
            int[][] desplazamientos = {
                    { 0, -4 }, // Punta
                    { -1, -3 }, // Diagonal abajo izq
                    { 1, -3 } // Diagonal abajo der
            };
            Composite cuerpo = new Composite();
            for (int[] d : desplazamientos) {
                Pixel p = new Pixel(xCentral + d[0], yCentral + d[1], new casillaDisparo());
                p.addObserver(JugadorManager.getJugadorManager());
                cuerpo.addComponente(p);
            }
            return cuerpo;
        }
        return null;
    }

    @Override
    public int getMunicion() {
        return municion;
    }

    @Override
    public void setMunicion(int m) {
        this.municion = m;
    }

    @Override
    public String getNombre() {
        return "Flecha";
    }
}

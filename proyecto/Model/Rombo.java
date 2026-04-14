package Model;

public class Rombo implements EstrategiaDisparo {
    private int municion = 20;

    @Override
    public Composite disparar(int xCentral, int yCentral) {
        if (municion > 0) {
            municion--;
            int[][] desplazamientos = {
                    { 0, -7 },
                    { -1, -6 }, { 0, -6 }, { 1, -6 }, // Punta
                    { -2, -5 }, { -1, -5 }, { 0, -5 }, { 1, -5 }, { 2, -5 }, // Centro
                    { -1, -4 }, { 0, -4 }, { 1, -4 }, // Punta
                    { 0, -3 },
            };
            Composite cuerpo = new Composite();
            for (int[] d : desplazamientos) {
                cuerpo.addComponente(new Pixel(xCentral + d[0], yCentral + d[1], new casillaDisparo()));
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
        return "Rombo";
    }
}

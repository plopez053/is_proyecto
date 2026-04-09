package Model;

import java.util.ArrayList;
import java.util.List;

public class Rombo implements EstrategiaDisparo {
    private int municion = 20;

    @Override
    public List<Disparo> disparar(int xCentral, int yCentral) {
        List<Disparo> listaDisparos = new ArrayList<>();
        if (municion > 0) {
            municion--;
            int[][] desplazamientos = {
                    { 0, -6 }, // Punta
                    { -1, -5 }, { 0, -5 }, { 1, -5 }, // Centro
                    { 0, -4 } // Base
            };
            listaDisparos.add(new Disparo(xCentral, yCentral, desplazamientos));
        }
        return listaDisparos;
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

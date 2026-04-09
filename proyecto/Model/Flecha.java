package Model;

import java.util.ArrayList;
import java.util.List;

public class Flecha implements EstrategiaDisparo {
    private int municion = 30;

    @Override
    public List<Disparo> disparar(int xCentral, int yCentral) {
        List<Disparo> listaDisparos = new ArrayList<>();
        if (municion > 0) {
            municion--;
            int[][] desplazamientos = {
                    { 0, -4 }, // Punta
                    { -1, -3 }, // Diagonal abajo izq
                    { 1, -3 } // Diagonal abajo der
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
        return "Flecha";
    }
}

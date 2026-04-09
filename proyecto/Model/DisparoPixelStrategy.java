package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisparoPixelStrategy implements EstrategiaDisparo {
    @Override
    public List<Disparo> disparar(int xCentral, int yCentral) {
        int shotY = yCentral - 4;
        List<Disparo> listaDisparos = new ArrayList<>();
        if (shotY >= 0) {
            listaDisparos.add(new Disparo(xCentral, shotY));
        }
        return listaDisparos;
    }

    @Override
    public int getMunicion() {
        return -1; // Ilimitado
    }

    @Override
    public void setMunicion(int m) {
        // No hace nada para ilimitados
    }

    @Override
    public String getNombre() {
        return "Pixel";
    }
}

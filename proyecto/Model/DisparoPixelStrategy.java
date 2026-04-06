package Model;

import java.util.Collections;
import java.util.List;

public class DisparoPixelStrategy implements EstrategiaDisparo {
    @Override
    public List<Disparo> disparar(int xCentral, int yCentral) {
        int shotY = yCentral - 2;
        if (shotY >= 0) {
            return Collections.singletonList(new Disparo(xCentral, shotY));
        }
        return Collections.emptyList();
    }
}

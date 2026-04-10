package Model;

import java.util.Collections;
import java.util.List;

public class Enemigo {
    private Malo miNave;

    public Enemigo(Malo nave) {
        this.miNave = nave;
    }

    public Malo getNave() {
        return miNave;
    }

    public void mover(int dx, int dy) {
        if (miNave != null) {
            miNave.mover(dx, dy);
        }
    }

    public List<Pixel> getPixelesOcupados() {
        return miNave != null ? miNave.getPixelesOcupados() : Collections.emptyList();
    }
}

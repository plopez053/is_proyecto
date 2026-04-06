package Model;

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
}

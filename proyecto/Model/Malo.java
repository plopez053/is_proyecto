package Model;

public class Malo extends Nave {
    public Malo(int x, int y) {
        super(x, y);
    }

    public void notificarDestruccion() {
        EnemigoManager.getEnemigoManager().notificarDestruccionNave(this);
    }
}

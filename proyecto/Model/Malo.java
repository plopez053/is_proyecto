package Model;

public class Malo extends Nave {
    public Malo(int x, int y) {
        super(x, y);
    }

    @Override
    public void procesarDestruccion() {
        if (estaViva()) {
            removeNave();
            EnemigoManager.getEnemigoManager().notificarColisionComposite(getCuerpo());
        }
    }
}

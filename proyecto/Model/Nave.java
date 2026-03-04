package Model;

public class Nave extends Casilla {
    public Nave(int x, int y) {
        super(x, y);
    }

    public void moverNaveV(int y) {
        setY(y);
    }

    public void moverNave(int x) {
        setX(x);
    }

    public void disparar() {
        int shotX = getX();
        int shotY = getY() - 2;
        if (shotY >= 0) {
            new Disparo(shotX, shotY);
        }
    }
}

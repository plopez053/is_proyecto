package Model;

public abstract class Nave extends Casilla {
    protected boolean viva = true;
    protected Composite cuerpo;

    public Nave(int x, int y) {
        super(x, y);
    }

    public void setCuerpo(Composite cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Composite getCuerpo() {
        return cuerpo;
    }

    public void mover(int dx, int dy) {
        if (cuerpo != null) {
            cuerpo.mover(dx, dy);
        }
        setX(getX() + dx);
        setY(getY() + dy);
    }

    public void removeNave() {
        viva = false;
    }

    public boolean estaViva() {
        return viva;
    }
}

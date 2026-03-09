package Model;

public class Nave extends Casilla {
	private boolean viva = true;
	
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
    
    public void removeNave() {
    	viva = false;
    }
    
    public boolean estaViva() {
    	return viva;
    }
}

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
}

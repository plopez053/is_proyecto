package Model;

public class Malo extends Nave {
    public Malo(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();
        
        Pixel p1 = new Pixel(x, y, new casillaEnemigo());
        p1.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p1);
        
        Pixel p2 = new Pixel(x + 1, y, new casillaEnemigo());
        p2.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p2);
        
        Pixel p3 = new Pixel(x - 1, y, new casillaEnemigo());
        p3.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p3);
        
        Pixel p4 = new Pixel(x - 1, y + 1, new casillaEnemigo());
        p4.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p4);
        
        Pixel p5 = new Pixel(x + 1, y + 1, new casillaEnemigo());
        p5.addObserver(EnemigoManager.getEnemigoManager());
        cuerpo.addComponente(p5);

        this.setCuerpo(cuerpo);
    }

    public void notificarDestruccion() {
        EnemigoManager.getEnemigoManager().notificarDestruccionNave(this);
    }

}

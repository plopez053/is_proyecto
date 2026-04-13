package Model;

public class Malo extends Nave {
    public Malo(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();
        cuerpo.addComponente(new Pixel(x, y, new casillaEnemigo()));
        cuerpo.addComponente(new Pixel(x + 1, y, new casillaEnemigo()));
        cuerpo.addComponente(new Pixel(x - 1, y, new casillaEnemigo()));
        cuerpo.addComponente(new Pixel(x - 1, y + 1, new casillaEnemigo()));
        cuerpo.addComponente(new Pixel(x + 1, y + 1, new casillaEnemigo()));

        this.setCuerpo(cuerpo);
    }

    public void notificarDestruccion() {
        EnemigoManager.getEnemigoManager().notificarDestruccionNave(this);
    }

    
}

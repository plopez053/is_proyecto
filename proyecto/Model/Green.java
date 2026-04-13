package Model;

public class Green extends Bueno {
    public Green(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();
        cuerpo.addComponente(new Pixel(x, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x - 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x + 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x - 1, y - 1, new casillaNave()));
        cuerpo.addComponente(new Pixel(x, y - 1, new casillaNave()));
        cuerpo.addComponente(new Pixel(x + 1, y - 1, new casillaNave()));

        this.setCuerpo(cuerpo);

        java.util.List<EstrategiaDisparo> armas = new java.util.ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Flecha());
        this.setArmasDisponibles(armas);
    }
}

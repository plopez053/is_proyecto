package Model;

public class Blue extends Bueno {
    public Blue(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();
        cuerpo.addComponente(new Pixel(x, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x - 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x + 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x - 1, y - 1, new casillaNave()));
        cuerpo.addComponente(new Pixel(x + 1, y - 1, new casillaNave()));

        this.setCuerpo(cuerpo);

        java.util.List<EstrategiaDisparo> armas = new java.util.ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Rombo());
        this.setArmasDisponibles(armas);
    }
}

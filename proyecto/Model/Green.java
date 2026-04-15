package Model;
import java.util.ArrayList;
import java.util.List;
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

        List<EstrategiaDisparo> armas = new ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Flecha());
        this.setArmasDisponibles(armas);
    }
}

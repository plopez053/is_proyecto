package Model;
import java.util.ArrayList;
import java.util.List;
public class Blue extends Bueno {
    public Blue(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();
        
        Pixel p1 = new Pixel(x, y, new casillaNave());
        p1.addObserver(JugadorManager.getJugadorManager());
        cuerpo.addComponente(p1);
        
        Pixel p2 = new Pixel(x - 1, y, new casillaNave());
        p2.addObserver(JugadorManager.getJugadorManager());
        cuerpo.addComponente(p2);
        
        Pixel p3 = new Pixel(x + 1, y, new casillaNave());
        p3.addObserver(JugadorManager.getJugadorManager());
        cuerpo.addComponente(p3);
        
        Pixel p4 = new Pixel(x - 1, y - 1, new casillaNave());
        p4.addObserver(JugadorManager.getJugadorManager());
        cuerpo.addComponente(p4);
        
        Pixel p5 = new Pixel(x + 1, y - 1, new casillaNave());
        p5.addObserver(JugadorManager.getJugadorManager());
        cuerpo.addComponente(p5);

        this.setCuerpo(cuerpo);

        List<EstrategiaDisparo> armas = new ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Rombo());
        this.setArmasDisponibles(armas);
    }
}

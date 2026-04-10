package Model;

import java.util.ArrayList;
import java.util.List;

public class NaveFactory {
    private static NaveFactory instance;

    private NaveFactory() {
    }

    public static NaveFactory getInstance() {
        if (instance == null) {
            instance = new NaveFactory();
        }
        return instance;
    }

    public static NaveFactory getNaveFactory() {
        return getInstance();
    }

    public Nave crearNave(String tipo, int x, int y) {
        switch (tipo.toUpperCase()) {
            case "BUENO_RED":
                return crearBuenoRed(new Red(x, y), x, y);
            case "BUENO_GREEN":
                return crearBuenoGreen(new Green(x, y), x, y);
            case "BUENO_BLUE":
                return crearBuenoBlue(new Blue(x, y), x, y);
            case "MALO":
                return crearMalo(x, y);
            default:
                throw new IllegalArgumentException("Tipo no valido");
        }
    }

    private void addPixel(Composite c, Pixel p, Object owner) {
        // Añadir al composite y establecer el owner del composite.
        c.addComponente(p);
        c.setOwner(owner);

        // Registrar managers como observers del píxel para que reciban
        // notificaciones de destrucción directamente (similar a GameBoard->View).
        if (owner instanceof Malo) {
            p.addObserver(EnemigoManager.getEnemigoManager());
        } else {
            p.addObserver(JugadorManager.getInstance());
        }
    }

    private Bueno crearBuenoRed(Bueno nave, int x, int y) {
        Composite cuerpo = new Composite();
        addPixel(cuerpo, new Pixel(x, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x - 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x + 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x, y - 1, new casillaNave()), nave);

        nave.setCuerpo(cuerpo);
        List<EstrategiaDisparo> armas = new ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Flecha());
        armas.add(new Rombo());
        nave.setArmasDisponibles(armas);
        return nave;
    }

    private Bueno crearBuenoGreen(Bueno nave, int x, int y) {
        Composite cuerpo = new Composite();
        addPixel(cuerpo, new Pixel(x, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x - 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x + 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x - 1, y - 1, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x, y - 1, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x + 1, y - 1, new casillaNave()), nave);

        nave.setCuerpo(cuerpo);
        List<EstrategiaDisparo> armas = new ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Flecha());
        nave.setArmasDisponibles(armas);
        return nave;
    }

    private Bueno crearBuenoBlue(Bueno nave, int x, int y) {
        Composite cuerpo = new Composite();
        addPixel(cuerpo, new Pixel(x, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x - 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x + 1, y, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x - 1, y - 1, new casillaNave()), nave);
        addPixel(cuerpo, new Pixel(x + 1, y - 1, new casillaNave()), nave);

        nave.setCuerpo(cuerpo);
        List<EstrategiaDisparo> armas = new ArrayList<>();
        armas.add(new DisparoPixelStrategy());
        armas.add(new Rombo());
        nave.setArmasDisponibles(armas);
        return nave;
    }

    private Malo crearMalo(int x, int y) {
        Malo enemigo = new Malo(x, y);
        Composite cuerpo = new Composite();
        addPixel(cuerpo, new Pixel(x, y, new casillaEnemigo()), enemigo);
        addPixel(cuerpo, new Pixel(x + 1, y, new casillaEnemigo()), enemigo);
        addPixel(cuerpo, new Pixel(x - 1, y, new casillaEnemigo()), enemigo);
        addPixel(cuerpo, new Pixel(x - 1, y + 1, new casillaEnemigo()), enemigo);
        addPixel(cuerpo, new Pixel(x + 1, y + 1, new casillaEnemigo()), enemigo);

        enemigo.setCuerpo(cuerpo);
        return enemigo;
    }
}

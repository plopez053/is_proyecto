package Model;

public class NaveFactory {
    private static NaveFactory miNaveFactory;

    private NaveFactory() {}

    public static NaveFactory getNaveFactory() {
        if (miNaveFactory == null) {
            miNaveFactory = new NaveFactory();
        }
        return miNaveFactory;
    }

    public static NaveFactory getInstance() {
        return getNaveFactory();
    }

    public Nave crearNave(String tipo, int x, int y) {
        switch (tipo.toUpperCase()) {
            case "BUENO_RED":   return registrarObservers(new Red(x, y), false);
            case "BUENO_GREEN": return registrarObservers(new Green(x, y), false);
            case "BUENO_BLUE":  return registrarObservers(new Blue(x, y), false);
            case "MALO":        return registrarObservers(new Malo(x, y), true);
            case "BOSS":        return registrarObservers(new FinalBoss(x, y), true);
            default: throw new IllegalArgumentException("Tipo no valido: " + tipo);
        }
    }

    // Registra el observer correcto en cada píxel del composite
    private Nave registrarObservers(Nave nave, boolean esEnemigo) {
        if (nave.getCuerpo() == null) return nave;
        for (Pixel p : nave.getPixelesOcupados()) {
            if (esEnemigo) {
                p.addObserver(EnemigoManager.getEnemigoManager());
            } else {
                p.addObserver(JugadorManager.getJugadorManager());
            }
        }
        return nave;
    }
}

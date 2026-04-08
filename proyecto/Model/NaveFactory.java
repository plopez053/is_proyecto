package Model;

public class NaveFactory {
    private static NaveFactory instance;

    private NaveFactory() {}

    public static NaveFactory getInstance() {
        if (instance == null) {
            instance = new NaveFactory();
        }
        return instance;
    }

    // Para compatibilidad con código de compañeros
    public static NaveFactory getNaveFactory() {
        return getInstance();
    }

    public Nave crearNave(String tipo, int x, int y) {
        switch (tipo.toUpperCase()) {

        case "BUENO_RED":
            return crearBueno(new Red(x,y), x, y);

        case "BUENO_GREEN":
            return crearBueno(new Green(x,y), x, y);

        case "BUENO_BLUE":
            return crearBueno(new Blue(x,y), x, y);

        case "MALO":
            return crearMalo(x, y);

        default:
            throw new IllegalArgumentException("Tipo no v�lido");
        }
    }
    private Bueno crearBueno(Bueno nave, int x, int y) {
    	Composite cuerpo = new Composite();

        cuerpo.addComponente(new Pixel(x, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x - 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x + 1, y, new casillaNave()));
        cuerpo.addComponente(new Pixel(x, y - 1, new casillaNave()));

        nave.setCuerpo(cuerpo);
        nave.setArmaActual(new DisparoPixelStrategy());

        return nave;
    }
    
    private Malo crearMalo(int x, int y) {

        Malo enemigo = new Malo(x,y);
        Composite cuerpo = new Composite();

        cuerpo.addComponente(new Pixel(x, y, new casillaEnemigo()));
        cuerpo.addComponente(new Pixel(x + 1, y, new casillaEnemigo()));

        enemigo.setCuerpo(cuerpo);

        return enemigo;
    }
}

package Model;

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
                return new Red(x, y);
            case "BUENO_GREEN":
                return new Green(x, y);
            case "BUENO_BLUE":
                return new Blue(x, y);
            case "MALO":
                return new Malo(x, y);
            default:
                throw new IllegalArgumentException("Tipo no valido");
        }
    }
}

package Model;

public class NaveFactory {
    private static NaveFactory miNaveFactory;

    private NaveFactory() {
    }

    public static NaveFactory getNaveFactory() {
        if (miNaveFactory == null) {
            miNaveFactory = new NaveFactory();
        }
        return miNaveFactory;
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

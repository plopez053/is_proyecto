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

    public Nave crearNave(String tipo, int x, int y) {
        Nave myNave = null;
        if (tipo.equalsIgnoreCase("Bueno")) {
            Bueno nave = new Bueno(x, y);
            Composite cuerpo = new Composite();
            
            // Creamos una forma básica (tipo cruz) para la nave buena
            cuerpo.addComponente(new Pixel(x, y, 1)); // centro
            cuerpo.addComponente(new Pixel(x - 1, y, 1)); // izquierda
            cuerpo.addComponente(new Pixel(x + 1, y, 1)); // derecha
            cuerpo.addComponente(new Pixel(x, y - 1, 1)); // arriba
            
            nave.setCuerpo(cuerpo);
            nave.setArmaActual(new DisparoPixelStrategy());
            myNave = nave;
        } else if (tipo.equalsIgnoreCase("Malo")) {
            Malo enemigo = new Malo(x, y);
            Composite cuerpo = new Composite();
            
            // Un cuerpo de enemigo simple de 2 pixeles (por ejemplo)
            cuerpo.addComponente(new Pixel(x, y, 0));
            cuerpo.addComponente(new Pixel(x + 1, y, 0));
            
            enemigo.setCuerpo(cuerpo);
            myNave = enemigo;
        }
        return myNave;
    }
}

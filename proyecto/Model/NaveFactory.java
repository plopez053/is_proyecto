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
        Nave myNave = null;
        if (tipo.equalsIgnoreCase("Bueno")) {
            Bueno bueno = new Bueno(x, y);
            Composite cuerpo = new Composite();
            
            // Creamos una forma básica (tipo cruz) para la nave buena
            Pixel p1 = new Pixel(x, y, new casillaNave()); p1.setOwner(bueno);
            Pixel p2 = new Pixel(x - 1, y, new casillaNave()); p2.setOwner(bueno);
            Pixel p3 = new Pixel(x + 1, y, new casillaNave()); p3.setOwner(bueno);
            Pixel p4 = new Pixel(x, y - 1, new casillaNave()); p4.setOwner(bueno);
            
            cuerpo.addComponente(p1);
            cuerpo.addComponente(p2);
            cuerpo.addComponente(p3);
            cuerpo.addComponente(p4);
            
            bueno.setCuerpo(cuerpo);
            bueno.setArmaActual(new DisparoPixelStrategy());
            myNave = bueno;
        } else if (tipo.equalsIgnoreCase("Malo")) {
            Malo malo = new Malo(x, y);
            Composite cuerpo = new Composite();
            
            // Un cuerpo de enemigo simple de 2 pixeles (por ejemplo)
            Pixel p1 = new Pixel(x, y, new casillaEnemigo()); p1.setOwner(malo);
            Pixel p2 = new Pixel(x + 1, y, new casillaEnemigo()); p2.setOwner(malo);
            
            cuerpo.addComponente(p1);
            cuerpo.addComponente(p2);
            
            malo.setCuerpo(cuerpo);
            myNave = malo;
        }

        return myNave;
    }
}

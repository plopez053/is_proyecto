package Model;

public class casillaEnemigo implements EstadoCasilla {
    @Override public boolean esNave() { return false; }
    @Override public boolean estaOcupada() { return true; }
    @Override public boolean esEnemigo() { return true; }
    @Override public boolean esDisparo() { return false; }
}

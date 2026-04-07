package Model;

public class casillaNave implements EstadoCasilla {
    @Override public boolean esNave() { return true; }
    @Override public boolean estaOcupada() { return true; }
    @Override public boolean esEnemigo() { return false; }
    @Override public boolean esDisparo() { return false; }
}

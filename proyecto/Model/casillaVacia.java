package Model;

/**
 * Estado: Casilla vacía.
 * Al ser impactado → no hace nada (ya está vacía).
 * Equivale a Opened.click() en el minesWeeper que no hace nada.
 */
public class casillaVacia implements EstadoCasilla {
    @Override public boolean esNave()       { return false; }
    @Override public boolean estaOcupada()  { return false; }
    @Override public boolean esEnemigo()    { return false; }
    @Override public boolean esDisparo()    { return false; }

    @Override
    public void impactar(Pixel contexto) {
        // No hace nada: la casilla está vacía, como Opened.click() en minesWeeper
    }
}

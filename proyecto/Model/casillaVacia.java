package Model;

/**
 * Estado: Casilla vacía.
 * Al ser impactado → no hace nada (ya está vacía).
 */
public class casillaVacia implements EstadoCasilla {
    @Override
    public TipoCasilla getTipo() {
        return TipoCasilla.VACIA;
    }

    @Override
    public void impactar(Pixel contexto) {
        
    }
}

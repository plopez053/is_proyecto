package Model;

/**
 * Estado: Disparo (proyectil).
 * Responsabilidad: notificar al owner y cambiar el estado.
 */
public class casillaDisparo implements EstadoCasilla {
    @Override
    public TipoCasilla getTipo() {
        return TipoCasilla.DISPARO;
    }

    @Override
    public void impactar(Pixel contexto) {
        contexto.notificarDestruccion();
        contexto.changeState(new casillaVacia());
    }
}

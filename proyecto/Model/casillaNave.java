package Model;

/**
 * Estado: Nave del jugador.
 * Responsabilidad: notificar al manager y cambiar el estado.
 */
public class casillaNave implements EstadoCasilla {
    @Override
    public TipoCasilla getTipo() {
        return TipoCasilla.NAVE;
    }

    @Override
    public void impactar(Pixel contexto) {
        contexto.notificarDestruccion();
        contexto.changeState(new casillaVacia());
    }
}

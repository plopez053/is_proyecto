package Model;

/**
 * Estado: Nave enemiga.
 * Responsabilidad: notificar al owner y cambiar el estado.
 */
public class casillaEnemigo implements EstadoCasilla {
    @Override
    public TipoCasilla getTipo() {
        return TipoCasilla.ENEMIGO;
    }

    @Override
    public void impactar(Pixel contexto) {
        contexto.notificarDestruccion();
        contexto.changeState(new casillaVacia());
    }
}

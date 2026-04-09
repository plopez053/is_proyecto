package Model;

/**
 * Estado: Disparo (proyectil).
 * Responsabilidad: notificar al owner y cambiar el estado.
 * El Pixel (Contexto) se encarga de borrarse del tablero.
 */
public class casillaDisparo implements EstadoCasilla {
    @Override public boolean esNave()       { return false; }
    @Override public boolean estaOcupada()  { return true; }
    @Override public boolean esEnemigo()    { return false; }
    @Override public boolean esDisparo()    { return true; }

    @Override
    public void impactar(Pixel contexto) {
        // 1. Notificar al dueño
        if (contexto.getOwner() != null) {
            contexto.getOwner().procesarDestruccion();
        }
        // 2. Transición de estado → el Pixel queda vacío
        contexto.changeState(new casillaVacia());
    }
}

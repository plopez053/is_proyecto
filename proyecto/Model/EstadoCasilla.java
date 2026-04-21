package Model;

public interface EstadoCasilla {
    /**
     * Enum para identificar el tipo de casilla sin usar booleanos.
     */
    enum TipoCasilla {
        VACIA, NAVE, ENEMIGO, DISPARO, BOSS
    }

    /**
     * @return El tipo de entidad contenido en este píxel.
     */
    TipoCasilla getTipo();

    /**
     * Acción que ocurre cuando este píxel es impactado.
     * El estado decide el comportamiento (p.ej. notificar al manager)
     * Y la transición al siguiente estado.
     */
    void impactar(Pixel contexto);

    /**
     * @return true si la casilla tiene contenido (no es VACIA).
     */
    default boolean estaOcupada() {
        return getTipo() != TipoCasilla.VACIA;
    }
}

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

    default boolean puedeColisionar(EstadoCasilla otro) {
        if (this.getTipo() == otro.getTipo()) return false;
        if (this.getTipo() == TipoCasilla.DISPARO && otro.getTipo() == TipoCasilla.DISPARO) return false;
        return true;
    }
}

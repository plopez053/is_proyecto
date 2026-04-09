package Model;

public interface EstadoCasilla {
    boolean esNave();
    boolean estaOcupada();
    boolean esEnemigo();
    boolean esDisparo();
    
    /**
     * Acción que ocurre cuando este píxel es impactado.
     * El estado decide el comportamiento Y la transición al siguiente estado.
     * → Equivale a click(Square) en el ejemplo minesWeeper del repositorio.
     */
    void impactar(Pixel contexto);
}

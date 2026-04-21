package Model;

public class CasillaBoss implements EstadoCasilla {
    @Override
    public TipoCasilla getTipo() {
        return TipoCasilla.BOSS;
    }

    @Override
    public void impactar(Pixel contexto) {
      
        contexto.notificarDestruccion();

    }
}
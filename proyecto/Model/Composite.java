package Model;

import java.util.ArrayList;
import java.util.List;

public class Composite implements Entidad {
    private List<Entidad> componentes = new ArrayList<>();

    public void addComponente(Entidad ev) {
        componentes.add(ev);
    }

    public void removeComponente(Entidad ev) {
        componentes.remove(ev);
    }

    @Override
    public void mover(int dx, int dy) {
        if (comprobarColisiones(dx, dy)) {
            return; // Abortar movimiento si la nave fue destruida o chocó con un disparo
        }
        for (Entidad ev : componentes) {
            ev.mover(dx, dy);
        }
    }

    private boolean comprobarColisiones(int dx, int dy) { 
        GameBoard board = GameBoard.getGameBoard();
        boolean chocoFuerte = false;
        for (Entidad ev : componentes) {
            if (ev instanceof Pixel) {
                Pixel p = (Pixel) ev;
                int nx = p.getX() + dx;
                int ny = p.getY() + dy;
                
                if (board.esPosicionValida(nx, ny)) {
                    Pixel p1 = board.getCasilla(nx, ny);
                    if (p1.esDisparo()) {
                        JugadorManager.getJugador().eliminarDisparoActivo((Disparo)c); //TODO hay que cambiar el parametro
                        EnemigoManager.getEnemigoManager().notificarColisionComposite(this);
                        chocoFuerte = true;                
                    }else if ((p.esEnemigo() && p1.esNave()) || (p.esNave() && p1.esEnemigo())) {
                            Nave n = JugadorManager.getJugador().getNave();
                            if (n != null) n.removeNave();
                            chocoFuerte = true;
                    }
                 }
              }
          }
        return chocoFuerte;
    }

    @Override
    public List<Pixel> getCasillasOcupadas() {
        List<Pixel> ocupadas = new ArrayList<>();
        for (Entidad ev : componentes) {
            ocupadas.addAll(ev.getCasillasOcupadas());
        }
        return ocupadas;
    }
}

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
                    Casilla c = board.getCasilla(nx, ny);
                    if (c instanceof Disparo) {
                        JugadorManager.getInstance().eliminarDisparoActivo((Disparo)c);
                        EnemigoManager.getEnemigoManager().notificarColisionComposite(this);
                        chocoFuerte = true;
                    } else if (c instanceof Pixel) {
                        Pixel hitPixel = (Pixel) c;
                        if ((p.getEntityType() == 0 && hitPixel.getEntityType() == 1) || 
                            (p.getEntityType() == 1 && hitPixel.getEntityType() == 0)) {
                            Nave n = JugadorManager.getInstance().getNave();
                            if (n != null) n.removeNave();
                            chocoFuerte = true;
                        }
                    }
                }
            }
        }
        return chocoFuerte;
    }

    @Override
    public List<Casilla> getCasillasOcupadas() {
        List<Casilla> ocupadas = new ArrayList<>();
        for (Entidad ev : componentes) {
            ocupadas.addAll(ev.getCasillasOcupadas());
        }
        return ocupadas;
    }
}

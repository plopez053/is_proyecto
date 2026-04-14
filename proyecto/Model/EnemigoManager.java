package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EnemigoManager implements Observer {
    private static EnemigoManager miEnemigoManager;
    private List<Enemigo> enemigos;
    private Random random;
    private Timer timerEnemigos;

    private EnemigoManager() {
        enemigos = new ArrayList<>();
        random = new Random();
    }

    public static EnemigoManager getEnemigoManager() {
        if (miEnemigoManager == null) {
            miEnemigoManager = new EnemigoManager();
        }
        return miEnemigoManager;
    }

    public void iniciarTimerEnemigos() {
        detenerTimerEnemigos();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                moveEnemies();
            }
        };
        timerEnemigos = new Timer();
        timerEnemigos.schedule(task, 1000, 200);
    }

    public void detenerTimerEnemigos() {
        if (timerEnemigos != null) {
            timerEnemigos.cancel();
            timerEnemigos = null;
        }
    }

    public void spawnEnemies() {
        int numEnemies = random.nextInt(5) + 4; // entre 4 y 8 enemigos
        enemigos = new ArrayList<>();
        GameBoard board = GameBoard.getGameBoard();

        for (int i = 0; i < numEnemies; i++) {
            int x, y;
            boolean positionInvalid;
            do {
                positionInvalid = false;
                x = random.nextInt(board.getWidth() - 2) + 1;
                y = random.nextInt(15);

                // Comprobamos si la casilla central de spawn ya tiene algo
                Pixel p = board.getPixel(x, y);
                if (p != null && p.getEstado().getTipo() != EstadoCasilla.TipoCasilla.VACIA) {
                    positionInvalid = true;
                }

                // Chequeo de proximidad con otros enemigos (lógica de compañeros mejorada)
                for (Enemigo e : enemigos) {
                    if (e.getNave().getCuerpo() != null) {
                        for (Pixel ep : e.getNave().getPixelesOcupados()) {
                            // Si estamos a menos de 6 de distancia en X o 3 en Y de CUALQUIER píxel de otro
                            // enemigo
                            if (Math.abs(ep.getX() - x) < 6 && Math.abs(ep.getY() - y) < 4) {
                                positionInvalid = true;
                                break;
                            }
                        }
                    }
                    if (positionInvalid)
                        break;
                }
            } while (positionInvalid);

            if (!positionInvalid) {
                Malo nuevaNave = (Malo) NaveFactory.getNaveFactory().crearNave("Malo", x, y);
                Enemigo nuevoEnemigo = new Enemigo(nuevaNave);
                enemigos.add(nuevoEnemigo);
                if (nuevaNave.getCuerpo() != null) {
                    // Registrar píxeles del cuerpo en este manager antes de asignar en el tablero
                    registerComposite(nuevaNave.getCuerpo());
                    nuevaNave.getCuerpo().asignar();
                }
            }
        }
    }

    public Enemigo getEnemigoEn(int x, int y) {
        GameBoard board = GameBoard.getGameBoard();
        Pixel p = board.getPixel(x, y);
        if (p != null && p.getEstado().getTipo() == EstadoCasilla.TipoCasilla.ENEMIGO) {
            for (Enemigo e : enemigos) {
                if (e.getNave().getPixelesOcupados().contains(p)) {
                    return e;
                }
            }
        }
        return null;
    }

    public void notificarColisionComposite(Composite c) {
        Enemigo aEliminar = null;
        for (Enemigo e : enemigos) {
            if (e.getNave().getCuerpo() == c) {
                aEliminar = e;
                break;
            }
        }
        if (aEliminar != null) {
            removeEnemigo(aEliminar);
        }
    }

    public void notificarDestruccionNave(Malo nave) {
        Enemigo aEliminar = null;
        for (Enemigo e : enemigos) {
            if (e.getNave() == nave) {
                aEliminar = e;
                break;
            }
        }
        if (aEliminar != null) {
            removeEnemigo(aEliminar);
        }
    }

    public void moveEnemies() {
        List<Enemigo> copia = new ArrayList<>(enemigos);
        for (Enemigo e : copia) {
            e.mover(0, 1);
        }
    }

    

    public void matarEnemigoEnCoordenada(int x, int y) {
        Enemigo aEliminar = getEnemigoEn(x, y);
        if (aEliminar != null) {
            removeEnemigo(aEliminar);
        }
    }

    public void removeEnemigo(Enemigo e) {
        enemigos.remove(e);
        if (e.getNave() != null) {
            if (e.getNave().getCuerpo() != null) {
                    e.getNave().removeNave();
                    e.getNave().getCuerpo().borrar();
            } else {
                e.getNave().removeNave();
            }
        }
    }

    /** Registrar todos los píxeles de un Composite para que este manager reciba
     * notificaciones de destrucción desde los píxeles.
     */
    public void registerComposite(Composite c) {
        if (c == null)
            return;
        for (Pixel p : c.getPixelesOcupados()) {
            if (p != null)
                p.addObserver(this);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof int[]) {
            int[] coords = (int[]) arg;
            int x = coords[0];
            int y = coords[1];
            // Buscar si algún enemigo tiene ese pixel
            Malo maloAEliminar = null;
            for (Enemigo enemigo : enemigos) {
                Malo malo = enemigo.getNave();
                if (malo != null && malo.getCuerpo() != null) {
                    for (Pixel px : malo.getCuerpo().getPixelesOcupados()) {
                        if (px.getX() == x && px.getY() == y) {
                            maloAEliminar = malo;
                            break;
                        }
                    }
                }
                if (maloAEliminar != null) break;
            }
            if (maloAEliminar != null) {
                notificarDestruccionNave(maloAEliminar);
            }
        }
    }
}


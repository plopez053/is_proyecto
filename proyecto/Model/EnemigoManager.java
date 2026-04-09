package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EnemigoManager {
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
                GameBoard.getGameBoard().evaluarTickEnemigos();
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
                if (p != null && (p.esEnemigo() || p.esNave() || p.esDisparo())) {
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
                Malo nuevaNave = (Malo) NaveFactory.getInstance().crearNave("Malo", x, y);
                Enemigo nuevoEnemigo = new Enemigo(nuevaNave);
                enemigos.add(nuevoEnemigo);
                if (nuevaNave.getCuerpo() != null) {
                    nuevaNave.getCuerpo().dibujar(board);
                }
            }
        }
    }

    public Enemigo getEnemigoEn(int x, int y) {
        GameBoard board = GameBoard.getGameBoard();
        Pixel p = board.getPixel(x, y);
        if (p != null && p.esEnemigo()) {
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

    private List<Enemigo> enemigosAEliminar = new ArrayList<>();

    public void moveEnemies() {
        List<Enemigo> copia = new ArrayList<>(enemigos);
        for (Enemigo e : copia) {
            if (!enemigosAEliminar.contains(e)) {
                e.mover(0, 1);
            }
        }
        if (!enemigosAEliminar.isEmpty()) {
            enemigos.removeAll(enemigosAEliminar);
            enemigosAEliminar.clear();
        }
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public void matarEnemigoEnCoordenada(int x, int y) {
        Enemigo aEliminar = getEnemigoEn(x, y);
        if (aEliminar != null) {
            removeEnemigo(aEliminar);
        }
    }

    public void removeEnemigo(Enemigo e) {
        if (!enemigosAEliminar.contains(e)) {
            enemigosAEliminar.add(e);
        }
        if (e.getNave() != null) {
            e.getNave().removeNave();
            if (e.getNave().getCuerpo() != null) {
                e.getNave().getCuerpo().borrar(GameBoard.getGameBoard());
            }
        }
    }
}

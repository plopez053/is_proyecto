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

    private Timer timer;

    private EnemigoManager() {
        enemigos = new ArrayList<>();
        random = new Random();
    }

    public static synchronized EnemigoManager getEnemigoManager() {
        if (miEnemigoManager == null) {
            miEnemigoManager = new EnemigoManager();
        }
        return miEnemigoManager;
    }

    public void iniciarTimer() {
        detenerTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moverEnemigos();
            }
        }, 1000, 200);
    }

    public void detenerTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void moverEnemigos() {
        GameBoard gb = GameBoard.getGameBoard();
        gb.moverEnemigos();
    }

    public void spawnEnemies() {
        int numEnemies = random.nextInt(5) + 4; // entre 4 y 8 enemigos
        enemigos.clear();

        for (int i = 0; i < numEnemies; i++) {
            int x, y;
            boolean positionInvalid;
            do {
                positionInvalid = false;
                x = random.nextInt(GameBoard.getGameBoard().getWidth());
                y = random.nextInt(15);

                for (Enemigo e : enemigos) {
                    if (Math.abs(e.getX() - x) <= 1 && Math.abs(e.getY() - y) <= 1) {
                        positionInvalid = true;
                        break;
                    }
                }
            } while (positionInvalid);

            if (!positionInvalid) {
                Enemigo nuevoEnemigo = new Enemigo(x, y);
                enemigos.add(nuevoEnemigo);
            }
        }
    }

    public Enemigo getEnemigoEn(int x, int y) {
        for (Enemigo e : enemigos) {
            if (e.getX() == x && e.getY() == y) {
                return e;
            }
        }
        return null;
    }

    public void moveEnemies() {
        for (Enemigo e : enemigos) {
            int nuevaY = e.getY() + 1;
            e.setY(nuevaY);
        }
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public void removeEnemigo(Enemigo e) {
        enemigos.remove(e);
        GameBoard.getGameBoard().setCasilla(e.getX(), e.getY(), new Vacia(e.getX(), e.getY()));
    }
}

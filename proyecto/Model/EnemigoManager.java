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
    private FinalBoss boss = null;
    private int direccionBoss = 1; // 1 = derecha, -1 = izquierda
    private Timer timerBoss;
    private Timer timerEnemigos;
    private List<Composite> disparosBoss = new ArrayList<>();

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
        timerEnemigos.schedule(task, 1000, 400);
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
            boolean cercaniaInvalida;
            do {
                x = random.nextInt(board.getWidth() - 2) + 1;
                y = random.nextInt(15);

                // Chequeo de proximidad con otros enemigos (lógica de compañeros mejorada)
                final int finalX = x;
                final int finalY = y;
                cercaniaInvalida = enemigos.stream()
                        .anyMatch(e -> Math.abs(e.getNave().getX() - finalX) < 6
                                && Math.abs(e.getNave().getY() - finalY) < 4);
            } while (cercaniaInvalida);

            Malo nuevaNave = (Malo) NaveFactory.getNaveFactory().crearNave("Malo", x, y);
            Enemigo nuevoEnemigo = new Enemigo(nuevaNave);
            enemigos.add(nuevoEnemigo);
            if (nuevaNave.getCuerpo() != null) {
                nuevaNave.getCuerpo().asignar();
            }
        }
    }

    public Enemigo getEnemigoEn(int x, int y) {
        for (Enemigo e : enemigos) {
            if (e.getNave() != null && e.getNave().getCuerpo() != null &&
                    e.getNave().getCuerpo().ocupaCoordenada(x, y)) {
                return e;
            }
        }
        return null;
    }

    public void notificarColisionComposite(Composite c) {
        enemigos.stream()
                .filter(e -> e.getNave().getCuerpo() == c)
                .findFirst()
                .ifPresent(this::removeEnemigo);
    }

    public void notificarDestruccionNave(Malo nave) {
        enemigos.stream()
                .filter(e -> e.getNave() == nave)
                .findFirst()
                .ifPresent(this::removeEnemigo);
    }

    public void moveEnemies() {
        new ArrayList<>(enemigos).stream().forEach(e -> e.mover(0, 1));
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
            e.getNave().removeNave();
            if (e.getNave().getCuerpo() != null) {
                e.getNave().getCuerpo().borrar();
            }
        }
        // Cuando caen todos los enemigos, aparece el boss
        if (enemigos.isEmpty() && boss == null) {
            detenerTimerEnemigos();
            spawnBoss();
        }
    }

    public void spawnBoss() {
        int centroX = GameBoard.getGameBoard().getWidth() / 2;
        boss = (FinalBoss) NaveFactory.getNaveFactory().crearNave("BOSS", centroX, 5);
        if (boss.getCuerpo() != null) {
            boss.getCuerpo().asignar();
        }
        iniciarTimerBoss();
    }

    private void iniciarTimerBoss() {
        if (timerBoss != null)
            timerBoss.cancel();
        timerBoss = new Timer();
        timerBoss.schedule(new TimerTask() {
            @Override
            public void run() {
                moverBoss();
                moverDisparosBoss(); // <-- Mueve los disparos en cada tick
            }
        }, 500, 150);
    }

    private void moverBoss() {
        if (boss == null || !boss.estaViva())
            return;

        int dy = 0;
        // Si choca lateralmente, cambia de dirección y baja una casilla
        if (!boss.getCuerpo().canMove(direccionBoss, 0)) {
            direccionBoss *= -1;
            dy = 1;
        }
        boss.mover(direccionBoss, dy); // Patrón Composite

        // Probabilidad de disparo (ej. 20% en cada tick)
        if (random.nextInt(10) < 2) {
            dispararBoss();
        }
    }

    private void dispararBoss() {
        if (boss != null && boss.estaViva()) {
            Composite disparos = boss.disparar();
            if (disparos != null) {
                disparosBoss.add(disparos);
                disparos.asignar(); // Renderiza en el GameBoard
            }
        }
    }

    private void moverDisparosBoss() {
        new ArrayList<>(disparosBoss).stream().forEach(c -> c.mover(0, 1));
    }

    public void notificarDestruccionBoss(FinalBoss b) {
        if (timerBoss != null) {
            timerBoss.cancel();
            timerBoss = null;
        }
        boss = null;
        b.getCuerpo().borrar();

        // Limpiar los disparos huérfanos
        disparosBoss.forEach(Composite::borrar);
        disparosBoss.clear();

        // Lanzar la victoria a nivel global
        GameBoard.getGameBoard().ganarJuego();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Object[]) {
            Object[] coords = (Object[]) arg;
            int px = (int) coords[0];
            int py = (int) coords[1];

            // 1. Comprobar si es el boss
            if (boss != null && boss.estaViva() && boss.getCuerpo() != null) {
                if (boss.getCuerpo().ocupaCoordenada(px, py)) {
                    boss.recibirImpacto();
                    return;
                }
            }

            // 2. Comprobar si es un disparo del boss
            disparosBoss.stream()
                    .filter(c -> c.ocupaCoordenada(px, py))
                    .findFirst()
                    .ifPresent(c -> {
                        disparosBoss.remove(c);
                        c.borrar();
                    });

            // 3. Si no, buscar enemigo normal
            enemigos.stream()
                    .filter(e -> e.getNave() != null && e.getNave().getCuerpo() != null &&
                            e.getNave().getCuerpo().ocupaCoordenada(px, py))
                    .findFirst()
                    .ifPresent(this::removeEnemigo);
        }
    }

}

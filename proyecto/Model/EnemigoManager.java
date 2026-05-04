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
            e.getNave().removeNave();
            if (e.getNave().getCuerpo() != null) {
                e.getNave().getCuerpo().vaciar();
            }
        }
        // Cuando caen todos los enemigos, aparece el boss
        if (enemigos.isEmpty() && boss == null) {
            detenerTimerEnemigos();
            spawnBoss();
        }
    }

    /**
     * Registrar todos los píxeles de un Composite para que este manager reciba
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
                registerComposite(disparos); // Patrón Observer: escucha colisiones
                disparos.asignar(); // Renderiza en el GameBoard
            }
        }
    }

    private void moverDisparosBoss() {
        // Evita ConcurrentModificationException iterando sobre una copia
        List<Composite> copia = new ArrayList<>(disparosBoss);
        for (Composite c : copia) {
            c.mover(0, 1); // Eje Y positivo (hacia abajo)
        }
    }

    public void notificarDestruccionBoss(FinalBoss b) {
        if (timerBoss != null) {
            timerBoss.cancel();
            timerBoss = null;
        }
        boss = null;
        b.getCuerpo().vaciar();

        // Limpiar los disparos huérfanos
        for (Composite c : disparosBoss) {
            c.vaciar();
        }
        disparosBoss.clear();

        // Lanzar la victoria a nivel global
        GameBoard.getGameBoard().ganarJuego();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Pixel) {
            Pixel p = (Pixel) arg;

            // 1. Comprobar si es el boss
            if (boss != null && boss.estaViva()) {
                if (boss.getPixelesOcupados().contains(p)) {
                    boss.recibirImpacto();
                    return;
                }
            }

            // 2. Comprobar si es un disparo del boss
            Composite disparoAEliminar = null;
            for (Composite c : disparosBoss) {
                if (c.getPixelesOcupados().contains(p)) {
                    disparoAEliminar = c;
                    break;
                }
            }
            if (disparoAEliminar != null) {
                disparosBoss.remove(disparoAEliminar);
                disparoAEliminar.vaciar();
                return; // Cortar ejecución si ya se eliminó el disparo
            }

            // 3. Si no, buscar enemigo normal
            Enemigo aEliminar = null;
            for (Enemigo e : new ArrayList<>(enemigos)) {
                if (e.getPixelesOcupados().contains(p)) {
                    aEliminar = e;
                    break;
                }
            }
            if (aEliminar != null)
                removeEnemigo(aEliminar);
        }
    }

}

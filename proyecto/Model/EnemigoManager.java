package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemigoManager {
    private static EnemigoManager miEnemigoManager;
    private List<Enemigo> enemigos; // Ahora guarda el Controlador (Enemigo)
    private Random random;

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
                	if (e.getNave().getCuerpo().getCasillasOcupadas()!=null) {
                		for (Pixel p: e.getNave().getCuerpo().getCasillasOcupadas()) {
                			if (Math.abs(p.getX() - x) <= 1 && Math.abs(p.getY() - y) <= 1) {
                                positionInvalid = true;
                                break;
                            }
                		}
                		
                	}
                    
                }
            } while (positionInvalid);

            if (!positionInvalid) {
                // Utilizando el patrón Factory con "Malo" para la nave
                Malo nuevaNave = (Malo) NaveFactory.getNaveFactory().crearNave("Malo", x, y);
                // Inyectamos la nave en el piloto
                Enemigo nuevoEnemigo = new Enemigo(nuevaNave);
                enemigos.add(nuevoEnemigo);
            }
        }
    }

    public Enemigo getEnemigoEn(int x, int y) {
        for (Enemigo e : enemigos) {
            if (e.getNave().getCuerpo()!=null) {
            	for (Pixel p: e.getNave().getCuerpo().getCasillasOcupadas()) {
            		if (p.getX() == x && p.getY() == y) {
                        return e;

            		}
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
        for (Enemigo e : enemigos) {
            if (!enemigosAEliminar.contains(e)) {
                e.mover(0, 1); // El controlador mueve su nave
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
        Enemigo aEliminar = null;
        for (Enemigo e : enemigos) {
            if (e.getNave().getCuerpo() != null) {
                for (Pixel p : e.getNave().getCuerpo().getCasillasOcupadas()) {
                    if (p.getX() == x && p.getY() == y) {
                        aEliminar = e;
                        break;
                    }
                }
            }
            if (aEliminar != null) break;
        }
        if (aEliminar != null) {
            removeEnemigo(aEliminar);
        }
    }

    public void removeEnemigo(Enemigo e) {
        if (!enemigosAEliminar.contains(e)) {
            enemigosAEliminar.add(e);
        }
        if (e.getNave().getCuerpo() != null) {
            for (Pixel p : e.getNave().getCuerpo().getCasillasOcupadas()) {
            	p.cambiarEstado(new casillaVacia());
                GameBoard.getGameBoard().setCasilla(p.getX(), p.getY(), p);
            }
        }
    }
}

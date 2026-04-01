package Model;

public class Nave extends Casilla {
    private boolean viva = true;

    public Nave(int x, int y) {
        super(x, y);
    }

    public void mover(int dx, int dy) {
        GameBoard board = GameBoard.getGameBoard();
        int nuevaX = getX() + dx;
        int nuevaY = getY() + dy;

        if (board.esPosicionValida(nuevaX, nuevaY)) {
            int viejaX = getX();
            int viejaY = getY();

            if (board.hayEnemigo(nuevaX, nuevaY)) {
                // Si hay un enemigo, la nave muere y el juego termina
                viva = false;
                board.finalizarJuego();
            } else {
                setX(nuevaX);
                setY(nuevaY);
                board.actualizarPosicion(viejaX, viejaY, this);
            }
        }
    }

    public void disparar() {
        int shotX = getX();
        int shotY = getY() - 2;
        if (shotY >= 0) {
            Disparo nuevoDisparo = new Disparo(shotX, shotY);
            DisparoManager.getDisparoManager().agregarDisparo(nuevoDisparo);
        }
    }

    public void removeNave() {
        viva = false;
    }

    public boolean estaViva() {
        return viva;
    }
}

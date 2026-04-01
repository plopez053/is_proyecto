package Model;

public class Disparo extends Casilla {
    public Disparo(int x, int y) {
        super(x, y);
    }

    public void mover() {
        GameBoard gb = GameBoard.getGameBoard();
        int oldX = getX();
        int oldY = getY();
        int newY = oldY - 1;

        if (newY < 0) {
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Comprobar colisión
        Casilla ocupante = gb.getCasilla(oldX, newY);
        if (ocupante instanceof Enemigo) {
            EnemigoManager.getEnemigoManager().removeEnemigo((Enemigo) ocupante);
            detenerYBorrar(oldX, oldY);
            return;
        }

        // Actualizar posición en la matriz
        setY(newY);
        gb.actualizarPosicionDisparo(oldX, oldY, this);
    }

    private void detenerYBorrar(int x, int y) {
        DisparoManager.getDisparoManager().eliminarDisparo(this);
        GameBoard.getGameBoard().eliminarDisparo(x, y);
    }
}

package Model;

public abstract class Nave  {
    private boolean viva = true;
    private Composite pixeles;
    
    public Nave() {
       
    } 

    public void mover() {
    	pixeles.moverHD();
    	//Habría que hacer que dependiendo de la tecla pulsada se mueva uno u otro;
    	//Luego actualizar la casilla con el state.
    }
   /*public void mover(int dx, int dy) {
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
    }*/

    /*public void disparar() {
        int shotX = getX();
        int shotY = getY() - 2;
        if (shotY >= 0) {
            Disparo nuevoDisparo = new Disparo(shotX, shotY);
            DisparoManager.getDisparoManager().agregarDisparo(nuevoDisparo);
        }
    }*/

   

	public void removeNave() {
        viva = false;
    }

    public boolean estaViva() {
        return viva;
    }
}

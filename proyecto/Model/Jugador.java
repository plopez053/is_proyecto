package Model;

public class Jugador {
	private Nave nave;
	private static Jugador miJugador = null;
	
	private Jugador() {
		
	}
	
	public static Jugador getJugador() {
		if (miJugador == null) {
			miJugador = new Jugador();
		}
		return miJugador;
	}
	
	public Nave generate(String pTipo) {
		nave = NaveFactory.getNaveFactory().generate(pTipo);
		return nave;
	}
	

}

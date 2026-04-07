package Model;

public class casillaVacia implements EstadoCasilla {

	@Override
	public boolean esNave() {
		return false;
	}

	@Override
	public boolean estaOcupada() {
		return false;
	}

	@Override
	public boolean esEnemigo() {
		return false;
	}

	@Override
	public boolean esDisparo() {
		return false;
	}

}

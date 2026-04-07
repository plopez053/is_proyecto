package Model;

public class casillaDisparo implements EstadoCasilla {

	@Override
	public boolean esNave() {
		return false;
	}

	@Override
	public boolean estaOcupada() {
		return true;
	}

	@Override
	public boolean esEnemigo() {
		return false;
	}

	@Override
	public boolean esDisparo() {
		return true;
	}

}

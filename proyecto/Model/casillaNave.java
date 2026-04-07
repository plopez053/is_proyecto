package Model;

public class casillaNave implements EstadoCasilla{

	@Override
	public boolean esNave() {
		return true;
	}

	@Override
	public boolean estaOcupada() {
		// TODO Auto-generated method stub
		return true;
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

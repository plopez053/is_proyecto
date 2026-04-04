package Model;

public class NaveFactory {
	private static NaveFactory miNaveFactory = null;
	
	private NaveFactory() {
		
	}
	
	public static NaveFactory getNaveFactory() {
		if (miNaveFactory == null) {
			miNaveFactory = new NaveFactory();
		}
		return miNaveFactory;
	}
	
	public Nave generate(String pTipo) {
		Nave n = null;
		if (pTipo == "Red") {
			n = new Red();
		}else if (pTipo == "Green") {
			n = new Green();
		}else if (pTipo == "Blue") {
			n = new Blue();
		}
		return n;
	}
	
}

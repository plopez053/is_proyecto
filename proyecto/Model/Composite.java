package Model;

import java.util.ArrayList;

public class Composite implements Component{
	//No se como llamar a esta clase
	
	private ArrayList<Component> listaP;
	
	public Composite() {
		listaP = new ArrayList<Component>();
	}

	public void addComponente(Component pCom) {
		listaP.add(pCom);
	}
	
	public void deleteComponente(Component pCom) {
		listaP.remove(pCom);
	}
	
	@Override
	public void moverVI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moverVD() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moverHI() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moverHD() {
		// TODO Auto-generated method stub
		
	}

}

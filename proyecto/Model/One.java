package Model;

public class One implements Component {
    private int x;
    private int y;

    public One(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
package Model;

public class FinalBoss extends Nave {
    private int vidasRestantes = 3;

    public FinalBoss(int x, int y) {
        super(x, y);
        Composite cuerpo = new Composite();

        int[][] forma = {
            // Antenas
            {-3, -4}, {3, -4},

            // Cabeza con ojos
            {-6, -3}, {-5, -3}, {-4, -3}, {-2, -3}, {0, -3}, {2, -3}, {4, -3}, {5, -3}, {6, -3},

            // Cuerpo superior ancho
            {-8, -2}, {-7, -2}, {-6, -2}, {-5, -2}, {-4, -2}, {-3, -2}, {-2, -2}, {-1, -2},
            {0, -2}, {1, -2}, {2, -2}, {3, -2}, {4, -2}, {5, -2}, {6, -2}, {7, -2}, {8, -2},

            // Cuerpo con huecos (ojos)
            {-8, -1}, {-7, -1},
            {-5, -1}, {-4, -1}, {-3, -1},
            {-1, -1}, {0, -1}, {1, -1},
            {3, -1}, {4, -1}, {5, -1},
            {7, -1}, {8, -1},

            // Cuerpo central completo
            {-8, 0}, {-7, 0}, {-6, 0}, {-5, 0}, {-4, 0}, {-3, 0}, {-2, 0}, {-1, 0},
            {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {8, 0},

            // Boca (dientes alternos)
            {-8, 1}, {-6, 1}, {-4, 1}, {-2, 1}, {0, 1}, {2, 1}, {4, 1}, {6, 1}, {8, 1},

            // Patas
            {-8, 2}, {-6, 2}, {-5, 2}, {4, 2}, {5, 2}, {6, 2}, {8, 2},

            // Punta de patas
            {-9, 3}, {-7, 3}, {-5, 3}, {5, 3}, {7, 3}, {9, 3},
        };

        for (int[] d : forma) {
            cuerpo.addComponente(new Pixel(x + d[0], y + d[1], new CasillaBoss()));
        }
        this.setCuerpo(cuerpo);
    }
    public void recibirImpacto() {
        vidasRestantes--;
        System.out.println("Boss impactado, vidas: " + vidasRestantes);
        if (vidasRestantes <= 0) {
            removeNave();
            if (cuerpo != null) cuerpo.borrar();
            EnemigoManager.getEnemigoManager().notificarDestruccionBoss(this);
        }
    }
    public boolean estaDestruido() {
        return vidasRestantes <= 0;
    }
}

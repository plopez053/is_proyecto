package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DisparoManager { //TODO creo q esta clase tb sobra por lo q nos dijo ander, hay q hacerlo con el patrón strategy
    private static DisparoManager miDisparoManager;
    private List<Disparo> disparos;
    private Timer timer;

    private DisparoManager() {
        disparos = new ArrayList<>();
        iniciarTimer();
    }

    public static synchronized DisparoManager getDisparoManager() {
        if (miDisparoManager == null) {
            miDisparoManager = new DisparoManager();
        }
        return miDisparoManager;
    }

    public void agregarDisparo(Disparo d) {
        synchronized (disparos) {
            disparos.add(d);
        }
    }

    public void eliminarDisparo(Disparo d) {
        synchronized (disparos) {
            disparos.remove(d);
        }
    }

    public void iniciarTimer() {
        detenerTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moverDisparos();
            }
        }, 0, 50);
    }

    public void detenerTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void moverDisparos() {
        List<Disparo> copiaDisparos;
        synchronized (disparos) {
            copiaDisparos = new ArrayList<>(disparos);
        }
        for (Disparo d : copiaDisparos) {
            d.mover(); /
        }
    }
}

package commands.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Temporizador {
    private ScheduledExecutorService activeExecutorService;
    private ScheduledExecutorService activeAnalizadorTiempo;
    private Runnable tarea;
    private int segundosDelay;

    boolean contando = false;

    public Temporizador(int delay, Runnable tarea){
        segundosDelay = delay;
        this.tarea = tarea;
    }
    public void terminarConteo(){
        contando = false;
    }

    public void cambiarDelay(int delay){
        this.segundosDelay = delay;
    }

    public void empezar(){
        if(!contando) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(0);
            executorService.schedule(tarea, segundosDelay, TimeUnit.SECONDS);
            activeExecutorService = executorService;
            ScheduledExecutorService analizadorTiempo = Executors.newScheduledThreadPool(0);
            activeAnalizadorTiempo = analizadorTiempo;
            analizadorTiempo.schedule(this::terminarConteo, segundosDelay, TimeUnit.SECONDS);
            contando = true;
        }
    }

    public void parar(){
        if(contando){
            activeExecutorService.shutdownNow();
            activeAnalizadorTiempo.shutdownNow();
            contando = false;
        }

    }
}

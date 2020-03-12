/*package antispam;

import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.ExceptionLogger;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UsuarioAntiSpam {
    private User usuario;
    private int delay;
    private boolean estaMuteado = false;
    private Role rolMuteado;
    private ScheduledExecutorService ejecutor = Executors.newScheduledThreadPool(1);

    public UsuarioAntiSpam(Server unServer, User unUsuario, Role rolDado, int segundosDelay){
        usuario = unUsuario;
        delay = segundosDelay;

        rolMuteado = rolDado;
    }

    public void desactivarMute(){
        estaMuteado = false;
        rolMuteado.removeUser(usuario).exceptionally(ExceptionLogger.get());

    }

    public void silenciar(){

        rolMuteado.addUser(usuario).exceptionally(ExceptionLogger.get());
        usuario.addRole(rolMuteado,"antispam mode").exceptionally(ExceptionLogger.get());
        ejecutor.schedule(this::desactivarMute,delay, TimeUnit.SECONDS);
        estaMuteado = true;
    }

    public boolean estaMuteado(){
        return estaMuteado;
    }



}*/

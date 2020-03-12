/*package antispam;

import commands.util.Temporizador;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiSpamListener implements MessageCreateListener {

    Map<User, Temporizador> usuarios = new HashMap<>();

    final String NombreRolMuteado = "antispam";
    private int tiempoPausa = 5;
    private Role rolMuteo;
    private long idRolMuteo;
    private Server servidor;

    private Permissions crearPermisosRolMuteado(){
        PermissionsBuilder permissionsBuilder = new PermissionsBuilder();
        permissionsBuilder.setAllUnset();
        permissionsBuilder.setDenied(PermissionType.SEND_MESSAGES);
        permissionsBuilder.setAllowed(PermissionType.READ_MESSAGES);
        permissionsBuilder.setAllowed(PermissionType.SPEAK);
        permissionsBuilder.setAllowed(PermissionType.STREAM);
        permissionsBuilder.setAllowed(PermissionType.USE_VOICE_ACTIVITY);
        return permissionsBuilder.build();
    }

    private void crearRolMuteo(){
        for(Role rol : servidor.getRolesByName(NombreRolMuteado)){
            prepararRol(rol);
            rolMuteo = rol;
            idRolMuteo = rolMuteo.getId();
            return;
            }

        PermissionsBuilder permisosDefault = new PermissionsBuilder();
        servidor.createRoleBuilder().setAuditLogReason("crear rol antispam").setName(NombreRolMuteado)
                .setPermissions(permisosDefault.setAllUnset().build())
                .setColor(Color.MAGENTA).create().thenAccept(role -> {

            prepararRol(role);

            rolMuteo = role;
            idRolMuteo = rolMuteo.getId();
        });

    }

    private void prepararRol(Role role){
        List<Role> roles = new ArrayList<>(servidor.getRoles());
        if(role.getPosition() != 0){
            roles.remove(role);
            roles.add(0,role);
            servidor.createUpdater().reorderRoles(roles).update();
        }
        for(TextChannel canal : servidor.getTextChannels()){
            canal.asServerChannel().ifPresent(serverChannel -> {
                serverChannel.createUpdater().addPermissionOverwrite(role,crearPermisosRolMuteado()).update();
            });
        }

    }

    public AntiSpamListener(int segundosEntreMensajes, Server unServidor){
        tiempoPausa = segundosEntreMensajes;
        servidor = unServidor;
        crearRolMuteo();
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        User usuarioAutor = event.getMessageAuthor().asUser().get();
        Server server = event.getServer().get();
        ServerUpdater serverUpdater = server.createUpdater();
        if(!usuarios.containsKey(usuarioAutor)){
            Temporizador temporizador = new Temporizador(tiempoPausa);
            usuarios.put(usuarioAutor, temporizador);
        }

        if(!usuarios.get(usuarioAutor).estaContando()){
            prepararRol(rolMuteo);
            System.out.println("ql wn");
            serverUpdater.addRoleToUser(usuarioAutor,server.getRoleById(idRolMuteo).get()).update().exceptionally(ExceptionLogger.get());
            Runnable desmutear = ()-> serverUpdater.removeRoleFromUser(usuarioAutor,server.getRoleById(idRolMuteo).get()).update().exceptionally(ExceptionLogger.get());

            usuarios.get(usuarioAutor).empezar(desmutear);

        }
    }
}
*/
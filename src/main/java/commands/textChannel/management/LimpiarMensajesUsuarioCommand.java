package commands.textChannel.management;

import commands.Command;
import commands.admin.AdminLevelComponent;
import commands.util.StringsUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class LimpiarMensajesUsuarioCommand extends Command {

    public LimpiarMensajesUsuarioCommand(){
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"limpiarmensajesusuario"};
        description = "Busca entre los ultimos X mensajes (max 100) del canal los enviados por el usuario dado y los elimina.\n " +
                "Si no se especifica el número busca entre los últimos 100.\n" +
                "Si el usuario tiene espacios en su nombre encerrarlo entre comillas(Ej. \"Esteban Quito\") ";
        parameters = new String[]{"<nombre usuario>", "<cantidad mensajes>"};
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        int numeroMensajes = 100;

        MessageChannel canal = event.getChannel();

        if (stringParametros.equals("")){
            canal.sendMessage("`necesito un nombre, zapallo.`").queue();
            return;
        }

        String[] parametros = StringsUtil.splitIgnorandoComillas(stringParametros, ' ');
        String nombreUsuario = parametros[0].toLowerCase();

            if(parametros.length > 1){
                try{
                    numeroMensajes = Integer.parseInt(parametros[1]);
                }catch(NumberFormatException nfe){
                    event.getChannel().sendMessage("`Eso no es un número bolu2`").queue();
                    return;
                }
            }

        Guild servidor = event.getGuild();

        Set<Member> listaUsuariosConNombreDado = new HashSet<>(servidor.getMembersByName(nombreUsuario, true));
        listaUsuariosConNombreDado.addAll(servidor.getMembersByNickname(nombreUsuario, true));
        if(listaUsuariosConNombreDado.isEmpty()){
            canal.sendMessage("`No hay usuarios con el nombre` **"+nombreUsuario+"** `en el servidor.`").queue();
            return;
        }

            canal.getHistoryBefore(event.getMessage(),numeroMensajes).queue(
                    messageHistory -> {
                        List<Message> listaMensajesObtenidos = messageHistory.getRetrievedHistory();
                        List<Message> listaMensajesAEliminar = new ArrayList<>();
                        for(Message mensaje: listaMensajesObtenidos){

                            for(Member miembro : listaUsuariosConNombreDado){
                                if(mensaje.getMember().getIdLong() == miembro.getIdLong()){
                                    if(!listaMensajesAEliminar.contains(mensaje)){
                                        listaMensajesAEliminar.add(mensaje);
                                    }
                                    break;
                                }
                            }
                            }
                        int mensajesAEliminar = listaMensajesAEliminar.size();
                        canal.purgeMessages(listaMensajesAEliminar);
                        event.getChannel().sendMessage("**"+mensajesAEliminar+ "** mensajes de "+"`"+ parametros[0]+"` fueron deletea2.").queue();
                        });

    }


}

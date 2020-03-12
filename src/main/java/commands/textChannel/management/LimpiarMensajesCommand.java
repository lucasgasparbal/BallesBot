package commands.textChannel.management;


import commands.Command;
import commands.admin.AdminLevelComponent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LimpiarMensajesCommand extends Command {
    public LimpiarMensajesCommand(){
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"limpiarmensajes"};
        description = "Limpia los últimos X mensajes del canal de texto donde es invocado (max 100). Si no se especifica el número borra 100.";
        parameters = new String[]{"<Número Mensajes>"};
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        int cantidadMensajes = 100;
        MessageChannel canal = event.getChannel();

        if(!stringParametros.equals("")){
            String[] parametros = stringParametros.split(" ",2);
            if(parametros.length >= 1){
                try{
                    cantidadMensajes = Integer.parseInt(parametros[0]);
                }catch(NumberFormatException nfe){
                    canal.sendMessage("`Eso no es un número pelotu2`").queue();
                    return;
                }
            }
        }
        canal.getHistoryBefore(event.getMessage(), cantidadMensajes).queue(messageHistory ->{
            int cantidadMensajesBorrados = messageHistory.size();
            canal.purgeMessages(messageHistory.getRetrievedHistory());
            canal.sendMessage("Deletea2 **"+String.valueOf(cantidadMensajesBorrados)+"** mensajes.").queue();
        }
        );

    }
}

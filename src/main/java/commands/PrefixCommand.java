package commands;

import commands.Command;
import commands.admin.AdminLevelComponent;
import commands.util.PrefixesHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrefixCommand extends Command {

    PrefixesHandler prefixesHandler;

    public PrefixCommand(PrefixesHandler aPrefixesHandler){
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"prefijo"};
        description = "cambia el prefijo utilizado en el servidor para acceder a los comandos del bot. Para saber el prefijo en el servidor mencionar al bot(@BALLES-BOT)";
        parameters = new String[]{"<nuevo prefijo>"};
        prefixesHandler = aPrefixesHandler;
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        if(stringParametros.equals("")){
            event.getChannel().sendMessage("`necesito al menos un caracter para establecer un nuevo prefijo`").queue();
            return;
        }

        Long serverId = event.getGuild().getIdLong();
        prefixesHandler.changePrefixForServer(stringParametros, serverId);
        event.getChannel().sendMessage("cambiado el prefijo del server a **"+prefixesHandler.getPrefixFromServer(serverId)+"**").queue();
    }
}

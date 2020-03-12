package commands.audio;

import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SearchComand extends AudioCommand {
    public SearchComand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"search"};
        parameters = new String[]{"<identificador busqueda>"};
        description = "muestra los resultados de la busqueda en Youtube para que luego el usuario eliga qué video agregar a la lista de reproducción";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        if(stringParametros.isEmpty()){
            event.getChannel().sendMessage("```Necesito al menos una palabra para iniciar una busqueda.```").queue();
        }else{
            audioPlayerSendManager.showSearchResults(stringParametros, event);
        }
    }
}

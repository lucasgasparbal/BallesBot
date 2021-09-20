package commands.audio;

import commands.Command;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ClearCommand extends AudioCommand {

    public ClearCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{ "clearqueue","clear","limpiar"};
        description = "elimina todos los audios restantes de la cola de reproducción";


    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        audioPlayerSendManager.clearTrackQueueForServerOfEvent(event);
        event.getChannel().sendMessage(":shower:Lista de reproducción limpiada.:shower:").queue();
    }
}

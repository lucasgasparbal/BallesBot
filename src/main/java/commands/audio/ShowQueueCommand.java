package commands.audio;

import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShowQueueCommand extends  AudioCommand {

    public ShowQueueCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"showqueue", "queue"};
        description = "Muestra los próximos audios en la lista de reproducción";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        if(!event.getGuild().getAudioManager().isConnected()){
            event.getChannel().sendMessage("El reproductor de audio no está activo.").queue();
        }else{
            audioPlayerSendManager.showTrackQueue(event);
        }
    }
}

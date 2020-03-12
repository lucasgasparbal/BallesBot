package commands.audio;

import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class SkipCommand extends AudioCommand {

    public SkipCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"skip", "s"};
        description = "Deja de reproducir el audio actual y si hay audio en la lista de reproducción reproduce el siguiente.";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if(!audioManager.isConnected()){
            event.getChannel().sendMessage("```No estoy conectado a ningun canal de voz.```").queue();
        }else{
            audioPlayerSendManager.skipTrackForServerOfEvent(event,sizeOfHumanMembersInVoiceChat(audioManager.getConnectedChannel()));
        }
    }
}

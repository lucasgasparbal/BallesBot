package commands.audio;

import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PauseCommand extends AudioCommand {

    public PauseCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"pause","resume"};
        description = "pausa el reproductor de sonidos del bot. Si el reproductor ya fue pausado lo despausa.";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if(!audioManager.isConnected()){
            event.getChannel().sendMessage("```No estoy conectado en ningún canal de voz.```").queue();
        }else{
            audioPlayerSendManager.pausePlayerForServerOfEvent(event);
        }
    }
}

package commands.audio;

import Exceptions.audio.NotConnectedToVoiceChannelException;
import commands.admin.AdminLevelComponent;
import commands.util.VoiceChannelVotingBooth;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class DisconnectCommand extends AudioCommand{

    public DisconnectCommand(AudioPlayerSendManager audioPlayerSendManager) {
        super(audioPlayerSendManager);
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"disconnect", "dc"};
        description = "Desconecta al bot del canal de voz.";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        AudioManager guildAudioManager = event.getGuild().getAudioManager();

        try {
            audioPlayerSendManager.disconnectFromVoiceChannel(guildAudioManager,event);
        } catch (NotConnectedToVoiceChannelException e) {
            event.getChannel().sendMessage("```No estoy conectado a ningún canal de voz```").queue();
        }

    }
}

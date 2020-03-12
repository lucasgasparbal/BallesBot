package commands.audio;

import Exceptions.audio.NotConnectedToVoiceChannelException;
import commands.util.VoiceChannelVotingBooth;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class DisconnectCommand extends AudioCommand{

    VoiceChannelVotingBooth disconnectVotingBooth = new VoiceChannelVotingBooth();

    public DisconnectCommand(AudioPlayerSendManager audioPlayerSendManager) {
        super(audioPlayerSendManager);
        names = new String[]{"disconnect"};
        description = "Desconecta al bot del canal de voz.";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        AudioManager guildAudioManager = event.getGuild().getAudioManager();
        if(!guildAudioManager.isConnected()){
            event.getChannel().sendMessage("```No estoy conectado a ningún canal de voz```").queue();
            return;
        }
        VoiceChannel connectedVoiceChannel = guildAudioManager.getConnectedChannel();

        disconnectVotingBooth.updateVotesIfNeeded(connectedVoiceChannel.getIdLong(),sizeOfHumanMembersInVoiceChat(connectedVoiceChannel));

        boolean callingUserIsAdmin = event.getMember().hasPermission(Permission.MANAGE_SERVER);
        if(!callingUserIsAdmin){
            disconnectVotingBooth.addVote();
        }

        if(callingUserIsAdmin || disconnectVotingBooth.votingCompleted() ){
            try {
                disconnectFromVoiceChannel(guildAudioManager,event);
            } catch (NotConnectedToVoiceChannelException e) {
                event.getChannel().sendMessage("```No estoy conectado a ningún canal de voz```").queue();
            }
            disconnectVotingBooth.resetBooth();
        }else{
            event.getChannel().sendMessage(String.format(":passport_control: **%d/%d Votos para desconectar al bot del canal.**",
                    disconnectVotingBooth.getCurrentVotes(),disconnectVotingBooth.getNeededVotes())).queue();
        }


    }
}

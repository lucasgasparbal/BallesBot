package commands.audio;

import Exceptions.audio.NotConnectedToVoiceChannelException;
import Exceptions.audio.VoiceChannelNotFoundException;
import commands.Command;
import commands.util.audio.AudioPlayerSendHandler;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.LinkedList;

public abstract class AudioCommand extends Command {
    AudioPlayerSendManager audioPlayerSendManager;

    public AudioCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        audioPlayerSendManager = anAudioPlayerSendManager;
    }

    protected void joinVoiceChannel(AudioManager audioManager, MessageReceivedEvent event, VoiceChannel voiceChannel) throws VoiceChannelNotFoundException {
        if (voiceChannel != null) {
            audioPlayerSendManager.setHandlerForEvent(event);
            audioManager.openAudioConnection(voiceChannel);

        }else{
            throw new VoiceChannelNotFoundException();
        }
    }

    protected void disconnectFromVoiceChannel(AudioManager audioManager, MessageReceivedEvent event) throws NotConnectedToVoiceChannelException {
        if(audioManager.isConnected()){
            audioManager.closeAudioConnection();
            audioPlayerSendManager.disconnectHandlerForEvent(event);
        }else{
            throw new NotConnectedToVoiceChannelException();
        }

    }

    public boolean isAudioCommand(){ return true; }

    protected int sizeOfHumanMembersInVoiceChat(VoiceChannel voiceChannel){
        int humansInVoiceChannel = 0;
        for(Member member : voiceChannel.getMembers()){
            if(!member.getUser().isBot()){
                humansInVoiceChannel++;
            }
        }

        return  humansInVoiceChannel;
    }
}

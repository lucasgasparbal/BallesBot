package commands.util.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.MessageChannel;

public abstract class CustomAudioLoadResultHandler implements AudioLoadResultHandler {

    private MessageChannel channel;
    AudioPlayerSendHandler audioPlayer;

    public CustomAudioLoadResultHandler(MessageChannel messageChannel, AudioPlayerSendHandler audioPlayerSendHandler){
        channel = messageChannel;
        audioPlayer = audioPlayerSendHandler;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        audioPlayer.addTrackToPlayerQueue(audioTrack, channel);
        System.out.println("f");
    }



    @Override
    public void loadFailed(FriendlyException e) {
        channel.sendMessage("``` No se pudo cargar el audio pedido. ```").queue();
    }
}

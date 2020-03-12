package commands.util.audio;

import Exceptions.audio.search.NoTrackOnIndexException;
import Exceptions.audio.search.UserHasSearchActiveException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import commands.util.Temporizador;
import commands.util.VoiceChannelVotingBooth;
import commands.util.audio.search.GuildAudioSearchs;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.nio.ByteBuffer;
import java.util.List;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final String CancelString = "cancel";

    private final AudioPlayer audioPlayer;
    private String audioTrackPlayedToBeSkippedIdentifier = "";
    private TrackScheduler trackScheduler;
    private AudioFrame lastFrame;
    private GuildAudioSearchs guildAudioSearchs = new GuildAudioSearchs();
    VoiceChannelVotingBooth skipVotingBooth = new VoiceChannelVotingBooth();

    public AudioPlayerSendHandler(AudioPlayer audioPlayer, MessageChannel messageChannel, Temporizador autoDisconnectTimer) {
        this.audioPlayer = audioPlayer;
        trackScheduler = new TrackScheduler(messageChannel,autoDisconnectTimer);
        audioPlayer.addListener(trackScheduler);
    }

    @Override
    public boolean canProvide() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }


    public void addTrackToPlayerQueue(AudioTrack audioTrack, MessageChannel messageChannel){
        if(audioPlayer.getPlayingTrack() == null){
            trackScheduler.updateMessageChannel(messageChannel);
            audioPlayer.playTrack(audioTrack);
        }else{
            trackScheduler.addTrack(audioTrack, messageChannel);
        }

    }

    public void addPlaylistToPlayerQueue(AudioPlaylist audioPlaylist, MessageChannel messageChannel) {
        if(audioPlayer.getPlayingTrack() == null){
            trackScheduler.updateMessageChannel(messageChannel);
            audioPlayer.playTrack(audioPlaylist.getTracks().remove(0));
        }
        trackScheduler.addPlaylist(audioPlaylist, messageChannel);
    }

    public void addSearchResultToPlayerQueue(AudioPlaylist searchResults, MessageChannel messageChannel){
        addTrackToPlayerQueue(searchResults.getTracks().get(0), messageChannel);
    }

    public void clearTrackQueue(){
        trackScheduler.clearQueue();
    }

    public void skipTrack(MessageChannel messageChannel, boolean callingUserIsAdmin,long voiceChannelId, double usersOnChannel){


        trackScheduler.updateMessageChannel(messageChannel);
        AudioTrack playingTrack = audioPlayer.getPlayingTrack();


        if(playingTrack == null){
            messageChannel.sendMessage("```No estoy transmitiendo en estos momentos.```").queue();
        }else{
            int skipVotesNeeded = (int) Math.ceil(usersOnChannel/2);
            skipVotingBooth.updateVotesIfNeeded(voiceChannelId,skipVotesNeeded);

                if(!audioTrackPlayedToBeSkippedIdentifier.equals(playingTrack.getIdentifier())){
                skipVotingBooth.resetBooth();
                audioTrackPlayedToBeSkippedIdentifier = playingTrack.getIdentifier();
                }

            if(!callingUserIsAdmin){
                skipVotingBooth.addVote();

            }

            if(callingUserIsAdmin || skipVotingBooth.votingCompleted() ){
                messageChannel.sendMessage(":next_track: **REPRODUCCION OMITIDA.**").queue();
                audioPlayer.stopTrack();
                skipVotingBooth.resetBooth();
            }else{
                messageChannel.sendMessage(String.format(":passport_control: **%d/%d votos para saltear la reproducción actual.**",
                        skipVotingBooth.getCurrentVotes(),skipVotingBooth.getNeededVotes())).queue();
            }

        }


    }

    public void changePlayerPausedState(MessageChannel messageChannel){
        trackScheduler.updateMessageChannel(messageChannel);
        audioPlayer.setPaused(!audioPlayer.isPaused());
    }

    public void disconnectStop(){
        trackScheduler.clearQueue();
        trackScheduler.stopAutoDisconnectTimer();
        audioPlayer.stopTrack();
    }

    public void showQueueOnChannel(MessageChannel messageChannel){
        trackScheduler.showQueue(messageChannel);
    }

    public void addSearchResults(Long userRequesterId, List<AudioTrack> searchResults ) throws UserHasSearchActiveException {
        guildAudioSearchs.addSearch(userRequesterId,searchResults);
    }

    public void checkSearchesBy(Long memberId, String messageContent, MessageChannel messageChannel) {
        if(!guildAudioSearchs.containsSearchByMember(memberId)){
            return;
        }
        try{
            int trackIndex = Integer.parseInt(messageContent.strip()) - 1;
            addTrackToPlayerQueue(guildAudioSearchs.getSearchResultForUser(memberId, trackIndex),messageChannel);

        }catch(NumberFormatException e){
            if(messageContent.strip().equalsIgnoreCase("cancel")){
                guildAudioSearchs.destroySearch(memberId);
                messageChannel.sendMessage(":no_entry_sign:**BUSQUEDA CANCELADA**:no_entry_sign:").queue();
            }
        } catch (NoTrackOnIndexException e) {
            messageChannel.sendMessage("```Ingrese un número válido.```").queue();
        }
    }

    public void startAutoDisconnectTimer(){
        trackScheduler.startAutoDisconnectTimer();
    }

    public void stopAutoDisconnectTimer(){
        trackScheduler.stopAutoDisconnectTimer();
    }
}
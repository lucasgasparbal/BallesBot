package commands.util.audio;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import commands.util.Temporizador;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {

    final private int MaxNumberOfShowedTracks = 15;
    private Temporizador autoDisconnectTimer;
    private MessageChannel messageChannel;
    private LinkedList<AudioTrack> audioTrackQueue = new LinkedList<>();

    public TrackScheduler(MessageChannel aMessageChannel,Temporizador autoDisconnectTimer){
        this.autoDisconnectTimer = autoDisconnectTimer;
        messageChannel = aMessageChannel;
    }
    @Override
    public void onPlayerPause(AudioPlayer player) {
        messageChannel.sendMessage(":pause_button: **Reproductor pausa2.**").queue();
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        messageChannel.sendMessage(":arrow_forward: **Reanudando reproductor.**").queue();
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        messageChannel.sendMessage(":musical_note: **Ahora reproduciendo**:").setEmbeds(displayTrackInfo(track)).queue();
        autoDisconnectTimer.parar();
        // A track started playing
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            playTrackIfAvailable(player);
        }else if(endReason == AudioTrackEndReason.STOPPED){
            playTrackIfAvailable(player);
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        messageChannel.sendMessage("```Hubo un error con el audio: "+track.getInfo().title+"```").queue();
        System.out.println("cama arriba");
        playTrackIfAvailable(player);
        exception.printStackTrace();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        messageChannel.sendMessage("```Hubo un error con el audio: "+track.getInfo().title+"```").queue();
        System.out.println("cama abajo");
        playTrackIfAvailable(player);
    }

    public void addTrack(AudioTrack audioTrack, MessageChannel aMessageChannel){
        messageChannel = aMessageChannel;
        audioTrackQueue.addLast(audioTrack);
        this.messageChannel.sendMessage(":ok: **Agregado con éxito: **").setEmbeds(displayTrackInfo(audioTrack)).queue();
    }

    public void addPlaylist(AudioPlaylist playlist, MessageChannel aMessageChannel){
        messageChannel = aMessageChannel;
        audioTrackQueue.addAll(playlist.getTracks());
        this.messageChannel.sendMessage(":cool: **Agregada la lista de reproducción: **"+playlist.getName()).queue();
    }

    private void playTrackIfAvailable(AudioPlayer player){
        if(!audioTrackQueue.isEmpty()){
            player.playTrack(audioTrackQueue.pollFirst());
        }else{
            autoDisconnectTimer.empezar();
        }
    }

    private MessageEmbed displayTrackInfo(AudioTrack track){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        AudioTrackInfo audioTrackInfo = track.getInfo();

        embedBuilder.setTitle(audioTrackInfo.title, audioTrackInfo.uri);
        embedBuilder.setDescription("\nduration: "+formattedTime(audioTrackInfo.length));
        embedBuilder.setColor(Color.DARK_GRAY);
        embedBuilder.setAuthor("BALLES-BOT");

        return embedBuilder.build();
    }

    public void clearQueue(){
        audioTrackQueue.clear();
    }

    public void showQueue(MessageChannel messageChannel){
        updateMessageChannel(messageChannel);
        if(audioTrackQueue.isEmpty()){
            this.messageChannel.sendMessage("**No hay audios en la lista de reproducción**").queue();
        }else{
            int trackCount = 0;
            this.messageChannel.sendMessage(":play_pause: **A continuación:\n**").queue();
            StringBuilder stringBuilder = new StringBuilder();
            for(AudioTrack audioTrack : audioTrackQueue){
                if(trackCount >= MaxNumberOfShowedTracks){
                    return;
                }
                if(stringBuilder.length() > 1000){
                    messageChannel.sendMessage(stringBuilder.toString()).queue();
                    stringBuilder = new StringBuilder();
                }
                trackCount ++;
                stringBuilder.append("\n__**").append(trackCount).append(".").append(audioTrack.getInfo().title).append("**__ \t — \t").append(formattedTime(audioTrack.getDuration()));

            }
            this.messageChannel.sendMessage(stringBuilder.toString()).queue();
        }
    }

    private String formattedTime(Long milliseconds){
        long seconds = (milliseconds/1000) % 60;
        long minutes = ((milliseconds / (1000*60)) % 60);
        long hours   = ((milliseconds / (1000*60*60)) % 24);
       return String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    public void updateMessageChannel(MessageChannel messageChannel){
        this.messageChannel = messageChannel;
    }

    public void setAutoDisconnectTimer(Temporizador newTimer){
        autoDisconnectTimer.parar();
        autoDisconnectTimer = newTimer;
    }

    public void startAutoDisconnectTimer() {
        autoDisconnectTimer.empezar();
    }

    public void stopAutoDisconnectTimer() {
        autoDisconnectTimer.parar();
    }
}

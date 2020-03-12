package commands.util.audio;

import Exceptions.audio.WordAlreadyBannedInGuildException;
import Exceptions.audio.WordNotBannedInGuildException;
import Exceptions.audio.search.NoTrackOnIndexException;
import Exceptions.audio.search.UserHasSearchActiveException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import commands.util.Temporizador;
import commands.util.TimeFormat;
import commands.util.audio.search.GuildAudioSearchs;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.*;

public class AudioPlayerSendManager {
    final int DelayForAutoDisconnect = 300;
    final String BannedWordWarning = ":raised_hand::cop:**ALTO AHÍ RUFIÁN**:cop::back_of_hand:\n " +
            "EL VIDEO QUE SOLICITASTE CONTIENE UNA O MÁS PALABRAS BANEADAS EN ESTE SERVER";
    private final int VideoSearchLimit = 15;
    Map<Long, AudioPlayerSendHandler> serverPlayerHandlers = new HashMap<>();
    ServerPlayerBannedWords serverPlayerBannedWords = new ServerPlayerBannedWords();
    Map<Long, GuildAudioSearchs> guildAudioSearchs = new HashMap<>();
    AudioPlayerManager audioPlayerManager;

    public AudioPlayerSendManager(){
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
    }


    public void setHandlerForEvent(MessageReceivedEvent event){
        AudioPlayerSendHandler audioPlayerSendHandler = getAudioPlayerSendHandlerForServerOfEvent(event);
        event.getGuild().getAudioManager().setSendingHandler(audioPlayerSendHandler);
        audioPlayerSendHandler.startAutoDisconnectTimer();
    }

    private AudioPlayerSendHandler getAudioPlayerSendHandlerForServerOfEvent(MessageReceivedEvent event){
        Long serverId = event.getGuild().getIdLong();
        if(!serverPlayerHandlers.containsKey(serverId)){
            Temporizador autoDisconnecTimer = new Temporizador(DelayForAutoDisconnect,()->autoDisconnectFromGuild(event.getGuild().getAudioManager()));
            serverPlayerHandlers.put(serverId, new AudioPlayerSendHandler(audioPlayerManager.createPlayer(),event.getChannel(),autoDisconnecTimer));
        }
        return serverPlayerHandlers.get(serverId);
    }

    public void addTrackToQueueForServerOfEvent(String trackIdentifier, MessageReceivedEvent event){

        MessageChannel messageChannel = event.getChannel();
        AudioPlayerSendHandler audioPlayerSendHandler = getAudioPlayerSendHandlerForServerOfEvent(event);
        Long guildId = event.getGuild().getIdLong();
        String identifier = trackIdentifier.trim();
        if(serverPlayerBannedWords.stringFromGuildHasBannedWords(guildId,identifier)){
            messageChannel.sendMessage(BannedWordWarning).queue();
            return;
        }
        audioPlayerManager.loadItem(identifier,
                new CustomAudioLoadResultHandler(messageChannel, audioPlayerSendHandler) {

                    @Override
                    public void trackLoaded(AudioTrack audioTrack){
                        if(serverPlayerBannedWords.stringFromGuildHasBannedWords(guildId,audioTrack.getInfo().title)){
                            messageChannel.sendMessage(BannedWordWarning).queue();
                            return;
                        }
                        audioPlayerSendHandler.addTrackToPlayerQueue(audioTrack,messageChannel);
                    }
                    @Override
                    public void playlistLoaded(AudioPlaylist audioPlaylist) {
                        audioPlayerSendHandler.addPlaylistToPlayerQueue(audioPlaylist, messageChannel);
                    }

                    @Override
                    public void noMatches() {
                        audioPlayerManager.loadItem("ytsearch: " + trackIdentifier.strip(), new CustomAudioLoadResultHandler(messageChannel,audioPlayerSendHandler) {
                            @Override
                            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                                AudioTrack audioTrack = audioPlaylist.getTracks().get(0);
                                if(serverPlayerBannedWords.stringFromGuildHasBannedWords(guildId,audioTrack.getInfo().title)){
                                    messageChannel.sendMessage(BannedWordWarning).queue();
                                    return;
                                }
                                audioPlayerSendHandler.addTrackToPlayerQueue(audioTrack,messageChannel);
                            }

                            @Override
                            public void noMatches() {
                                messageChannel.sendMessage("``` No se encontraron resultados con: "+trackIdentifier.trim()+"```").queue();
                            }
                        });
                    }
                });

    }

    private GuildAudioSearchs getGuildAudioSearchsFor(Long guildId){
        if(!guildAudioSearchs.containsKey(guildId)){
            guildAudioSearchs.put(guildId,new GuildAudioSearchs());
        }

        return guildAudioSearchs.get(guildId);
    }
    public void showSearchResults(String searchTerms, MessageReceivedEvent event){
        String correctedSearchTerms = searchTerms.strip();
        MessageChannel messageChannel = event.getChannel();

        if(serverPlayerBannedWords.stringFromGuildHasBannedWords(event.getGuild().getIdLong(),correctedSearchTerms)){
            messageChannel.sendMessage(BannedWordWarning).queue();
            return;
        }
        audioPlayerManager.loadItem("ytsearch: "+correctedSearchTerms, new CustomAudioLoadResultHandler(messageChannel,serverPlayerHandlers.get(event.getGuild().getIdLong())) {
            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                Long guildId = event.getGuild().getIdLong();
                Long memberId = event.getMember().getIdLong();
                List<AudioTrack> searchResults = audioPlaylist.getTracks();
                searchResults = cleanBannedWordsAudioTracks(guildId,searchResults);
                try{

                    getGuildAudioSearchsFor(guildId).addSearch(memberId,searchResults);

                } catch (UserHasSearchActiveException e) {
                    messageChannel.sendMessage("```Ya hay una búsqueda tuya activa en el servidor, tenés que completarla o cancelarla```").queue();
                    return;
                }

                int trackCount = 0;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("__**RESULTADOS**__");
                if(searchResults.size() == 0){
                    messageChannel.sendMessage("```No hay resultados de busqueda para:"+correctedSearchTerms+"```").queue();
                    return;
                }

                for(int i = 0; i < searchResults.size(); i++){
                    trackCount++;
                    if(trackCount > VideoSearchLimit){
                        break;
                    }
                    if(stringBuilder.length() > 1500){
                        messageChannel.sendMessage(stringBuilder.toString()).queue();
                        stringBuilder = new StringBuilder();
                    }
                    stringBuilder.append("\n__**").append(trackCount).append(".").append(searchResults.get(i).getInfo().title).append("**__ \t — \t").append(TimeFormat.hoursMinutesSeconds(searchResults.get(i).getDuration()));
                }
                messageChannel.sendMessage(stringBuilder.toString()).queue();
                messageChannel.sendMessage(":information_source:**Ingrese el número del video que desea agregar a la lista de reproducción o escriba cancel para cancelar la operación**:information_source:").queue();
            }

            @Override
            public void noMatches() {
                messageChannel.sendMessage("```No hay resultados de busqueda para:"+correctedSearchTerms+"```").queue();
            }
        });

    }
    public void clearTrackQueueForServerOfEvent(MessageReceivedEvent event){
        getAudioPlayerSendHandlerForServerOfEvent(event).clearTrackQueue();
    }

    public void skipTrackForServerOfEvent(MessageReceivedEvent event, int currentVoiceChannelMembers){
        Member callingMember = event.getMember();
        VoiceChannel voiceChannel = event.getGuild().getAudioManager().getConnectedChannel();
        getAudioPlayerSendHandlerForServerOfEvent(event).skipTrack(event.getChannel(),callingMember.hasPermission(Permission.MANAGE_SERVER),voiceChannel.getIdLong(),currentVoiceChannelMembers);
    }

    public void pausePlayerForServerOfEvent(MessageReceivedEvent event){
        getAudioPlayerSendHandlerForServerOfEvent(event).changePlayerPausedState(event.getChannel());
    }

    public void showTrackQueue(MessageReceivedEvent event){
        getAudioPlayerSendHandlerForServerOfEvent(event).showQueueOnChannel(event.getChannel());
    }

    public void checkAudioSearchs(MessageReceivedEvent event) {

        Long memberId = event.getMember().getIdLong();
        Long guildId = event.getGuild().getIdLong();
        MessageChannel messageChannel = event.getChannel();
        String messageContent = event.getMessage().getContentStripped();
        GuildAudioSearchs eventGuildAudioSearch = getGuildAudioSearchsFor(guildId);
        if(!eventGuildAudioSearch.containsSearchByMember(memberId)){
            return;
        }
        try{

            int trackIndex = Integer.parseInt(messageContent) - 1;

            AudioManager audioManager = event.getGuild().getAudioManager();
            if(!event.getGuild().getAudioManager().isConnected()){

                VoiceChannel userVoiceChannel = event.getMember().getVoiceState().getChannel();
                if(userVoiceChannel == null){
                    messageChannel.sendMessage("```No Estoy conectado a ningún canal. Conectate a uno o conectame con el comando \"join\"```").queue();
                    eventGuildAudioSearch.destroySearch(memberId);
                    return;
                }else{
                    audioManager.openAudioConnection(userVoiceChannel);
                }
            }


            getAudioPlayerSendHandlerForServerOfEvent(event).addTrackToPlayerQueue(eventGuildAudioSearch.getSearchResultForUser(memberId,trackIndex),messageChannel);


        }catch(NumberFormatException e){
            if(messageContent.strip().equalsIgnoreCase("cancel")){
                eventGuildAudioSearch.destroySearch(memberId);
                messageChannel.sendMessage(":no_entry_sign:**BUSQUEDA CANCELADA**:no_entry_sign:").queue();
            }
        } catch (NoTrackOnIndexException e){
            messageChannel.sendMessage("```Ingrese un número válido.```").queue();
        }

    }

    public void addBannedWordToServer(String word, Long guildId) throws WordAlreadyBannedInGuildException {
        serverPlayerBannedWords.addBannedWordToGuild(guildId, word);
    }

    public void deleteBannedWordFromServer(String word, Long guildId) throws WordNotBannedInGuildException {
        serverPlayerBannedWords.deleteBannedWordFromGuild(guildId,word);
    }

    public List<AudioTrack> cleanBannedWordsAudioTracks(Long guildId, List<AudioTrack> audioTracks){
        List<AudioTrack> returnList = new LinkedList<>();
        for(AudioTrack audioTrack : audioTracks){
            if(!serverPlayerBannedWords.stringFromGuildHasBannedWords(guildId, audioTrack.getInfo().title)){
                returnList.add(audioTrack);
            }
        }
        return  returnList;
    }

    public Collection<String> getBannedWordsFromAudioPlayer(Long guildId){
        return serverPlayerBannedWords.getGuildBannedWords(guildId);
    }

    public void autoDisconnectFromGuild(AudioManager audioManager){
        if(audioManager.isConnected()){
            audioManager.closeAudioConnection();
            System.out.println("doh!");
        }
    }

    public void disconnectHandlerForEvent(MessageReceivedEvent event) {
        getAudioPlayerSendHandlerForServerOfEvent(event).disconnectStop();
    }
}

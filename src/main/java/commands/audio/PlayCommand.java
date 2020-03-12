package commands.audio;

import Exceptions.audio.VoiceChannelNotFoundException;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends AudioCommand {

    final String[] MusicChannelIndicators = {"musica", "music", "radio"};

    private void joinVoiceChanneltoPlayAudio(AudioManager audioManager, MessageReceivedEvent event) throws VoiceChannelNotFoundException {

        VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
        try{

            joinVoiceChannel(audioManager,event, voiceChannel);

        }catch (VoiceChannelNotFoundException v){

            for (VoiceChannel aVoiceChannel : event.getGuild().getVoiceChannels()) {
                for (String musicChannelIndicator : MusicChannelIndicators) {

                    if (aVoiceChannel.getName().toLowerCase().contains(musicChannelIndicator)) {

                        joinVoiceChannel(audioManager,event,aVoiceChannel);
                    }
                }
            }

            throw new VoiceChannelNotFoundException();
        }


    }

    public PlayCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);

        names = new String[]{"play", "p"};
        description = "Reproduce el sonido dado por el identificador. Si es un link reproduce el sonido que pueda extraerse de la página. Si no busca en Youtube un video." +
                "En caso de que ya se esté reproduciendo algo, lo agrega a la cola de reproducción.";
        parameters = new String[]{"<Identificador>"};
    }



    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        if(!audioManager.isConnected()){
            try {

                joinVoiceChanneltoPlayAudio(audioManager,event);

            } catch (VoiceChannelNotFoundException e) {

                StringBuilder stringBuilder = new StringBuilder();
                for(String keyString : MusicChannelIndicators){
                    stringBuilder.append(keyString).append(" ");


                }

                event.getChannel()
                        .sendMessage("```No encontré un canal de voz compatible. unite a uno antes o ponele a algún canal una de las siguientes palabras en el nombre:\n"+
                                stringBuilder+"```").queue();

                return;
            }
        }

        audioPlayerSendManager.addTrackToQueueForServerOfEvent(stringParametros,event);



    }


}


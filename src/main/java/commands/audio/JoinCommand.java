package commands.audio;

import Exceptions.audio.VoiceChannelNotFoundException;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends AudioCommand {

    public JoinCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"join"};
        description = "Conecta al bot al canal de voz con el nombre dado o lo conecta al canal en el que esté el usuario si no se pasa parámetro.";
        parameters = new String[]{"<Nombre canal de voz>"};
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        AudioManager audioManager = event.getGuild().getAudioManager();

        if(stringParametros.isEmpty()){

            try {
                joinVoiceChannel(audioManager,event,event.getMember().getVoiceState().getChannel());
            } catch (VoiceChannelNotFoundException ex) {
                event.getChannel().sendMessage("```No estás unido a ningún canal de voz, si queres que me una a algún canal pasa el nombre o unite a uno.```").queue();
            }

        }else{

            String stringABuscar = stringParametros.trim();
            VoiceChannel voiceChannel = event.getGuild().getVoiceChannelsByName(stringABuscar, true).get(0);

            try {
                joinVoiceChannel(audioManager,event,voiceChannel);
            } catch (VoiceChannelNotFoundException e) {
                event.getChannel().sendMessage("```No se encontró ningún canal de voz con el nombre: "+stringABuscar+"```").queue();
            }
        }




    }
}

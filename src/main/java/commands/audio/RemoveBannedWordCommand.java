package commands.audio;

import Exceptions.audio.WordAlreadyBannedInGuildException;
import Exceptions.audio.WordNotBannedInGuildException;
import commands.admin.AdminLevelComponent;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RemoveBannedWordCommand extends  AudioCommand{
    public RemoveBannedWordCommand(AudioPlayerSendManager anAudioPlayerSendManager) {

        super(anAudioPlayerSendManager);
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"unbanword", "unban"};
        description = "Elimina al texto parámetro de la lista de palabras baneadas para el reproductor de audio";
        parameters = new String[]{"<texto a desbanear>"};

    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        try {
            audioPlayerSendManager.deleteBannedWordFromServer(stringParametros,event.getGuild().getIdLong());
            event.getChannel().sendMessage("**\""+stringParametros+"\" REMOVIDO CON EXITO DE LA LISTA DE PALABRAS BANEADAS**").queue();
        } catch ( WordNotBannedInGuildException e) {
            event.getChannel().sendMessage("```El texto \""+stringParametros+"\" no está baneado en este servidor.```").queue();
        }
    }
}

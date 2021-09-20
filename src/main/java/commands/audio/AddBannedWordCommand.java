package commands.audio;

import Exceptions.audio.WordAlreadyBannedInGuildException;
import commands.admin.AdminLevelComponent;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AddBannedWordCommand extends AudioCommand {
    public AddBannedWordCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"banword", "ban"};
        description = "Bloquea a la palabra o oración dada para que el reproductor no tenga en cuenta audios relacionados a esta.";
        parameters = new String[]{"<texto a banear>"};

    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        try {
            audioPlayerSendManager.addBannedWordToServer(stringParametros,event.getGuild().getIdLong());
            event.getChannel().sendMessage("**\""+stringParametros+"\" AGREGADO CON EXITO A LA LISTA DE PALABRAS BANEADAS**").queue();
        } catch (WordAlreadyBannedInGuildException e) {
            event.getChannel().sendMessage("```El texto \""+stringParametros+"\" ya está baneado en este servidor.```").queue();
        }
    }
}

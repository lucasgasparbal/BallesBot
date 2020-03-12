package commands.audio;

import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Collection;

public class ShowBannedWordsCommand extends AudioCommand{

    public ShowBannedWordsCommand(AudioPlayerSendManager anAudioPlayerSendManager) {
        super(anAudioPlayerSendManager);
        names = new String[]{"bannedwords"};
        description = "Muestra las palabras baneadas en este servidor";
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        Collection<String> bannedWords = audioPlayerSendManager.getBannedWordsFromAudioPlayer(event.getGuild().getIdLong());

        MessageChannel messageChannel = event.getChannel();

        if(bannedWords.isEmpty()){
            messageChannel.sendMessage("**Actualmente no hay ninguna palabra baneada en este servidor**").queue();
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(":x:__**PALABRAS BANEADAS**__:x:");
            for(String bannedWord : bannedWords){
                if(stringBuilder.length() > 1500){
                    messageChannel.sendMessage(stringBuilder.toString()).queue();
                    stringBuilder = new StringBuilder();
                }
                stringBuilder.append("\n").append(" + ").append(bannedWord);
            }
            messageChannel.sendMessage(stringBuilder.toString()).queue();
        }
    }
}

import commands.*;
import commands.audio.*;
import commands.textChannel.AlbertoCommand;
import commands.textChannel.AyudaCommand;
import commands.textChannel.SaludoCommand;
import commands.textChannel.management.AntiSpamCommand;
import commands.textChannel.management.LimpiarMensajesCommand;
import commands.textChannel.management.LimpiarMensajesUsuarioCommand;
import commands.util.PrefixesHandler;
import commands.util.audio.AudioPlayerSendManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class BallesBot extends ListenerAdapter {

    PrefixesHandler prefixesHandler = new PrefixesHandler();
    AudioPlayerSendManager audioPlayerSendManager = new AudioPlayerSendManager();
    private Map<String, Command> commands = new HashMap<>();
    public static void main(String[] args) throws LoginException{
        JDA jda = JDABuilder.createDefault(args[0]).build();
        jda.addEventListener(new BallesBot());
    }

    private void  mapCommand(Command command){
        for(String name : command.getNames()){
            commands.put(name,command);
        }
    }
    public BallesBot(){

        SaludoCommand saludoCommand = new SaludoCommand();
        mapCommand(saludoCommand);

        AlbertoCommand albertoCommand = new AlbertoCommand();
        mapCommand(albertoCommand);

        LimpiarMensajesCommand limpiarMensajesCommand = new LimpiarMensajesCommand();
        mapCommand(limpiarMensajesCommand);

        LimpiarMensajesUsuarioCommand limpiarMensajesUsuarioCommand = new LimpiarMensajesUsuarioCommand();
        mapCommand(limpiarMensajesUsuarioCommand);

        AntiSpamCommand antiSpamCommand = new AntiSpamCommand();
        mapCommand(antiSpamCommand);

        PrefixCommand prefixCommand = new PrefixCommand(prefixesHandler);
        mapCommand(prefixCommand);

        PlayCommand playCommand = new PlayCommand(audioPlayerSendManager);
        mapCommand(playCommand);

        SkipCommand skipCommand = new SkipCommand(audioPlayerSendManager);
        mapCommand(skipCommand);

        PauseCommand pauseCommand = new PauseCommand(audioPlayerSendManager);
        mapCommand(pauseCommand);

        JoinCommand joinCommand = new JoinCommand(audioPlayerSendManager);
        mapCommand(joinCommand);

        ClearCommand clearCommand = new ClearCommand(audioPlayerSendManager);
        mapCommand(clearCommand);

        DisconnectCommand disconnectCommand = new DisconnectCommand(audioPlayerSendManager);
        mapCommand(disconnectCommand);

        ShowQueueCommand showQueueCommand = new ShowQueueCommand(audioPlayerSendManager);
        mapCommand(showQueueCommand);

        SearchComand searchComand = new SearchComand(audioPlayerSendManager);
        mapCommand(searchComand);

        AddBannedWordCommand addBannedWordCommand = new AddBannedWordCommand(audioPlayerSendManager);
        mapCommand(addBannedWordCommand);

        RemoveBannedWordCommand removeBannedWordCommand = new RemoveBannedWordCommand(audioPlayerSendManager);
        mapCommand(removeBannedWordCommand);

        ShowBannedWordsCommand showBannedWordsCommand = new ShowBannedWordsCommand(audioPlayerSendManager);
        mapCommand(showBannedWordsCommand);

        AyudaCommand ayudaCommand = new AyudaCommand(commands.values());
        mapCommand(ayudaCommand);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        Guild server = event.getGuild();
        Long serverId = server.getIdLong();
        TextChannel messageChannel = message.getTextChannel();

        String serverPrefix = prefixesHandler.getPrefixFromServer(serverId);
        if(!messageIsCommand(message,serverPrefix)){
            if(message.getMentionedUsers().contains(event.getJDA().getSelfUser())){
                messageChannel.sendMessage("**prefijo del server:** "+"`"+serverPrefix+"`").queue();
            }

            audioPlayerSendManager.checkAudioSearchs(event);
            return;
        }

        String[] splitText = splitTextIntoCommandAndParameters(message.getContentDisplay(), serverPrefix);

        if(commands.get(splitText[0].toLowerCase()) == null){

            messageChannel.sendMessage("`Comando` \""+splitText[0]+"\" `Inválido. Utilizar el comando ayuda para ver una lista de los comandos disponibles.`").queue();

        }else{

            if(splitText.length > 1){
                commands.get(splitText[0].toLowerCase()).execute(event, splitText[1]);
            }else{
                commands.get(splitText[0].toLowerCase()).execute(event,"");
            }
        }
    }

    private boolean messageIsCommand(Message mensaje, String serverPrefix){

        if(mensaje.getAuthor().isBot()){
            return false;
        }

        return (mensaje.getContentDisplay().startsWith(serverPrefix));

    }

    private String[] splitTextIntoCommandAndParameters(String string, String serverPrefix) {

        string =  string.replaceFirst(serverPrefix,"");

        return string.split(" ",2);
    }

}



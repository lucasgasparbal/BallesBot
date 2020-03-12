package commands.textChannel;

import commands.Command;
import commands.util.CommandComparator;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class AyudaCommand extends Command {

    Set<Command> regularCommandSet = new LinkedHashSet<>();
    Set<Command> musicCommandSet = new LinkedHashSet<>();

    public AyudaCommand(Collection<Command> commands){
        names = new String[]{"ayuda"};
        description = "";

        List<Command> sortList = new LinkedList<>(commands);
        sortList.sort(new CommandComparator());

        Set<Command> commandSet = new LinkedHashSet<>(sortList);

        for(Command command : commandSet){
            if(command.isAudioCommand()){
                musicCommandSet.add(command);
            }else{
                regularCommandSet.add(command);
            }
        }
    }

    public String getInstructions(){
        return "";
    }

    public void _execute(MessageReceivedEvent event, String stringParametros){
        MessageChannel messageChannel = event.getChannel();

        messageChannel.sendMessage(" \t__**COMANDOS DEL REPRODUCTOR DE AUDIO**__\n \n").queue();
        messageCommandInstructionsForSet(musicCommandSet,messageChannel);
        messageChannel.sendMessage("-------------------- \n\t__**COMANDOS**__\n \n").queue();
        messageCommandInstructionsForSet(regularCommandSet,messageChannel);

    }

    private void messageCommandInstructionsForSet(Set<Command> commands, MessageChannel messageChannel){
        StringBuilder stringResultante = new StringBuilder();




        for(Command comando : commands){

            if(stringResultante.length() > 1000){
                messageChannel.sendMessage(stringResultante).queue();
                stringResultante = new StringBuilder();
            }

            stringResultante.append(comando.getInstructions());
        }

        messageChannel.sendMessage(stringResultante).queue();
    }
}

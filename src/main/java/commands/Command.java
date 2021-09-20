package commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public abstract class Command {

    final String TextoMensajeDenegado = "```ACCESO DENEGA2.```";

    protected ClearenceLevelComponent clearenceLevelComponent = new EveryOneLevelComponent();
    protected String[] names = {""};
    protected String description = "";
    protected String[] parameters = {""};

    public void execute(MessageReceivedEvent event, String stringParametros){
        if(!clearenceLevelComponent.userAuthorChecksClearenceLevel(event.getMember())){
            event.getChannel().sendMessage(TextoMensajeDenegado).queue();
            return;
        }
        _execute(event,stringParametros);
    }
    public abstract void _execute(MessageReceivedEvent event, String stringParametros);


    public int compareToCommand(Command anotherCommand){
        if( this.isAdminOnly() && anotherCommand.isAdminOnly()){
            return (this.names[0].compareTo(anotherCommand.names[0]));
        }else if(this.isAdminOnly()){
            return 1;
        }else{
            return -1;
        }

    }

    protected String getNamesAndParameters(){
        StringBuilder returnString = new StringBuilder();

        List<String> names = Arrays.asList(this.names);
        Iterator<String> namesIterator = names.iterator();

        while(namesIterator.hasNext()){
            String name = namesIterator.next();
            StringBuilder stringParameters = new StringBuilder();
            for(String parameter : parameters){
                stringParameters.append(parameter).append(" ");
            }
            returnString.append(name).append(" ").append(stringParameters);
            if(namesIterator.hasNext()){
                returnString.append(" |   ");
            }
        }

        return returnString.toString();
    }

    protected boolean isAdminOnly(){ return clearenceLevelComponent.isAdminOnly();
    }

    public boolean isAudioCommand(){ return false; }

    public String getInstructions(){
        return "\n**"+getNamesAndParameters()+"**\n```"+clearenceLevelComponent.clearenceLevelForInstructions()+description+"```";
    }

    public Collection<String> getNames(){
        return asList(names);
    }
}

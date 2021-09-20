package commands.textChannel;

import commands.Command;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.Timer;
import java.util.TimerTask;

public class SaludoCommand extends Command {

    final int MilisegundosEntreSaludos = 10*60*1000;
    private TimerTask tareaSaludo = new TimerTask() {
        @Override
        public void run() {
            yaSaludo =  false;
        }
    };
    private Timer reloj = new Timer();
    private boolean yaSaludo =  false;

    public SaludoCommand(){
        names = new String[]{"saludo","hola"};
        description = "BALLES-BOT saluda si tiene ganas.";

    }


    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {
        if(!yaSaludo){
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setTTS(true);
            messageBuilder.setContent(" Aloja hijos de puta.");
            event.getChannel().sendMessage(messageBuilder.build()).queue();
            yaSaludo = true;
            reloj.schedule(tareaSaludo,MilisegundosEntreSaludos);
        }else{
            event.getChannel().sendMessage("`Ya salude bolu3`").queue();
        }

    }
}

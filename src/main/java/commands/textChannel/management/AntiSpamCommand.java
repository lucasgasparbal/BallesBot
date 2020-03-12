package commands.textChannel.management;


import commands.Command;
import commands.admin.AdminLevelComponent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AntiSpamCommand extends Command {

    final int ValorPredeterminadoDelay = 5;

    boolean antispamActivado = false;

    public AntiSpamCommand(){
        clearenceLevelComponent = new AdminLevelComponent();
        names = new String[]{"antispam"};
        description = "habilita Slowmode en todos los canales de texto del servidor. Si ya fue habilitado lo deshabilita.\n" +
                "Se puede modificar el delay entre mensajes permitidos(default 5 segundos) hasta un maximo de "+ TextChannel.MAX_SLOWMODE+" segundos.";
        parameters = new String[]{"<segundos delay>"};
    }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        int delaySegundos = ValorPredeterminadoDelay;

        MessageChannel canalMensaje = event.getChannel();
        if(!stringParametros.equals("")){
            String[] parametros = stringParametros.split(" ");
            try{
                delaySegundos = Integer.parseInt(parametros[0]);
            }catch(NumberFormatException nfe){
                canalMensaje.sendMessage("`Eso no es un número pelotu2`").queue();
                return;
            }
        }


        List<TextChannel> canales = event.getGuild().getTextChannels();

        if(antispamActivado){
            for(TextChannel canal : canales){
                canal.getManager().setSlowmode(0).queue();
            }
            antispamActivado = false;
            canalMensaje.sendMessage("**ANTISPAM OFF**").queue();
        }else{

            if(delaySegundos > TextChannel.MAX_SLOWMODE){
                canalMensaje.sendMessage("`el número dado es superior al máximo permitido ("+TextChannel.MAX_SLOWMODE+") `").queue();
            }else if(delaySegundos < 0){
                canalMensaje.sendMessage("`el número dado debe ser mayor a 0`").queue();
            }

            for(TextChannel canal : canales){
                canal.getManager().setSlowmode(delaySegundos).queue();
            }
            antispamActivado = true;
            canalMensaje.sendMessage("**ANTISPAM ON**").queue();
        }






    }
}

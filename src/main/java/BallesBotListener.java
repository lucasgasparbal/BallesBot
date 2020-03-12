/*import commands.AdminLevelCommands.*;
import commands.textChannel.AlbertoCommand;
import commands.Command;
import commands.textChannel.SaludoCommand;

import java.util.HashMap;
import java.util.Map;

public class BallesBotListener implements MessageCreateListener {
    private Map<String,Command> commands = new HashMap<>();
    private String prefix = "b!";
    public DiscordApi discordApi;

    public BallesBotListener(DiscordApi api){
        discordApi = api;

        SaludoCommand saludo = new SaludoCommand();
        commands.put(saludo.getName(),saludo);

        ArmagedonCommand armagedon = new ArmagedonCommand();
        commands.put(armagedon.getName(),armagedon);

        LimpiarMensajesCommand limpiarMensajes = new LimpiarMensajesCommand();
        commands.put(limpiarMensajes.getName(),limpiarMensajes);

        LimpiarMensajesUsuarioCommand limpiarMensajesUsuario = new LimpiarMensajesUsuarioCommand();
        commands.put(limpiarMensajesUsuario.getName(), limpiarMensajesUsuario);

        AlbertoCommand albertoCommand = new AlbertoCommand();
        commands.put(albertoCommand.getName(), albertoCommand);

        AntiSpamCommand antiSpamCommand = new AntiSpamCommand();
        commands.put(antiSpamCommand.getName(), antiSpamCommand);

        MuteCommand muteCommand = new MuteCommand();
        commands.put(muteCommand.getName(), muteCommand);
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if(mensajeEsComando(event)){

            String[] partitionedString = partirTextoEnComandoYParametros(event.getMessage().getContent());
            if (partitionedString.length == 0){
                return;
            }

            if(partitionedString[0].equalsIgnoreCase("ayuda")){

                comandoAyuda(event);

            }else if(commands.get(partitionedString[0]) != null){

                if(partitionedString.length > 1){
                    commands.get(partitionedString[0]).execute(event, partitionedString[1]);
                }else{
                    commands.get(partitionedString[0]).execute(event,"");
                }


            }else{
                event.getMessage().getChannel()
                        .sendMessage("**comando '"+ partitionedString[0] +"' invalido**\n utilizar el comando **ayuda** para una lista de comandos válidos.");
            }

        }else if(event.getMessage().getMentionedUsers().contains(discordApi.getYourself())) {
            event.getMessage().getChannel().sendMessage("**prefijo:** "+"`"+prefix+"`");
        }
    }

    private String[] partirTextoEnComandoYParametros(String string) {
        string =  string.toLowerCase();
        string = string.replaceFirst(prefix,"");

        return string.split(" ",2);
    }


    private boolean mensajeEsComando(MessageCreateEvent evento){
        Message mensaje= evento.getMessage();

        if(mensaje.getAuthor() == null || mensaje.getAuthor().isBotUser()){
            return false;
        }

        return (mensaje.getContent().startsWith(prefix));

    }

    private void comandoAyuda(MessageCreateEvent evento){
        String stringResultante = "";
        for(Command comando : commands.values()){
            stringResultante = stringResultante +"\n**"+comando.getName()+" "+comando.getParametros()+"**\n"+"```"+comando.getDescription()+"```";
        }
        evento.getChannel().sendMessage(" \t__**Comandos**__\n"+ stringResultante);
    }
}*/

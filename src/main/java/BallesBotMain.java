import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class BallesBotMain {
    public static void main(String[] args) throws LoginException {
        JDA jda = JDABuilder.createDefault(args[0]).build();
        jda.addEventListener(new BallesBot());
    }
}

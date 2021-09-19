package commands.textChannel;
import commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;

public class AlbertoCommand extends Command {
    LinkedList<String> tweets = new LinkedList<>();
    InputStream imagenAlberto;
    InputStream imagenLogoTwitter;
    public AlbertoCommand(){
        names = new String[]{"alberto"};
        description = "Algunas palabras del actual presidente de la nación.";
         ClassLoader cargadorRecursos  = Thread.currentThread().getContextClassLoader();
         imagenAlberto =  cargadorRecursos.getResourceAsStream("imagenes/Alberto.jpg");
         imagenLogoTwitter = cargadorRecursos.getResourceAsStream("imagenes/Twitter_Logo_Blue.png");

         tweets.add("No lo tomes a mal. No discuto con boludos que se masturban creyendo que son lúcidas las boludeces que escriben. Ese es tu caso.");
         tweets.add("Celebro que un pajero de tu talla no me valore. Mil gracias");
         tweets.add("No te insulto!!! Te describo.");
         tweets.add("te conteste. Seguí leyendote, onanista verbal");
         tweets.add("@eugeniomonjeau te presumía necio. Ahora se que sólo sos un boludo importante. Un boludo con vista al Mar!!!! Hervite y tomate el caldo...");
         tweets.add("@jlespert Pajert... Si fuera vos no hablaría... Recordas cuantas boludeces predijiste? Cállate Pajert");
         tweets.add("Que pedazo de pelotudo resultaste. Pasaste de hacerme reír a tener pena por tu imbecilidad. Solo agradece que mi paciencia es infinita. Y rogá que tus imbéciles prepoteadas un día no se crucen con alguien sanguíneo. Seguí tu vida. Pelotudo");
         tweets.add("Celebro entretenerte. Vos no lo haces. Me pareces un pelotudo");
         tweets.add("Nena, no es algo que me inquiete lo que vos creas. Mejor aprende a cocinar. Tal vez así logres hacer algo bien. Pensar no es tu fuerte. Está visto");
         tweets.add("Andamos muy bien, pedazo de hijo de puta");
         tweets.add("Ubícate idiota... ya algunos te han ubicado. Pelotudo");
         tweets.add("@changokardenas otro boludo con vista al mar. Todos militontos de Cristina... Se creen revolucionarios y son tristes repetidores de mentiras");
         tweets.add("juro que no sabía que en San Juan sembraban boludos");
         tweets.add("Nada más penoso que un pelotudo que se cree inteligente. Tengo la mala suerte de haberme cruzado con uno que sos vos. En fin... seguí masturbandote leyendo imbecilidades que escribís");
         tweets.add("Y a vos quien te designa para algo? si sos un pobre laburante. Tu jefe te dice que hacer y vos cerras el culo y lo haces, bobo");
         tweets.add("@anitaazarzamar otra vieja pelotuda");
         tweets.add("a mi CFK me persiguió durante ocho años!! A vos ni te registró!!! SOS UN IMBECIL PERFECTO!!! chau pelotudo.i");
         tweets.add("chau pelotudo");
         Collections.shuffle(tweets);
     }

    @Override
    public void _execute(MessageReceivedEvent event, String stringParametros) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        Collections.shuffle(tweets);
        String texto = tweets.peekFirst();


        event.getChannel().sendMessageEmbeds(embedBuilder.setAuthor("Alberto Fernández(@alferdez)", "https://twitter.com/alferdez")
                        .setDescription(texto)
                        .setFooter("Twitter")
                        .setColor(Color.CYAN).build()).queue();

    }
}

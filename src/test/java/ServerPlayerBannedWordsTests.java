import Exceptions.audio.WordAlreadyBannedInGuildException;
import commands.util.audio.ServerPlayerBannedWords;
import org.junit.Assert;
import org.junit.Test;

public class ServerPlayerBannedWordsTests {
    @Test
    public void StringFromGuildHasBannedWordReturnsTrueOnWindowsXPEarrapeCase(){
        ServerPlayerBannedWords serverPlayerBannedWords = new ServerPlayerBannedWords();

        try{

            serverPlayerBannedWords.addBannedWordToGuild(0L,"earrape");

        }catch (WordAlreadyBannedInGuildException ignored){

        };

        Assert.assertTrue(serverPlayerBannedWords.stringFromGuildHasBannedWords(0L,"Windows XP startup earrape"));
    }
}

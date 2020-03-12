package commands.util.audio;

import Exceptions.audio.WordNotBannedInGuildException;
import Exceptions.audio.WordAlreadyBannedInGuildException;
import commands.util.SerialFileHandler;
import commands.util.StringsUtil;

import java.util.*;

public class ServerPlayerBannedWords {
    Map<Long, Set<String>> guildIdBannedWords;

    String fileSeparator = System.getProperty("file.separator");
    private final String SerialFilePathFile = "balles-bot"+fileSeparator+"data"+fileSeparator+"serverAudioPlayerBannedWords.serial";

    public ServerPlayerBannedWords(){
        guildIdBannedWords = (Map<Long, Set<String>>) SerialFileHandler.readObjectFromFile(SerialFilePathFile);
        if(guildIdBannedWords == null){
            guildIdBannedWords = new HashMap<>();
        }
    }
    public Set<String> getGuildBannedWords(Long guildId){
        if(!guildIdBannedWords.containsKey(guildId)){
            guildIdBannedWords.put(guildId, new HashSet<>());

        }
        return guildIdBannedWords.get(guildId);
    }

    public boolean stringFromGuildHasBannedWords(Long guildId, String string){
        Set<String> guildBannedWords = getGuildBannedWords(guildId);
        for(String bannedWord : guildBannedWords){
            String analizedString = string.toLowerCase();
            if(analizedString.contains(bannedWord) || analizedString.startsWith(bannedWord.strip()) || analizedString.endsWith(bannedWord.strip())){
                return true;
            }
        }

        return false;
    }

    public void addBannedWordToGuild(Long guildId, String word) throws WordAlreadyBannedInGuildException {
        Set<String> guildBannedWords = getGuildBannedWords(guildId);
        if(!guildBannedWords.add(StringsUtil.addWhitespacesToString(word.toLowerCase().trim()))){
            throw new WordAlreadyBannedInGuildException();
        }
        SerialFileHandler.writeObjectOnFile(guildIdBannedWords,SerialFilePathFile);
    }

    public void deleteBannedWordFromGuild(Long guildId, String word) throws WordNotBannedInGuildException {
        Set<String> guildBannedWords =  getGuildBannedWords(guildId);
       if(!guildBannedWords.remove(StringsUtil.addWhitespacesToString(word.toLowerCase().trim()))){
            throw new WordNotBannedInGuildException();
       }
        SerialFileHandler.writeObjectOnFile(guildIdBannedWords,SerialFilePathFile);
    }
}

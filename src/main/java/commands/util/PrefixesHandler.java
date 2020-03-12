package commands.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PrefixesHandler {
    Map<Long, String> prefixes;

    String fileSeparator = System.getProperty("file.separator");
    final String filePathPrefix = "balles-bot"+fileSeparator+"data"+fileSeparator+"prefixes.serial";

    public PrefixesHandler(){

        prefixes = (Map<Long, String>) SerialFileHandler.readObjectFromFile(filePathPrefix);
        if(prefixes == null){
            prefixes = new HashMap<>();
        }

    }
    private void addServer(Long serverId){
        final String DefaultPrefix = "b!";
        prefixes.put(serverId,DefaultPrefix);
        SerialFileHandler.writeObjectOnFile(prefixes,filePathPrefix);
    }

    public String getPrefixFromServer(Long serverId){
        if(!prefixes.containsKey(serverId)){
            addServer(serverId);
        }
        return prefixes.get(serverId);
    }

    public void changePrefixForServer(String newPrefix, Long serverId){
       prefixes.put(serverId,newPrefix);
       SerialFileHandler.writeObjectOnFile(prefixes,filePathPrefix);

    }

}

package commands.util;

import java.io.*;
import java.util.HashMap;

public class SerialFileHandler {

    public static void writeObjectOnFile(Object object, String filePath){

        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream outputStream = new ObjectOutputStream(file);
            outputStream.writeObject(object);
            outputStream.close();
            file.close();
        }  catch(FileNotFoundException e){
            try {
                File newFile = new File(filePath);
                newFile.createNewFile();
                FileOutputStream file = new FileOutputStream(newFile);
                ObjectOutputStream outputStream = new ObjectOutputStream(file);
                outputStream.writeObject(object);
                outputStream.close();
                file.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(String filePath){
        try {

            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream inputStream = new ObjectInputStream(file);
            Object returnObject = inputStream.readObject();
            inputStream.close();
            file.close();
            return  returnObject;

        } catch (FileNotFoundException e) {
            File prefixesFile = new File(filePath);
            try {
                if(!prefixesFile.createNewFile()){
                    System.out.println("f");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return null;
    }
}

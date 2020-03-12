package commands.util;
import java.util.ArrayList;

public class StringsUtil {

   public static String[] splitIgnorandoComillas(String unString, char caracterSplit){
        ArrayList<String> resultado= new ArrayList<>();
        Boolean comillasAbiertas = false;
        char[] arrayCaracteres = unString.toCharArray();

        StringBuilder palabra = new StringBuilder();
        for(int i = 0; i < arrayCaracteres.length; i++){

            if(i == arrayCaracteres.length-1) {

                if(arrayCaracteres[i] != '"'){
                        palabra.append(arrayCaracteres[i]);

                }

                if(palabra.length() > 0){
                    resultado.add(palabra.toString().trim());
                }

            }else{

                if((arrayCaracteres[i] == caracterSplit && !comillasAbiertas) | i == arrayCaracteres.length-1){

                    if(palabra.length() > 0){
                        resultado.add(palabra.toString().trim());
                    }
                    palabra = new StringBuilder();

                }else if(arrayCaracteres[i] == '"'){

                    comillasAbiertas = !comillasAbiertas;

                }else{
                    palabra.append(arrayCaracteres[i]);
                }
            }


        }

        String[] arrayStrings = new String[1];

        return resultado.toArray(arrayStrings);
    }

    public static String[] splitIgnorandoComillas(String unString, String caracterSplit){

        if(caracterSplit.length() == 1){
            return splitIgnorandoComillas(unString,caracterSplit.charAt(0));
        }

        ArrayList<String> resultado= new ArrayList<>();
        Boolean comillasAbiertas = false;
        char[] arrayCaracteres = unString.toCharArray();

        StringBuilder palabra = new StringBuilder();

        for(int i = 0; i < arrayCaracteres.length; i++){

            if(i == arrayCaracteres.length-1) {

                if(arrayCaracteres[i] != '"'){
                    palabra.append(arrayCaracteres[i]);
                }

                if(palabra.length() > 0){
                    resultado.add(palabra.toString());
                }

            }else{

                if((arrayCaracteres[i] == caracterSplit.toCharArray()[0] && !comillasAbiertas)){


                    for(int j = 1; j < caracterSplit.toCharArray().length;j++){
                        if(i+j >= arrayCaracteres.length){
                            resultado.add(palabra.toString());
                            return resultado.toArray(String[]::new);
                        }

                        if(arrayCaracteres[i+j] != caracterSplit.toCharArray()[j]){
                            break;
                        }

                        if(j == caracterSplit.toCharArray().length-1){
                            if(palabra.length() > 0){
                                resultado.add(palabra.toString().trim());
                            }
                            palabra = new StringBuilder();
                            i = i+j;
                        }
                    }

                }else if(arrayCaracteres[i] == '"'){
                    comillasAbiertas = !comillasAbiertas;
                }else{
                    palabra.append(arrayCaracteres[i]);
                }
            }


        }

        return resultado.toArray(String[]::new);
    }

    public static String addWhitespacesToString(String string){
        return " " + string + " ";
    }
}

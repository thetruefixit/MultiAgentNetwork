package Other;

import com.google.gson.Gson;
import padeg.lib.Padeg;
import vk.FileWorker;

import java.util.ArrayList;

public class group {
// * Структура для хранения базы самых
// употребляемых прилагательных в формате JSON //
//////////////////////////////////////////////////////////////////////
    public static class PrilJson {                                   //
      private Pril adj = new Pril();                                 //
    }                                                                //
    private static class Pril {                                      //
        String[] words = new String[0];                              //
    }                                                                //
///////////////////////////////////////////////////////////////////////
    // * Временный расширяющийся список, содержащий базу слов после парсинга из JSON //
    public static final ArrayList<String> base = parsing();


    private static ArrayList<String> parsing() {
        ArrayList<String> newList = new ArrayList<String>();
        Gson gson = new Gson(); // JSON Парсер
        PrilJson obj = gson.fromJson(FileWorker.read("db.json"),PrilJson.class);
        for(int i = 0;i<obj.adj.words.length;i++)
            newList.add(obj.adj.words[i]);
        return newList;
    }

    public static int CheckWordInBase(String word) {
        int i=0, result=0;
        boolean b = true;
        for(i=0;((b)&(i<base.size()));i++)
        {
            if(word.toLowerCase().equals(base.get(i))) {
                b = false;
                result = i;
            }
        }
        return result;
    }
//Метод для создание какой-либо базы слов
    private static String[] getM() {
        ArrayList<String> ii = new ArrayList<String>();
        for (int i=0; i<base.size(); i++) {
              if((base.get(i).endsWith("ая"))||(base.get(i).endsWith("ой"))||(base.get(i).endsWith("ую")))
                  for(int j=1;j<7;j++)
                    ii.add(Padeg.getFIOPadeg(base.get(i),"","",false,j).toLowerCase());
                else
                  for(int j=1;j<7;j++)
                    ii.add(Padeg.getFIOPadeg(base.get(i),"","",true,j).toLowerCase());
        }

        String[] hey = new String[764*6];
        ii.toArray(hey);
    return hey;
    }
//Создание JSON-файла из объекта заданной структуры.
    public static void saveToJson() {
        Gson gson = new Gson();
        PrilJson obj = new PrilJson();
        String str = gson.toJson(obj);
        System.out.println(str);
        FileWorker.write("Pril_new.json", str);
    }
}

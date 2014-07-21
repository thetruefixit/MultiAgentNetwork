package vk;

import com.google.gson.Gson;
import Сlassifier.helper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fixit on 05.06.2014.
 */
public class vkGroups {
    private static class response{
        private int count = 0;
        private int[] items = new int[0];
    }
    private static class GroupJson {
        private response response = new response();
    }
    public static ArrayList<Integer> getIds(String id,String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        ArrayList<Integer> mass= new ArrayList<Integer>();
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count=0",token);
        GroupJson Obj = gson.fromJson(str,GroupJson.class);
        count = Obj.response.count;
        if(count!=0) {
            if(count<=1000) {
                str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count="+count+"&v=5.16",token);
                Obj = gson.fromJson(str,GroupJson.class);
                for (int i = 0; i< Obj.response.items.length;i++) {
                        mass.add(Obj.response.items[i]);
                }
                System.out.println(mass.size());
            }
            else {
                if(count%1000== 0) {
                    int size = count / 1000;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count=1000&v=5.16",token);//Запрос на получение фото.
                        Obj = gson.fromJson(str,GroupJson.class);
                        for (int k = 0; k< Obj.response.items.length;k++) {
                            mass.add(Obj.response.items[k]);
                        }
                        offset=offset+1000;
                    }
                    System.out.println(mass.size());
                }
                if(count%1000!=0) {
                    int size = count / 1000;
                    int last = count % 1000;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count=1000&v=5.16",token);//Запрос на получение фото.
                        Obj = gson.fromJson(str,GroupJson.class);
                        for (int k = 0; k< Obj.response.items.length;k++) {
                            mass.add(Obj.response.items[k]);
                        }
                        System.out.println(mass.size());
                        offset=offset+1000;
                    }
                    str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count="+last+"&v=5.16",token);//Запрос на получение фото.
                    Obj = gson.fromJson(str,GroupJson.class);
                    for (int k = 0; k< Obj.response.items.length;k++) {
                        mass.add(Obj.response.items[k]);
                    }
                    System.out.println(mass.size());
                }
            }

        }
        return mass;
    }
    public static ArrayList<Integer> getRandom1000(String id,String token) throws IOException {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String offset = "0"; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("groups.getMembers?group_id="+id+"&offset="+offset+"&count=0",token);
        GroupJson Obj = gson.fromJson(str,GroupJson.class);
        count = Obj.response.count;
        for (int i = 0; i < 1000;i++) {
            offset = helper.generateId(1, count - 1);
            count = 1;
            str = HttpGetter.sendGet("groups.getMembers?group_id=" + id + "&offset=" + offset + "&count=" + count + "&v=5.16", token);
            Obj = gson.fromJson(str, GroupJson.class);
                result.add(Obj.response.items[0]);
            System.out.println("Получен:"+i);
        }
        return result;
    }
}

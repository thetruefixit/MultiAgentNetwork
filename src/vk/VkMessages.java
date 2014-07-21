package vk;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Данный класс используется для получения сообщений с социальной сети Vk.com
 * Для этого используется http-get запрос, позволяющий получить список сообщений в формате JSON.
 * Подробнее о формате получения можно прочесть здесь: https://vk.com/dev/
 * После получение, происходит конвертация, с помощью библиотеки Google gson и структурного объекта MessageJson.
 *
 */
public class VkMessages {
/* Шаблон для JSON */
public class items {
    public String id="";//id сообщения
    public String user_id="";
    public String from_id="";
    public String body ="";
    public String date="";
    public String read_state="";
    public byte out = 1; //Маркер, который служит для определения, является ли залогиненый юзер отправителем. 1=Да,0=Нет
}
    public static class response{
        public int count = 0;
        public items[] items = new items[0];
    }
    public static class messagesJson {
        public response response = new response();
    }
/* Конец Шаблона */

    public static ArrayList GetById(String vkid,String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count=200&v=5.5&rev=1",token);//Запрос на получение сообщений.
        messagesJson obj = gson.fromJson(str,messagesJson.class);//Парсинг полученных сообщений из JSON В MessageJson.class
        count = obj.response.count;
        ArrayList Mass = new ArrayList(); // Создание расширяющегося массива
        int i = 0;
        while(count>i) {
            if((obj.response.items[i-offset].out == 1)) {
                Mass.add(obj.response.items[i-offset].body);
            }
            i++;
            if((i+1==200)|(i+1==offset+200)) {  //Перемещаем сдвиг на следующий слой массива сообщений и получаем его
                offset+=200;
                str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count=200&v=5.5&rev=1",token);
                obj = gson.fromJson(str,messagesJson.class);
                i++;
            }
        }
        return Mass;
    }
    public static ArrayList<String> GetSendedMsg(String token) throws IOException {
        int offset = 0; // Данаая переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("messages.get?out=1&offset="+offset+"&count=200&v=5.5",token);//Запрос на получение сообщений.
        messagesJson obj = gson.fromJson(str,messagesJson.class);//Парсинг полученных сообщений из JSON В MessageJson.class
        count = obj.response.count;
        ArrayList<String> Mass = new ArrayList<String>(); // Создание расширяющегося массива
        int i = 0;
        while(count>i) {
            if((obj.response.items[i-offset].out == 1)) {
                Mass.add(obj.response.items[i-offset].body);
            }
            i++;
            if((i+1==200)|(i+1==offset+200)) {  //Перемещаем сдвиг на следующий слой массива сообщений и получаем его
                offset+=200;
                str = HttpGetter.sendGet("messages.get?out=1&offset="+offset+"&count=200&v=5.5",token);
                obj = gson.fromJson(str,messagesJson.class);
                i++;
            }
        }
        return Mass;
    }
    public static ArrayList<ArrayList<String>> getMsgListOld(ArrayList<String> friendsList,String token) {
        ArrayList<ArrayList<String>> mass = new ArrayList<ArrayList<String>>();
        for (int i=0;i<friendsList.size();i++) {
            ArrayList temp = new ArrayList();
            try {
                temp = VkMessages.GetById(friendsList.get(i),token);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mass.add(temp);

        }

        return mass;
    }
    public static ArrayList GetByWord(String word,String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        Gson gson = new Gson(); // JSON Парсер
        ArrayList Mass = new ArrayList(); // Создание расширяющегося массива
        String str = HttpGetter.sendGet("messages.getHistory?q="+word+"&offset="+offset+"&count=200&v=5.5",token);//Запрос на получение сообщений.
        messagesJson obj = gson.fromJson(str,messagesJson.class);//Парсинг полученных сообщений из JSON В MessageJson.class
        count = obj.response.count;
        int i = 0;
        while(count>i) {
            if((obj.response.items[i-offset].out == 1)) {
                Mass.add(obj.response.items[i-offset].body);
            }
            i++;
            if((i+1==200)|(i+1==offset+200)) {  //Перемещаем сдвиг на следующий слой массива сообщений и получаем его
                offset+=200;
                str = HttpGetter.sendGet("messages.getHistory?q="+word+"&offset="+offset+"&count=200&v=5.5",token);
                obj = gson.fromJson(str,messagesJson.class);
                i++;
            }
        }

        return Mass;
    }
    public static ArrayList<items> getAllMsgById(String vkid, String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        ArrayList<items> mass= new ArrayList<items>();
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count=0&rev=1&v=5.21",token);
        messagesJson messagesObj = gson.fromJson(str,messagesJson.class);
        count = messagesObj.response.count;
        System.out.println(count);
        System.out.println(token);
        if(count!=0) {
            if(count<=200) {
                str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count="+count+"&rev=1&v=5.21",token);
                messagesObj = gson.fromJson(str,messagesJson.class);
                for (items i : messagesObj.response.items) {
                        mass.add(i);
                }
            }
            else {
                if(count%200== 0) {
                    int size = count / 200;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count=200&rev=1&v=5.21",token);//Запрос на получение фото.
                        messagesObj = gson.fromJson(str, messagesJson.class);
                        for (items k : messagesObj.response.items) {
                                mass.add(k);
                        }
                        offset=offset+200;
                    }
                }
                if(count%200!=0) {
                    int size = count / 200;
                    int last = count % 200;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count=200&rev=1&v=5.21",token);//Запрос на получение фото.
                        messagesObj = gson.fromJson(str, messagesJson.class);
                        for (items k : messagesObj.response.items) {
                                mass.add(k);
                        }
                        offset=offset+200;
                    }
                    str = HttpGetter.sendGet("messages.getHistory?user_id="+vkid+"&offset="+offset+"&count="+last+"&rev=1&v=5.21",token);//Запрос на получение фото.
                    messagesObj = gson.fromJson(str,messagesJson.class);
                    for (items k : messagesObj.response.items) {
                            mass.add(k);
                    }
                }
            }

        }
        return mass;
    }
}

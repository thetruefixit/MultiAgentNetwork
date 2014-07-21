package vk;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class VkWall {
    public class groups {
        public String id = "";
        public String name = "";
    }
    public class attachments {
        public String type="";
        public String id="";
        public String owner_id="";
        public String title="";
        public String desctiption="";
    }
    public class copy_history {
        public String id = "";
        public String owner_id = "";
        public String from_id = "";
        public String text = "";
    }
    public class items {
        public String id="";//id фото
        public String owner_id="";
        public String text="";
        public copy_history[] copy_history = new copy_history[0];
        public attachments[] attachments = new attachments[0];
    }
    private static class response{
        public int count = 0;
        public items[] items = new items[0];
        public groups[] groups = new groups[0];
    }
    private static class WallJson {
        private response response = new response();
    }

    public static class result {
        public ArrayList<items> items = new ArrayList<VkWall.items>();
        public ArrayList<groups> groups = new ArrayList<VkWall.groups>();
        public ArrayList<copy_history> hist = new ArrayList<copy_history>();
        public String owner_id = "";
        public int error_code = 0;

    }
    public static result getWallById(String vkid, String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        result rslt = new result();
        ArrayList<items> mass = new ArrayList<items>();
        ArrayList<groups> groupsArrayList = new ArrayList<groups>();
        ArrayList<copy_history> hist = new ArrayList<copy_history>();
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("wall.get?owner_id=" + vkid + "&offset=" + offset + "&count=0&extended=1&filter=all&v=5.7", token);
        if (!str.contains("error_msg: 'User was deleted or banned'")) {
            WallJson WallObj = gson.fromJson(str, WallJson.class);
            count = WallObj.response.count;
            // System.out.println("Ожидается получение стены:" + count);
            //  if ((count < 2000)&(count>100)) { // Искуственный счетчик для увелечения производительности
            if (count != 0) {
                if (count <= 200) {
                    str = HttpGetter.sendGet("wall.get?owner_id=" + vkid + "&offset=" + offset + "&count=" + count + "&extended=1&filter=all&v=5.7", token);
                    WallObj = gson.fromJson(str, WallJson.class);
                    for (items i : WallObj.response.items) {
                        if ((i.text.length() > 0)) {
                            mass.add(i);
                        } else if (i.copy_history != null) {
                            if (i.copy_history[0].text.length() > 0)
                                hist.add(i.copy_history[0]);
                        }
                    }
                    if (WallObj.response.groups.length > 0)
                        for (groups g : WallObj.response.groups) {
                            groupsArrayList.add(g);
                        }
                } else {
                    if (count % 200 == 0) {
                        int size = count / 200;
                        for (int i = 0; i < size; i++) {
                            str = HttpGetter.sendGet("wall.get?owner_id=" + vkid + "&offset=" + offset + "&count=" + count + "&extended=1&filter=all&v=5.7", token);//Запрос на получение фото.
                            WallObj = gson.fromJson(str, WallJson.class);
                            for (items k : WallObj.response.items) {
                                if ((k.text.length() > 0))
                                    mass.add(k);
                                else if (k.copy_history != null) {
                                    if (k.copy_history[0].text.length() > 0)
                                        hist.add(k.copy_history[0]);
                                }
                            }
                            if (WallObj.response.groups.length > 0)
                                for (groups g : WallObj.response.groups) {
                                    groupsArrayList.add(g);
                                }
                            offset = offset + 200;
                        }
                    }
                    if (count % 200 != 0) {
                        int size = count / 200;
                        int last = count % 200;
                        for (int i = 0; i < size; i++) {
                            str = HttpGetter.sendGet("wall.get?owner_id=" + vkid + "&offset=" + offset + "&count=200&extended=1&filter=all&v=5.7", token);//Запрос на получение фото.
                            WallObj = gson.fromJson(str, WallJson.class);
                            for (items k : WallObj.response.items) {
                                if ((k.text.length() > 0))
                                    mass.add(k);
                                else if (k.copy_history != null) {
                                    if (k.copy_history[0].text.length() > 0)
                                        hist.add(k.copy_history[0]);
                                }
                            }
                            if (WallObj.response.groups.length > 0)
                                for (groups g : WallObj.response.groups) {
                                    groupsArrayList.add(g);
                                }
                            offset = offset + 200;
                        }
                        str = HttpGetter.sendGet("wall.get?owner_id=" + vkid + "&offset=" + offset + "&count=" + last + "&extended=1&filter=all&v=5.7", token);//Запрос на получение фото.
                        WallObj = gson.fromJson(str, WallJson.class);
                        for (items k : WallObj.response.items) {
                            if ((k.text.length() > 0))
                                mass.add(k);
                            else if (k.copy_history != null) {
                                if (k.copy_history[0].text.length() > 0)
                                    hist.add(k.copy_history[0]);
                            }
                        }
                        if (WallObj.response.groups.length > 0)
                            for (groups g : WallObj.response.groups) {
                                groupsArrayList.add(g);
                            }
                    }
                }

            }
            rslt.groups = groupsArrayList;
            rslt.items = mass;
            rslt.hist = hist;
        } else {
            //System.out.println("Отмена получения!");
            rslt.error_code = 1;
        }
        return rslt;
    }

}

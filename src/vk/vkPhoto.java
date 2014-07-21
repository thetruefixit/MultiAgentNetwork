package vk;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Fixit
 * Date: 25.03.14
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class vkPhoto {
    public class items {
        public String id="";//id фото
        public String owner_id="";
        public String text="";
        public String lat="";
        @SerializedName("long") public String lon="";
    }
    private static class response{
        private int count = 0;
        private items[] items = new items[0];
    }
    private static class PhotoJson {
        private response response = new response();
    }
    public static ArrayList<items> getAllPhotoById(String vkid, String token) throws IOException {
        int offset = 0; // Данная переменая используется для сдвига массива сообщений при запросе с vk.com
        int count = 0; //Общее число сообщений
        ArrayList<items> mass= new ArrayList<items>();
        Gson gson = new Gson(); // JSON Парсер
        String str = HttpGetter.sendGet("photos.getAll?owner_id="+vkid+"&offset="+offset+"&count=0&extended=0&photo_sizes=0&no_service_albums=0&v=5.16",token);
        PhotoJson PhotoObj = gson.fromJson(str,PhotoJson.class);
        count = PhotoObj.response.count;
        System.out.println(count);
        System.out.println(token);
        if(count!=0) {
            if(count<=200) {
                str = HttpGetter.sendGet("photos.getAll?owner_id="+vkid+"&offset="+offset+"&count="+count+"&extended=0&photo_sizes=0&no_service_albums=0&v=5.16",token);
                PhotoObj = gson.fromJson(str,PhotoJson.class);
                for (items i : PhotoObj.response.items) {
                    if(i.lat!=null)
                        mass.add(i);
                }
            }
            else {
                if(count%200== 0) {
                    int size = count / 200;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("photos.getAll?owner_id="+vkid+"&offset="+offset+"&count=200&extended=0&photo_sizes=0&no_service_albums=0&v=5.16",token);//Запрос на получение фото.
                        PhotoObj = gson.fromJson(str, PhotoJson.class);
                        for (items k : PhotoObj.response.items) {
                            if(k.lat!=null)
                            mass.add(k);
                        }
                        offset=offset+200;
                    }
                }
                if(count%200!=0) {
                    int size = count / 200;
                    int last = count % 200;
                    for (int i = 0; i < size; i++) {
                        str = HttpGetter.sendGet("photos.getAll?owner_id="+vkid+"&offset="+offset+"&count=200&extended=0&photo_sizes=0&no_service_albums=0&v=5.16",token);//Запрос на получение фото.
                        PhotoObj = gson.fromJson(str, PhotoJson.class);
                        for (items k : PhotoObj.response.items) {
                            if(k.lat!=null)
                            mass.add(k);
                        }
                        offset=offset+200;
                    }
                    str = HttpGetter.sendGet("photos.getAll?owner_id="+vkid+"&offset="+offset+"&count="+last+"&extended=0&photo_sizes=0&no_service_albums=0&v=5.16",token);//Запрос на получение фото.
                    PhotoObj = gson.fromJson(str,PhotoJson.class);
                    for (items k : PhotoObj.response.items) {
                        if(k.lat!=null)
                        mass.add(k);
                    }
                }
            }

        }
        return mass;
    }
}

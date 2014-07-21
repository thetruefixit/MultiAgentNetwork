package vk;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Данный класс, описывает методы для работы с Api Vk.com, которые получают список друзей для последущего анализа.
 */
public class VkFriends {
    private static class friendsJson {
        String[] response = new String[0];
    }
    public class items {
        public String uid = "";//id сообщения
        public String first_name = "";
        public String last_name = "";

    }
    public static class response{
        public int count = 0;
        public items[] items = new items[0];
    }
    private static class frJson {
        public items[] response = new items[0];
    }
    public static ArrayList<items> getFriendsList(String token) {
        Gson gson = new Gson();
        String response = null;
        try {
            response = HttpGetter.sendGet("friends.get?fields=name&offset=13&v5.21",token);
        } catch (IOException e) {
            System.out.println("Can't get a friend list");
        }
        System.out.println(response);
        frJson obj = gson.fromJson(response,frJson.class);
        return new ArrayList<items>(Arrays.asList(obj.response));
    }


}

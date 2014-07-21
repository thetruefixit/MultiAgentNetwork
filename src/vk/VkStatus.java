package vk;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * Данный класс позволяет получать и устанавливать статусное сообщение в социальной сети Vk.com
 */

public class VkStatus {
    /*Шаблон для JSON */
    private static class response {
        private String text = "";
    }
    private static class statusJson {
        private response response = new response();
    }
    /*-------------------------------------------------*/

    private static String EditSpaces(String messages) {
        return messages.replaceAll(" ","%20");
    }
    //Установка статуса текушего юзера
    public static void setStatus(String text,String token) {
        try {
            HttpGetter.sendGet("status.set?text="+EditSpaces(text),token);
            System.out.println("Status updated!");
        } catch (IOException e) {
            System.out.println("Can't set status message");
        }
    }
    //Получение статуса текущего юзера
    public static String getStatus(String token) {
        String response = null;
        try {
            response = HttpGetter.sendGet("status.get?",token);
        } catch (IOException e) {
            return "Can't get status message";
        }

        Gson gson = new Gson();
        statusJson obj = gson.fromJson(response,statusJson.class);
        return obj.response.text;
    }
}

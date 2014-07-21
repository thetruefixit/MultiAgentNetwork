package vk;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 *
 * Данный класс используется для обработки http-запросов.
 * Также используется для парсинга токена из адресной строки браузера и последующей его обработки.
 */
class HttpGetter {
    private static String getContent(HttpResponse resp) throws IOException {
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(resp.getEntity().getContent())
        );
        StringBuffer result = new StringBuffer();
        String line ="";
        while((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
     public static String sendGet(String command,String token) throws IOException {
         String url = "https://api.vk.com/method/"+command+"&access_token="+token;
         HttpClient client = new DefaultHttpClient();
         HttpGet request = new HttpGet(url);
         HttpResponse response = client.execute(request);
         return getContent(response);
     }
}

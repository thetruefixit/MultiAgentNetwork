package yandex;

//http://api.yandex.ru/maps/doc/staticapi/1.x/dg/concepts/input_params.xml

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class geocode {

    private static class ResponseJson {
        private response response = new response();
    }
    private static class response {
        GeoObjectCollection GeoObjectCollection = new GeoObjectCollection();
    }
    private static class GeoObjectCollection {
        private F_Members[] featureMember = new F_Members[0];
    }
    private class F_Members {
        GeoObject GeoObject = new GeoObject();
    }
    private static class GeoObject {
        metaDataProperty metaDataProperty = new metaDataProperty();
    }
    private static class metaDataProperty {
        GeocoderMetaData GeocoderMetaData = new GeocoderMetaData();
    }
    private static class GeocoderMetaData {
        String text = ""; //Raw Adr data
        AddressDetails AddressDetails = new AddressDetails();
    }
    private static class AddressDetails {
        Country Country = new Country();
    }
    private static class Country {
        String CountryName = "";  // Страна
        AdministrativeArea AdministrativeArea = new AdministrativeArea();
    }
    private static class AdministrativeArea {
        String AdministrativeAreaName = ""; //Область, край и тд
        SubAdministrativeArea SubAdministrativeArea = new SubAdministrativeArea();
    }
    private static class SubAdministrativeArea{
        String SubAdministrativeAreaName = ""; //округ Города
        Locality Locality = new Locality();
    }
    private static class Locality {
        String LocalityName = "";     //Город
        Thoroughfare Thoroughfare = new Thoroughfare();
    }
    private static class Thoroughfare {
        String ThoroughfareName = "";  //Улица
        Premise Premise = new Premise();
    }
    private static class Premise {
        String PremiseNumber = ""; //Номер
    }

    private static String url = "http://geocode-maps.yandex.ru/1.x/?format=json&&result=1&geocode=";
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
    public static String SendRequest(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
            return getContent(response);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }
    public static types.GetData getAdr(String geocode) { //format: long,lat !!
        types.GetData Adr = new types.GetData();
        String Query = url+geocode;
        String json = SendRequest(Query);
        Gson gson = new Gson();
        ResponseJson obj = gson.fromJson(json,ResponseJson.class);
        try {
            Adr.Raw = obj.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.text;
            Adr.Country = obj.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country.CountryName;
            Adr.City = obj.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.LocalityName;
            Adr.Street = obj.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare.ThoroughfareName;
            Adr.Num = obj.response.GeoObjectCollection.featureMember[0].GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.Locality.Thoroughfare.Premise.PremiseNumber;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return Adr;
    }
}

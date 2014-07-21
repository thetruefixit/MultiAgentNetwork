package vk;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
/*
 * Данный класс отвечает за визуальную авторизацию в социальной сети Vk.com
 * Также используется для получение токена, при успешной авторизации.
 */
class Browser extends Region {

    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    public static String Access_Token = "";
    String app_id="3985695", // id-приложения, зарегистрированного в сети зарание, для получение доступа к пользовательской информации.
            scope="friends,wall,status,messages,photos,groups,music",  // Права доступа.
            redirect_uri="https://oauth.vk.com/blank.html",     //страница переадресации после успешной авторизации.
            display="popup",                                    //Вид окна при авторизации
            response_type="token";                              //Тип ответа: токен гарантирует доступ к данным пользователя в течение определенного времени ( ~ 1 день )


    public static String parsToken(String URL) {
        String result=URL.substring(URL.indexOf("access_token=")+"access_token=".length(),URL.indexOf("&expires_in"));
        return result;
    }
    public Browser() {
        //Подключаем стили
        getStyleClass().add("browser");
        // Загружаем страницу авторизации
        webEngine.load("http://oauth.vk.com/authorize?" +"client_id=" + app_id +"&scope=" + scope +"&redirect_uri=" + redirect_uri +"&display=" + display +"&response_type=" + response_type);
        //Открываем окно WebView
        getChildren().add(browser);
        //Добавляем слуховой аппарат, для определения того момента, когда будет выведен токен в адресную строку.
       webEngine.getLoadWorker().stateProperty().addListener(
               new ChangeListener<Worker.State>() {
                   @Override
                   public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State state2) {
                       if(state == state2.SUCCEEDED) {
                           if(webEngine.getLocation().indexOf("oauth.vk.com/blank.html#access_token=")>0){
                               Access_Token = parsToken(webEngine.getLocation());
                               webEngine.executeScript("location.replace('localhost')");
                               //todo исправить вход, должно перекидовать на страницу.
                           }
                       }
                   }
               }
       );


    }
    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}
package vk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * Класс, выполняющий инициализацию формы Browser с последующей обработкой полученных данных.
 */
public class Token extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
        Scene scene = new Scene(new Browser(), 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static String GetToken() throws IOException {
        String[] args=new String[10];
        Application.launch(args);
        if(Browser.Access_Token!="")
            return Browser.Access_Token;
        else
        {
            return "-1";
        }
    }
}

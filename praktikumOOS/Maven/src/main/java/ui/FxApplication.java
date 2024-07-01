package ui;
import bank.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class FxApplication extends javafx.application.Application {

    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainpage.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("OOS P5 - Bank");
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

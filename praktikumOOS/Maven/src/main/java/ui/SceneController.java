package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene_MainView(javafx.event.ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("mainpage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene =  new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene_AccountView(javafx.event.ActionEvent event, Parent _root) throws IOException{
        root = FXMLLoader.load(getClass().getClassLoader().getResource("accountview.fxml"));
        stage = (Stage)(_root.getScene().getWindow());
        scene =  new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

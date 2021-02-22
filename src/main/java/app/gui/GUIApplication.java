package app.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/Main.fxml"));

        Scene scene = new Scene(root);
        stage.setResizable(true);
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        stage.setHeight(450);
        stage.setWidth(600);

        stage.setScene(scene);
        stage.show();
    }

}

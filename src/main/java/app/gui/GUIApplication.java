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
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.setHeight(400);
        stage.setWidth(800);

        stage.setScene(scene);
        stage.show();
    }

}

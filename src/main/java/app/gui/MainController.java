package app.gui;

import app.gui.connectform.ConnectController;
import core.TCP.Connector;
import app.eventbus.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    IEventBus eventBus = EventBus.getInstance();
    Connector connector;

    @FXML
    private Text statusBarText;
    @FXML
    private Button statusIcon;
    @FXML
    private ConnectController connectController;

    private boolean isConnected = false;
    private boolean isListening = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBus.subscribe(StatusEvent.class, this::onStatusChange);
    }


    public void onStatusChange(StatusEvent event) {
        String status = event.getStatus();
        switch (status) {
            case StatusEvent.ALREADY_BUSY: {
                statusBarText.textProperty().set(statusBarText.getText() + " | Already running");
                System.out.println("Connection already existing");
                break;
            }
            case StatusEvent.SET_CONNECTED: {
                statusBarText.textProperty().set(event.getStatusText());
                System.out.println("Connected to ");
                break;
            }
            case StatusEvent.IS_LISTENING: {
                statusBarText.textProperty().set(event.getStatusText());
                System.out.println("Listening on ");
                break;
            }
            case StatusEvent.ENABLE_SEND: {
                System.out.println("enable");
                break;
            }
        }
    }

}

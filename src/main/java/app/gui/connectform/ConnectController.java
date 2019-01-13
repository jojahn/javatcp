package app.gui.connectform;

import app.eventbus.*;
import core.TCP.Connector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {
    ConnectModel model = new ConnectModel();
    IEventBus eventBus = EventBus.getInstance();
    Connector connector;

    private Boolean isConnected = false;
    private Boolean isListening = false;

    @FXML
    private TextField ipTextField;
    @FXML
    private TextField portTextField;
    @FXML
    private Button listenButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button stopButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectButton.setOnAction(this::onConnect);
        listenButton.setOnAction(this::onListen);
        stopButton.setOnAction(this::onStop);

        ipTextField.textProperty().bindBidirectional(model.ipProperty());
        portTextField.textProperty().bindBidirectional(model.portProperty());
    }

    public void onConnect(ActionEvent e){
        if(isListening) {
            eventBus.publish(new StatusEvent(StatusEvent.ALREADY_BUSY));
            return;
        }

        try {

            int port = Integer.parseInt(model.getPort());
            /* to-do */
            InetAddress localhost = InetAddress.getLocalHost();

            connector = new Connector(localhost, port);
            connector.connect();
            this.isConnected = true;

            eventBus.publish(new StatusEvent("Connecting ...", Color.STEELBLUE, 0));
            // eventBus.publish(new StatusEvent(StatusEvent.SET_CONNECTED));
            eventBus.publish(new StatusEvent(StatusEvent.ENABLE_SEND));
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            eventBus.publish(new StatusEvent("Connection Failed", Color.RED, 0));
        }
    }

    public void onListen(ActionEvent e) {
        if(isConnected) {
            eventBus.publish(new StatusEvent(StatusEvent.ALREADY_BUSY));
            return;
        }

        try {

            int port = Integer.parseInt(model.getPort());
            /* to-do */
            InetAddress localhost = InetAddress.getLocalHost();

            connector = new Connector(localhost, port);
            connector.listen();
            this.isListening = true;

            eventBus.publish(new StatusEvent("Starting Listening ... ", Color.STEELBLUE, 0));
            // eventBus.publish(new StatusEvent(StatusEvent.SET_CONNECTED, "Listening on (" + localhost + ":" + port + ")"));
            eventBus.publish(new StatusEvent(StatusEvent.ENABLE_SEND));
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            eventBus.publish(new StatusEvent("Listening Failed", Color.RED, 0));
        }
    }

    public void onStop(ActionEvent e){
        try {
            if(connector == null) {
                throw new NullPointerException("connector is empty");
            }
            else if(isConnected) {
                connector.close();
            } else if(isListening) {
                connector.stopListen();
            } else {
                throw new IllegalStateException("cannot stop if not started");
            }
            eventBus.publish(new StatusEvent("Disconnected", Color.GRAY, 0));
            eventBus.publish(new StatusEvent(StatusEvent.SET_DISCONNECTED));

            eventBus.publish(new StatusEvent(StatusEvent.DISABLE_SEND));
        } catch (Exception ex) {
            ex.printStackTrace();
            eventBus.publish(new StatusEvent("Disconnect failed", Color.ORANGERED, 0));
        }
    }
}

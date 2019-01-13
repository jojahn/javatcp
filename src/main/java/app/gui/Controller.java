package app.gui;

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

@Deprecated
public class Controller implements Initializable {

    Model model = new Model();
    IEventBus eventBus = EventBus.getInstance();
    Connector connector;

    private boolean isConnected = false;
    private boolean isListening = false;

    @FXML
    private TextField messageInput;
    @FXML
    private TextField messageOutput;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField inetAddressTextField;
    @FXML
    private Button sendButton;
    @FXML
    private Button connectButton;
    @FXML
    private Button listenButton;
    @FXML
    private Button startButton;
    @FXML
    private Text statusFormText;
    @FXML
    private Text statusBarText;
    @FXML
    private Button statusIcon;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendButton.setOnAction(this::onMessageEmit);
        connectButton.setOnAction(this::onConnect);
        listenButton.setOnAction(this::onListen);
        startButton.setOnAction(this::onDisconnect);

        messageInput.textProperty().bindBidirectional(model.messageInputProperty());
        messageOutput.textProperty().bindBidirectional(model.messageOutputProperty());
        portTextField.textProperty().bindBidirectional(model.portProperty());
        inetAddressTextField.textProperty().bindBidirectional(model.inetAddressProperty());

        messageInput.disableProperty().set(true);
        messageOutput.disableProperty().set(true);
        sendButton.disableProperty().set(true);

        eventBus.subscribe(OutputEvent.class, this::onOutputEvent);
        eventBus.subscribe(StatusEvent.class, this::onStateChange);
        eventBus.subscribe(TCPEvent.class, this::onMessage);
    }

    public void onConnect(ActionEvent e){
        try {
            int port = Integer.parseInt(model.getPort());
            InetAddress localhost = InetAddress.getLocalHost();
            connector = new Connector(localhost, port);
            connector.connect();
            this.isConnected = true;
            eventBus.publish(new StatusEvent("Connecting ...", Color.STEELBLUE, 0));
            statusIcon.getStyleClass().clear();
            statusIcon.getStyleClass().add("positive-status-icon");

            messageInput.disableProperty().set(false);
            messageOutput.disableProperty().set(false);
            sendButton.disableProperty().set(false);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            eventBus.publish(new StatusEvent("Connection Failed", Color.RED, 0));
        }
    }

    public void onListen(ActionEvent e) {
        try {
            int port = Integer.parseInt(model.getPort());
            InetAddress localhost = InetAddress.getLocalHost();
            connector = new Connector(localhost, port);
            connector.listen();
            this.isListening = true;
            eventBus.publish(new StatusEvent("Starting Listening ... ", Color.STEELBLUE, 0));
            statusIcon.getStyleClass().clear();
            statusIcon.getStyleClass().add("positive-status-icon");
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
            eventBus.publish(new StatusEvent("Listening Failed", Color.RED, 0));
        }
    }

    public void onDisconnect(ActionEvent e){
        try {
            if(connector == null) {
                throw new NullPointerException("connector is empty");
            }
            else if(isConnected) {
                connector.close();
            } else if(isListening) {
                connector.stopListen();
            }
            eventBus.publish(new StatusEvent("Disconnected", Color.GRAY, 0));
            statusIcon.getStyleClass().clear();
            statusIcon.getStyleClass().add("neutral-status-icon");

            messageInput.disableProperty().set(true);
            messageOutput.disableProperty().set(true);
            sendButton.disableProperty().set(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            statusIcon.getStyleClass().clear();
            statusIcon.getStyleClass().add("negative-status-icon");
            eventBus.publish(new StatusEvent("Disconnect failed", Color.ORANGERED, 0));
        }
    }

    public void onMessageEmit(ActionEvent e) {
        String message = model.getMessageInput();
        System.out.println("EVENT: " + message);
        if(isConnected) {
            connector.send(message);
        } else if(isListening) {
            connector.sendServer(message);
        }
        eventBus.publish(new StatusEvent("Sending \"" + message + "\"", Color.STEELBLUE, 1));
    }

    public void onMessage(TCPEvent event) {
        String message = (String) event.getSource();
        System.out.println("RECV.EVENT: " + message);
        eventBus.publish(new StatusEvent("Recieved \"" + message + "\"", Color.STEELBLUE, 1));
        model.setMessageOutput(message);
    }


    public void renderMessage(String message) {
        model.setMessageOutput(message);
    }

    private void onStateChange(StatusEvent event){
        statusFormText.setFill(event.getColor());
        statusFormText.textProperty().set(event.getStatus());

        if(event.getLevel() == 0) {
            statusBarText.textProperty().set(event.getStatus());
        }

        if (!isListening && !isConnected) {
            startButton.getStyleClass().clear();
            startButton.textProperty().set("Start");
            startButton.getStyleClass().add("positive-fancy-btn");
        } else {
            startButton.textProperty().set("Stop");
            startButton.getStyleClass().clear();
            startButton.getStyleClass().add("negative-fancy-btn");
            if(isListening) {
                messageInput.disableProperty().set(false);
                messageOutput.disableProperty().set(false);
                sendButton.disableProperty().set(false);
            }

        }
    }

    private void onOutputEvent(OutputEvent event) {
        String value = event.getOutput();
        this.renderMessage(value);
    }
}

package app.gui.sendform;

import app.eventbus.EventBus;
import app.eventbus.IEventBus;
import app.eventbus.TCPEvent;
import app.gui.connectform.ConnectModel;
import core.TCP.Connector;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class SendController implements Initializable {
    SendModel model = new SendModel();
    IEventBus eventBus = EventBus.getInstance();
    Connector connector;

    private Boolean isConnected = false;
    private Boolean isListening = false;

    @FXML
    private TextField inputTextField;
    @FXML
    private TextField outputTextField;
    @FXML
    private Button sendButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sendButton.setOnAction(this::onSend);

        inputTextField.textProperty().bindBidirectional(model.inputProperty());
        outputTextField.textProperty().bindBidirectional(model.outputProperty());

        eventBus.subscribe(TCPEvent.class, this::onRecieve);
    }


    public void onSend(ActionEvent e) {
        String input = model.getInput();
        System.out.println("Sending " + model.getInput());

        eventBus.publish(new TCPEvent(input, TCPEvent.SEND_TAG));
    }

    public void onRecieve(TCPEvent event) {
        if(event.getTag() != TCPEvent.RECIEVE_TAG) {
            return;
        }

        String output = event.getMessage();
        model.setOutput(output);
    }

}

package app.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Deprecated
public class Model {
    private StringProperty messageInput = new SimpleStringProperty();
    private StringProperty messageOutput = new SimpleStringProperty();
    private StringProperty port = new SimpleStringProperty();

    public String getMessageInput() {
        return messageInput.get();
    }

    public StringProperty messageInputProperty() {
        return messageInput;
    }

    public void setMessageInput(String messageInput) {
        this.messageInput.set(messageInput);
    }

    public String getMessageOutput() {
        return messageOutput.get();
    }

    public StringProperty messageOutputProperty() {
        return messageOutput;
    }

    public void setMessageOutput(String messageOutput) {
        this.messageOutput.set(messageOutput);
    }

    public String getPort() {
        return port.get();
    }

    public StringProperty portProperty() {
        return port;
    }

    public void setPort(String port) {
        this.port.set(port);
    }

    public String getInetAddress() {
        return inetAddress.get();
    }

    public StringProperty inetAddressProperty() {
        return inetAddress;
    }

    public void setInetAddress(String inetAddress) {
        this.inetAddress.set(inetAddress);
    }

    private StringProperty inetAddress = new SimpleStringProperty();

}

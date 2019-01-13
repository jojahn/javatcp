package app.gui.connectform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnectModel {
    private StringProperty ip = new SimpleStringProperty();
    private StringProperty port = new SimpleStringProperty();

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
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
}

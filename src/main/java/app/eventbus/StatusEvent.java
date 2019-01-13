package app.eventbus;

import javafx.scene.paint.Color;

import java.util.EventObject;

public class StatusEvent extends EventObject {
    public final static String ENABLE_SEND = "ENABLE_SEND";
    public final static String DISABLE_SEND = "DISABLE_SEND";
    public final static String IS_LISTENING = "IS_LISTENING";
    public final static String SET_CONNECTED = "SET_CONNECTED";
    public final static String SET_DISCONNECTED = "SET_DISCONNECTED";
    public final static String ALREADY_BUSY = "ALREADY_BUSY";

    private String status;
    private Color color;
    private int level;
    private String statusText;

    public StatusEvent(String status) {
        super(status);
        this.status = status;
    }

    public StatusEvent(String status, String statusText) {
        super(status);
        this.status = status;
        this.statusText = statusText;
    }


    public StatusEvent(String source, Color color) {
        super(source);
        this.status = source;
        this.color = color;
    }

    @Deprecated
    public StatusEvent(String source, Color color, int level) {
        super(source);
        this.status = source;
        this.color = color;
        this.level = level;
    }

    public String getStatus() {
        return this.status;
    }

    public Color getColor() {
        return this.color;
    }

    public int getLevel() {
        return this.level;
    }

    public String getStatusText() {
        return this.statusText;
    }
}

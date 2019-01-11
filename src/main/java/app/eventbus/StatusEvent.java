package app.eventbus;

import javafx.scene.paint.Color;

import java.util.EventObject;

public class StatusEvent extends EventObject {
    private String status;
    private Color color;
    private int level;

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
}

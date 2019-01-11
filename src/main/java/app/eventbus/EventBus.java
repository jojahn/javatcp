package app.eventbus;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EventBus implements IEventBus{

    private static EventBus instance;
    private Map<String, List<IEventBusListener>> subscribers;

    public static EventBus getInstance() {
        if(instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public EventBus() {
        subscribers = new TreeMap<>();
    }

    @Override
    public void publish(Object event) {
        Class type = event.getClass();
        List<IEventBusListener> listeners = subscribers.get(type.toString());
        if(listeners != null) {
            for(IEventBusListener listener : listeners) {
                listener.onEvent(event);
            }
        }
    }

    @Override
    public <T> void subscribe(Class<T> type, IEventBusListener<T> listener) {
        System.out.println(subscribers.get(type.toString()));
        if(subscribers.get(type.toString()) == null) {
            subscribers.put(type.toString(), new ArrayList<>());
        }

        subscribers.get(type.toString()).add(listener);
    }
}

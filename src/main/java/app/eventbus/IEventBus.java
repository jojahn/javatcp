package app.eventbus;

public interface IEventBus {

    void publish(Object event);

    <T> void subscribe(Class<T> type, IEventBusListener<T> listener);
}

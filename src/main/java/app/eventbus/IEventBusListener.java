package app.eventbus;

@FunctionalInterface
public interface IEventBusListener<T> {
    void onEvent(T event);
}

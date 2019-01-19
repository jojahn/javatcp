package core.JSON;

public interface JSONObject<T> {
    static <R> R initialize(Class<R> type) throws IllegalAccessException, InstantiationException {
        return type.newInstance();
    }
    Object[] getAttributes();
}

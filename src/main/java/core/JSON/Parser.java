package core.JSON;

import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.Attributes;

public class Parser {
    public static String stringify(Object object) throws IllegalAccessException {
        String result = "{";
        Map<String, Object> attributes = getAttributes(object);
        Iterator<String> iter = attributes.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            Object value = attributes.get(key);
            result += key + ":\"" + value + "\"";
            result += (iter.hasNext()) ? "," : "";
        }

        return result + "}";
    }

    public static String stringify(Object[] objects) throws IllegalAccessException {
        String result = "[";
        for(Object object : objects) {
            result += Parser.stringify(object);
        }
        return result + "]";
    }

    public static <T extends Object> T parse(String jsonString, Class<T> format) throws Exception {
        Object result = format.getClass().newInstance();
        T t = (T) result;
        List<Field> attributes = getAttributeFields(format);
        for (Field attribute : attributes) {
            attribute.set(t, jsonString);
        }
        return t;
    }

    private static List<String> splitJSON(String jsonString) throws IllegalArgumentException {
        List<String> result = new ArrayList<>();
        jsonString = minifyJSON(jsonString);

        if(!jsonString.matches()) {
            throw new IllegalArgumentException();
        }

        // Get JSON Object(s) as String
        if(jsonIsArray(jsonString)) {
            String[] objects = jsonString.split("}");
            for (String object : objects) {
                result.add(object + "}");
            }
        } else {
            result.add(jsonString);
        }

        return result;
    }

    private static List<Field> getAttributeFields(Class type){
        List<Field> attributes = new ArrayList<>();
        for(Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            attributes.add(field);
        }
        return attributes;
    }

    private static Map<String, Object> getAttributes(Object object) throws IllegalAccessException {
        Map<String, Object> attributes = new TreeMap<>();

        for(Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String key = field.getName();
            Object value = field.get(object);
            attributes.put(key, value);
        }

        return attributes;
    }
}

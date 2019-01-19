package core.JSON;

import java.lang.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Helper {
    private static final String JSON_ATTR_REGEX = "([a-zA-Z0-9])+(:)([a-zA-Z0-9])+";
    private static final String JSON_REGEX = "(\\{)(" + JSON_ATTR_REGEX + "(,))*" + JSON_ATTR_REGEX + "(})";
    private static final String JSON_ARRAY_REGEX = "(\\[)(" + JSON_REGEX + "(,))*" + JSON_REGEX + "(])";

    protected static <T extends Number> T getNumber(String value, Class<?> type) {
        Number result;
        if(type.equals(int.class) || type.equals(Integer.class)) {
            try {
                result = Integer.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
        }

        else if (type.equals(long.class) || type.equals(Long.class)) {
            try {
                result = Long.valueOf(value);
            } catch (Exception e) {
                result = null;
            }
        }

        else if (type.equals(float.class) || type.equals(Float.class)) {
            try {
                result = Float.valueOf(value);
            } catch (Exception e) {
                result = null;
            }
        }

        else if (type.equals(double.class) || type.equals(Double.class)) {
            try {
                result = Double.valueOf(value);
            } catch (Exception e) {
                result = null;
            }
        }

        else {
            result = null;
        }
        return (T) result;
    }

    protected static List<Field> getAttributeFields(Class type){
        List<Field> attributes = new ArrayList<>();
        for(Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            attributes.add(field);
        }
        return attributes;
    }

    protected static Map<String, Object> getAttributes(Object object) throws IllegalAccessException {
        Map<String, Object> attributes = new TreeMap<>();

        for(Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String key = field.getName();
            Object value = field.get(object);
            attributes.put(key, value);
        }

        return attributes;
    }

    private static boolean isJSONArray(String jsonString) {
        return jsonString.matches(JSON_ARRAY_REGEX);
    }

    private static boolean isSingleJSON(String jsonString) {
        return jsonString.matches(JSON_REGEX);
    }

    protected static boolean isValid(String jsonString) {
        return jsonString.matches(JSON_REGEX) || jsonString.matches(JSON_ARRAY_REGEX);
    }

    protected static List<Map<String, Object>> jsonToMapList(String jsonString) {
        List<String> objectStrings = getObjects(jsonString);
        Map<String, Object> attrs = new TreeMap<>();
        List<Map<String, Object>> objectsWithAttrs = new ArrayList<>();
        for(String object : objectStrings) {
            object = object.replace("}", "").replace("{", "");
            String[] attributes = object.split(",");
            for (String attribute : attributes) {
                if(!attribute.equals("")) {
                    String key = attribute.split(":")[0];
                    String value = attribute.split(":")[1];
                    attrs.put(key, value);
                }
            }
            objectsWithAttrs.add(attrs);
            attrs = new TreeMap<>();
        }

        return objectsWithAttrs;
    }

    protected static List<String> getObjects(String jsonString) throws IllegalArgumentException {
        List<String> result = new ArrayList<>();
        jsonString = Parser.minify(jsonString);

        // Get JSON Object(s) as String
        if(isJSONArray(jsonString)) {
            jsonString = jsonString.replace("[", "");
            jsonString = jsonString.replace("]", "");

            String[] objects = jsonString.split("}");
            for (String object : objects) {
                result.add(object + "}");
            }
        } else {
            result.add(jsonString);
        }

        return result;
    }
}

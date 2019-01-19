package core.JSON;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class Parser {
    public static String stringify(Object object) throws IllegalAccessException {
        String result = "{";
        Map<String, Object> attributes = Helper.getAttributes(object);
        Iterator<String> iter = attributes.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            Object value = attributes.get(key);
            result += key + ":" + value + "";
            result += (iter.hasNext()) ? "," : "";
        }

        return result + "}";
    }

    public static String stringify(Object[] objects) throws IllegalAccessException {
        String result = "[";
        for(Object object : objects) {
            result += Parser.stringify(object) + ",";
        }
        result = result.substring(0, result.length() - 1);
        return result + "]";
    }

    public static <T> T parse(String jsonString, Class<T> format) throws Exception {
        if(!Helper.isValid(jsonString)) {
            throw new IllegalArgumentException();
        }

        T t = format.newInstance();

        List<Field> attributes = Helper.getAttributeFields(format);
        List<Map<String, Object>> objects = Helper.jsonToMapList(jsonString);

        if(objects.size() > 1) {
            throw new IllegalArgumentException("JSON String is array but is parsed as single object");
        }

        Map<String, Object> values = objects.get(0);
        for (Field attribute : attributes) {
            String value = (String) values.get(attribute.getName());
            Object objectValue;
            Number number = Helper.getNumber(value, attribute.getType());
            if(number != null) {
                objectValue = number;
            } else {
                objectValue = value;
            }
            attribute.set(t, objectValue);
        }

        return t;
    }

    public static <T> T[] parse(String jsonString, Class<T> arrayType, int size) throws Exception {
        if(!Helper.isValid(jsonString)) {
            throw new IllegalArgumentException();
        }

        T[] t;

        List<Field> attributes = Helper.getAttributeFields(arrayType);
        List<Map<String, Object>> objects = Helper.jsonToMapList(jsonString);
        if(size < 0) {
            size = objects.size();
        }

        t = (T[]) Array.newInstance(arrayType, size);
        for (int i = 0; i < size; i++) {
            t[i] = arrayType.newInstance();
        }

        int i = 0;
        for (Map<String, Object> values : objects) {
            for (Field attribute : attributes) {
                String value = (String) values.get(attribute.getName());
                Object objectValue;
                Number number = Helper.getNumber(value, attribute.getType());
                if(number != null) {
                    objectValue = number;
                } else {
                    objectValue = value;
                }
                attribute.set(t[i], objectValue);
            }
            i++;
        }

        return t;
    }

    public static String minify(String jsonString) {
        return jsonString.replace("\n", "").replace(" ", "");
    }

    public static String beautify(String jsonString) {
        String result = "";
        for(String json : jsonString.split("\\{")) {
            json = json.replace("}", "");
            if(!json.equals("")) {
                result += "{\n";
                for (String attr : json.split(",")) {
                    result += " " + attr + "\n";
                }
                result += "}";
            }

        }
        return result;
    }

}

package core.JSON;

public class JSONMain {
    public static void main(String[] args) {
        JSONTestClass test = new JSONTestClass(1, "dev");

        try {
            String result = Parser.stringify(test);
            System.out.println(result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}


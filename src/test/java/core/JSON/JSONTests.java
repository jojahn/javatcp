package core.JSON;

import core.JSON.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class JSONTests{
    @Test
    public void testParsing() {
        String unparsed = "{id:9,name:java}";
        try {
            JSONTestClass test = Parser.parse(unparsed, JSONTestClass.class);
            assertEquals(9, test.getId());
            assertEquals("java", test.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Parse string should be valid!");
        }
    }
    @Test
    public void testParsingWithArray() {
        String unparsed = "[{id:9,name:java},{id:3,name:any}]";
        try {
            JSONTestClass[] array = new JSONTestClass[2];
            JSONTestClass[] test = Parser.parse(unparsed, JSONTestClass.class, 2);
            assertEquals(9, test[0].getId());
            assertEquals("java", test[0].getName());
            assertEquals(3, test[1].getId());
            assertEquals("any", test[1].getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Parse string should be valid!");
        }
    }
    @Test
    public void testStringify() {
        String expected = "{id:9,name:java}";
        try {
            JSONTestClass test = new JSONTestClass(9, "java");
            String actual = Parser.stringify(test);
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Parse Object should be valid!");
        }
    }
    @Test
    public void testStringifyWithArray() {
        String expected = "[{id:9,name:java},{id:3,name:any}]";
        try {
            JSONTestClass[] test = new JSONTestClass[2];
            test[0] = new JSONTestClass(9, "java");
            test[1] = new JSONTestClass(3, "any");
            String actual = Parser.stringify(test);
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("Parse Object should be valid!");
        }
    }
}

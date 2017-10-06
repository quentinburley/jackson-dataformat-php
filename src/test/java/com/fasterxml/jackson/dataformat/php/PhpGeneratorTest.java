/*
 * Copyright 2013 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import java.util.*;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpGeneratorTest extends BasePHPTestCase {
    /**
     * Test of parsing Strings into primitives
     */
    public void testSerialize_Primitives() throws Exception {
        String strVal = getMapper().writeValueAsString("foobar");
        assertEquals("s:6:\"foobar\"", strVal);

        //TODO: Test with UTF-8 characters inside strings strings

        assertEquals("b:1", getMapper().writeValueAsString(true));

        assertEquals("i:10", getMapper().writeValueAsString(10));

        assertEquals("d:10.0", getMapper().writeValueAsString(10.0));
    }

    public void testSerialize_Map() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("key", 8);
        map.put("key2", "value");
        String result = getMapper().writeValueAsString(map);
        assertEquals("a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}", result);
    }

    public void testSerialize_Array() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("foo");
        list.add("bar");
        String result = getMapper().writeValueAsString(list);
        assertEquals("a:2:{i:0;s:3:\"foo\";i:1;s:3:\"bar\";}", result);
    }

    public void testSerialize_Object() throws Exception {
        String result = getMapper().writeValueAsString(new MyClass());
        assertEquals("a:2:{s:3:\"foo\";s:3:\"baz\";s:3:\"bar\";i:10;}", result);       
    }

    private class MyClass {
        public String foo = "baz";
        public Integer bar = 10;
    }
}

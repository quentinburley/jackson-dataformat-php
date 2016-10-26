/*
 * Copyright 2013 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpGeneratorTest extends TestCase {
    ObjectMapper mapper;

    public PhpGeneratorTest(final String testName) {
        super(testName);
        mapper = new ObjectMapper(new PhpFactory());
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of parsing Strings into primitives
     */
    public void testSerialize_Primitives() throws Exception {
        String strVal = mapper.writeValueAsString("foobar");
        assertEquals("s:6:\"foobar\"", strVal);

        //TODO: Test with UTF-8 characters inside strings strings

        assertEquals("b:1", mapper.writeValueAsString(true));

        assertEquals("i:10", mapper.writeValueAsString(10));

        assertEquals("d:10.0", mapper.writeValueAsString(10.0));
    }

    public void testSerialize_Map() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("key", 8);
        map.put("key2", "value");
        String result = mapper.writeValueAsString(map);
        assertEquals("a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}", result);
    }

    public void testSerialize_Array() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("foo");
        list.add("bar");
        String result = mapper.writeValueAsString(list);
        assertEquals("a:2:{i:0;s:3:\"foo\";i:1;s:3:\"bar\";}", result);
    }

    public void testSerialize_Object() throws Exception {
        String result = mapper.writeValueAsString(new MyClass());
        assertEquals("a:2:{s:3:\"foo\";s:3:\"baz\";s:3:\"bar\";i:10;}", result);       
    }

    private class MyClass {
        public String foo = "baz";
        public Integer bar = 10;
    }
}

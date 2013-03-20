/*
 * Copyright 2013 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.*;
import junit.framework.TestCase;

/**
 *
 * @author Joshua Hollander <jhollander@photobucket.com>
 */
public class PhpGeneratorTest extends TestCase {
    ObjectMapper mapper;

    public PhpGeneratorTest(String testName) {
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
    public void testParse_Primitives() throws Exception {
        String strVal = mapper.writeValueAsString("foobar");
        assertEquals("s:6:\"foobar\"", strVal);

        //TODO: Test with UTF-8 characters inside strings strings

        assertEquals("b:1", mapper.writeValueAsString(true));

        assertEquals("i:10", mapper.writeValueAsString(10));

        assertEquals("d:10.0", mapper.writeValueAsString(10.0));
    }

    public void testParse_Map() throws Exception {
        HashMap map = new LinkedHashMap();
        map.put("key", 8);
        map.put("key2", "value");
        String result = mapper.writeValueAsString(map);
        assertEquals("a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}", result);

        /*
        values = mapper.readValue("a:2:{i:0;s:5:\"value\";i:1;s:6:\"value2\";}", LinkedHashMap.class);
        assertNotNull(values);
        assertEquals(2, values.size());

        values = mapper.readValue("a:2:{i:0;s:5:\"value\";i:1;a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}}", LinkedHashMap.class);
        assertNotNull(values);
        assertEquals(2, values.size());*/
    }

    public void testParse_Object() throws Exception {
        /*
        Map values = mapper.readValue("O:7:\"MyClass\":2:{s:3:\"foo\";i:10;s:3:\"bar\";i:20;}", HashMap.class);
        assertNotNull(values);
        assertEquals(2, values.keySet().size());
        assertEquals(2, values.values().size());
        assertEquals(10L, values.get("foo"));
        assertEquals(20L, values.get("bar"));*/
    }
}

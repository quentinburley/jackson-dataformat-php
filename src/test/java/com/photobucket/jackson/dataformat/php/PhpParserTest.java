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
public class PhpParserTest extends TestCase {
    ObjectMapper mapper;

    public PhpParserTest(String testName) {
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
        String strVal = mapper.readValue("s:11:\"mediaCounts\"", String.class);
        assertEquals("mediaCounts", strVal);

        strVal = mapper.readValue("s:23:\"This is a longer string\"", String.class);
        assertEquals("This is a longer string", strVal);

        //TODO: Test with UTF-8 characters inside strings strings

        boolean boolVal = mapper.readValue("b:1", Boolean.class);
        assertEquals(true, boolVal);

        int intVal = mapper.readValue("i:10", Integer.class);
        assertEquals(10, intVal);

        intVal = mapper.readValue("i:-10", Integer.class);
        assertEquals(-10, intVal);

        double doubleVal = mapper.readValue("d:10.0", Double.class);
        assertEquals(10.0, doubleVal);

        float floatVal = mapper.readValue("d:10.0", Float.class);
        assertEquals(10.0F, floatVal);
    }

    public void testParse_Map() throws Exception {
        Map values = mapper.readValue("a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}", HashMap.class);
        assertNotNull(values);
        assertEquals(2, values.keySet().size());
        assertEquals(2, values.values().size());
        assertEquals(8L, values.get("key"));
        assertEquals("value", values.get("key2"));

        values = mapper.readValue("a:2:{i:0;s:5:\"value\";i:1;s:6:\"value2\";}", LinkedHashMap.class);
        assertNotNull(values);
        assertEquals(2, values.size());

        values = mapper.readValue("a:2:{i:0;s:5:\"value\";i:1;a:2:{s:3:\"key\";i:8;s:4:\"key2\";s:5:\"value\";}}", LinkedHashMap.class);
        assertNotNull(values);
        assertEquals(2, values.size());
    }

    public void testParse_Object() throws Exception {
        Map values = mapper.readValue("O:7:\"MyClass\":2:{s:3:\"foo\";i:10;s:3:\"bar\";i:20;}", HashMap.class);
        assertNotNull(values);
        assertEquals(2, values.keySet().size());
        assertEquals(2, values.values().size());
        assertEquals(10L, values.get("foo"));
        assertEquals(20L, values.get("bar"));
    }
}

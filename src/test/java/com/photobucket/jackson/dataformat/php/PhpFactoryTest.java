/*
 * Copyright 2013 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import junit.framework.TestCase;

/**
 *
 * @author Joshua Hollander <jhollander@photobucket.com>
 */
public class PhpFactoryTest extends TestCase {
    public PhpFactoryTest(String testName) {
        super(testName);
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
     * Test of createParser method, of class PhpFactory.
     */
    public void testCreateParser_String() throws Exception {
        PhpFactory instance = new PhpFactory();
        PhpParser result = instance.createParser("");
        assertNotNull(result);
    }
}

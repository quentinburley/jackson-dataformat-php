/*
 * Copyright 2013 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import junit.framework.TestCase;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpFactoryTest extends TestCase {

    /**
     * Test of createParser method, of class PhpFactory.
     */
    public void testCreateParser_String() throws Exception {
        PhpFactory instance = new PhpFactory();
        PhpParser result = instance.createParser("");
        assertNotNull(result);
    }
}

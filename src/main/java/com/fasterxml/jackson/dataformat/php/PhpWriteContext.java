/*
 * Copyright 2012 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteContext;

/**
 * Decorator around the JsonWriteContext object because it has a bunch of final methods that 
 * needed to have functionality added to them.
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpWriteContext {
    private JsonWriteContext _jsonCtx;

    private PhpWriteContext _parent;

    private PhpWriteContext _child;

    private int _fieldCount = 0;

    private Writer _writer;

    protected PhpWriteContext(final JsonWriteContext jsonContext, final PhpWriteContext parent,
                              final Writer writer) {
        _jsonCtx = jsonContext;
        _parent = parent;
        _writer = writer;
        if(_writer == null) {
            _writer = new StringWriter();
        }
    } 

    public static PhpWriteContext createRootContext(final Writer writer) {
        return new PhpWriteContext(JsonWriteContext.createRootContext(null), null, writer);
    }

    public final PhpWriteContext createChildArrayContext()
    {
        PhpWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new PhpWriteContext(_jsonCtx.createChildArrayContext(), this, null);
            return ctxt;
        }
        return ctxt.reset();
    }

    private PhpWriteContext reset() {
        _fieldCount = 0;
        return this;
    }

    public final PhpWriteContext createChildObjectContext()
    {
        PhpWriteContext ctxt = _child;
        if (ctxt == null) {
            _child = ctxt = new PhpWriteContext(_jsonCtx.createChildObjectContext(), this, null);
            return ctxt;
        }
        return ctxt.reset();
    }

    public final PhpWriteContext getParent() { return _parent; }

    public final String getCurrentName() { return _jsonCtx.getCurrentName(); }

    public final int writeFieldName(final String name) throws JsonProcessingException {
        _fieldCount++;
        return _jsonCtx.writeFieldName(name);
    }

    public final int writeValue() {
        return _jsonCtx.writeValue();
    }

    public int getFieldCount() { return _fieldCount; }

    public void incrementFieldCount() { _fieldCount++; }

    public Writer getWriter() {
        return _writer;
    }

    public void append(final CharSequence string) throws IOException {
        _writer.append(string);
    }

    public boolean inObject() {
        return _jsonCtx.inObject();
    }

    public boolean inArray() {
        return _jsonCtx.inArray();
    }

    public String getTypeDesc() {
        return _jsonCtx.getTypeDesc();
    }
}

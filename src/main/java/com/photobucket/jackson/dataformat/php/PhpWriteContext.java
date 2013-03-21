/*
 * Copyright 2012 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import com.fasterxml.jackson.core.json.JsonWriteContext;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Decorator around the JsonWriteContext object because it has a bunch of final methods that 
 * needed to have functionality added to them.
 * 
 * @author jhollander
 */
public class PhpWriteContext {
    private JsonWriteContext _jsonCtx;

    private PhpWriteContext _parent;

    private PhpWriteContext _child;

    private int _fieldCount = 0;

    private Writer _writer;

    protected PhpWriteContext(JsonWriteContext jsonContext, PhpWriteContext parent, Writer writer) {
        _jsonCtx = jsonContext;
        _parent = parent;
        _writer = writer;
        if(_writer == null) {
            _writer = new StringWriter();
        }
    } 

    public static PhpWriteContext createRootContext(Writer writer) {
        return new PhpWriteContext(JsonWriteContext.createRootContext(), null, writer);
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

    public final int writeFieldName(String name) {
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

    public void append(CharSequence string) throws IOException {
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

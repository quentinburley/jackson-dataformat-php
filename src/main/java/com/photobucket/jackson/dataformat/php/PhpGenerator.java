/*
 * Copyright 2013 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Joshua Hollander <jhollander@photobucket.com>
 */
public class PhpGenerator extends GeneratorBase {
    private static final String DELIM = ":";

    private static final String EOR = ";";

    final protected Writer _writer;

    private PhpWriteContext _ctx;
    
    PhpGenerator(IOContext ctxt, ObjectCodec codec, Writer out) {
         super(0, codec);
         _writer = out;
         _ctx = PhpWriteContext.createRootContext(_writer);
    }

    /*
    /**********************************************************
    /* Overridden configuration methods
    /**********************************************************
     */
    @Override
    public Object getOutputTarget() {
        return _writer;
    }


    /*
    /**********************************************************
    /* Overridden methods
    /**********************************************************
     */

    @Override
    public void writeFieldName(String name)  throws IOException, JsonGenerationException
    {
        int status = _ctx.writeFieldName(name);
        if (status == JsonWriteContext.STATUS_EXPECT_VALUE) {
            _reportError("Can not write a field name, expecting a value");
        }
        _writeString(name);
    }

    @Override
    public void writeFieldName(SerializableString name)
        throws IOException, JsonGenerationException
    {
        writeFieldName(name.getValue());
    }
    
    /*
    /**********************************************************
    /* Output method implementations, structural
    /**********************************************************
     */

    @Override
    public void writeStartArray() throws IOException, JsonGenerationException
    {
        _verifyValueWrite("start an array");
        _ctx = _ctx.createChildArrayContext();
        //Can't actually write anything here because we need to know how many fields this array will have
    }

    @Override
    public void writeEndArray() throws IOException, JsonGenerationException
    {
        if (!_ctx.inArray()) {
            _reportError("Current context not an ARRAY but "+_ctx.getTypeDesc());
        }
        _flushArray();
    }

    @Override
    public void writeStartObject() throws IOException, JsonGenerationException
    {
        _ctx = _ctx.createChildObjectContext();
        //Can't actually write anything here because we need to know how many fields this array will have
    }

    @Override
    public void writeEndObject() throws IOException, JsonGenerationException
    {
        if (!_ctx.inObject()) {
            _reportError("Current context not an object but "+_ctx.getTypeDesc());
        }
        _flushArray();
    }

    private void _flushArray() throws IOException {
        //can't actually write an object back to PHP.  what would we call it?  Just use an associative array instead
        Writer writer = _ctx.getParent().getWriter();
        writer.append("a");
        writer.append(DELIM);
        writer.append(Integer.toString(_ctx.getFieldCount()));
        writer.append(DELIM);
        writer.append("{");
        writer.append(_ctx.getWriter().toString());
        writer.append("}");
        _ctx = _ctx.getParent();
    }

    @Override
    public void flush() throws IOException {
        _writer.flush();
    }

    @Override
    public void close() throws IOException {
        _closed = true;
    }

    @Override
    protected void _releaseBuffers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException, JsonGenerationException {
        //?
    }

    @Override
    public void writeString(String text) throws IOException, JsonGenerationException {
        _writeString(text);
    }

    @Override
    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        _writeString(new String(text).substring(offset, len));
    }

    @Override
    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void _writeString(String name) throws IOException {
        _ctx.append("s");
        _ctx.append(DELIM);
        _ctx.append(Integer.toString(name.length()));
        _ctx.append(DELIM);
        _ctx.append("\"");
        _ctx.append(name);
        _ctx.append("\"");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeRaw(String text) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRaw(char c) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(int v) throws IOException, JsonGenerationException {
        _writeInt(v);
    }

    @Override
    public void writeNumber(long v) throws IOException, JsonGenerationException {
        _writeInt(v);
    }

    @Override
    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        _writeInt(v);
    }

    private void _writeInt(Number v) throws IOException {
        _ctx.append("i");
        _ctx.append(DELIM);
        _ctx.append(v.toString());
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeNumber(double d) throws IOException, JsonGenerationException {
        _writeDouble(d);
    }

    @Override
    public void writeNumber(float f) throws IOException, JsonGenerationException {
        _writeDouble(f);
    }

    @Override
    public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException {
        _writeDouble(dec);
    }

    private void _writeDouble(Number v) throws IOException {
        _ctx.append("d");
        _ctx.append(DELIM);
        _ctx.append(v.toString());
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        _ctx.append("b");
        _ctx.append(DELIM);
        _ctx.append(state ? "1" : "0");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
        _ctx.writeValue();
    }

    @Override
    public void writeNull() throws IOException, JsonGenerationException {
        _ctx.append("N");
        if(_ctx.inArray() || _ctx.inObject()) {
            _ctx.append(EOR);
        }
    }

}

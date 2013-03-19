/*
 * Copyright 2013 Photobucket 
 */
package com.photobucket.jackson.dataformat.php;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.io.IOContext;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Joshua Hollander <jhollander@photobucket.com>
 */
public class PhpGenerator extends GeneratorBase {

    PhpGenerator(IOContext ctxt, ObjectCodec codec, Writer out) {
         super(0, codec);
    }

    @Override
    public void flush() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _releaseBuffers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _verifyValueWrite(String typeMsg) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeStartArray() throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeEndArray() throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeStartObject() throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeEndObject() throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeFieldName(String name) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeString(String text) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeUTF8String(byte[] text, int offset, int length) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(long v) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(BigInteger v) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(double d) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(float f) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(BigDecimal dec) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNumber(String encodedValue) throws IOException, JsonGenerationException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeBoolean(boolean state) throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeNull() throws IOException, JsonGenerationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

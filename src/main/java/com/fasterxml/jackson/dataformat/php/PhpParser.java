/*
 * Copyright 2013 Photobucket 
 */
package com.fasterxml.jackson.dataformat.php;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.base.ParserMinimalBase;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.json.JsonReadContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.core.util.TextBuffer;
import com.fasterxml.jackson.dataformat.php.cfg.PackageVersion;

/**
 *
 * @author Joshua Hollander
 * @author Stephan Wienczny
 */
public class PhpParser extends ParserMinimalBase {
    private static final int DELIM = (int) ':';

    private static final int EOR = (int) ';';
    private final IOContext _ioContext;
    private final TextBuffer _textBuffer;
    /**
     * Need to keep track of underlying {@link Reader} to be able to auto-close it (if required to)
     */
    protected Reader _reader;
    protected boolean _closed;
    private String _currentValue;
    private boolean _assumeUTF8 = true;
    private ObjectCodec _objectCodec;
    private JsonReadContext _parsingContext;
    private int _inputPtr = 0;

    private int _currInputRowStart = 0;

    public PhpParser(final IOContext ctxt,final  BufferRecycler recycler, final int parserFeatures,
                     final ObjectCodec objectCodec, final Reader reader) {
        _objectCodec = objectCodec;
        _reader = reader;
        _ioContext = ctxt;
        _textBuffer = ctxt.constructTextBuffer();
        _parsingContext = JsonReadContext.createRootContext(null);
    }

    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        if (_closed) {
            return null;
        }

        int i = _reader.read();

        if (i == INT_RCURLY) {
            if (!_parsingContext.inArray() || !_parsingContext.inObject()) {
                //_reportMismatchedEndMarker(i, '}');
            }
            return _currToken = JsonToken.END_OBJECT;
        }

        if (_parsingContext.inObject() && _currToken != JsonToken.FIELD_NAME) {
            determineNextToken(i);
            _parsingContext.setCurrentName(_currentValue);
            _currToken = JsonToken.FIELD_NAME;
            return _currToken;
        }
         
        return (_currToken = determineNextToken(i));
    }

    @Override
    protected void _handleEOF() throws JsonParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCurrentName() throws IOException, JsonParseException {
        if (_currToken == JsonToken.START_OBJECT || _currToken == JsonToken.START_ARRAY) {
            JsonReadContext parent = _parsingContext.getParent();
            return parent.getCurrentName();
        }
        return _parsingContext.getCurrentName();
    }

    @Override
    public void close() throws IOException {
        if (!_closed) {
            _closed = true;
            try {
                _reader.close();
            } finally {
                _textBuffer.releaseBuffers();
            }
        }
    }

    @Override
    public boolean isClosed() {
        return _closed;
    }

    @Override
    public JsonStreamContext getParsingContext() {
        return _parsingContext;
    }

    @Override
    public void overrideCurrentName(final String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getText() throws IOException, JsonParseException {
        return _currentValue;
    }

    @Override
    public char[] getTextCharacters() throws IOException, JsonParseException {
        return _currentValue.toCharArray();
    }

    @Override
    public boolean hasTextCharacters() {
        return _currentValue != null && _currentValue.length() > 0;
    }

    @Override
    public int getTextLength() throws IOException, JsonParseException {
        return _currentValue.length();
    }

    @Override
    public int getTextOffset() throws IOException, JsonParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getBinaryValue(final Base64Variant bv) throws IOException, JsonParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void _reportUnexpectedChar(final int got, final int expected) throws JsonParseException {
        _reportError("Expected "+((char) expected)+" but got '"+((char) got)+"'");
    }

    private JsonToken determineNextToken(int i) throws IOException {
        boolean inObject = _parsingContext.inObject();
        JsonToken t = null;
        switch ((char) i) {
            case 'i':
                t = parseInt();
                break;
            case 'd':
                t = parseFloat();
                break;
            case 'b':
                t = parseBoolean();
                break;
            case 's':
                t = parseString();
                consumeEOR();
                break;
            case 'a':
                if (!inObject) {
                    //TODO: deal with line and col on context
                    _parsingContext = _parsingContext.createChildObjectContext(0, 0);
                }
                t = JsonToken.START_OBJECT; //php arrays are essentially maps
                readLength(); //first read the lenght (and discard, it's not useful)
                i = _reader.read(); //now read the delimiter
                if(i != INT_LCURLY) {
                    _reportUnexpectedChar(i, INT_LCURLY);
                }
                break;
            case 'O':
                if (!inObject) {
                    _parsingContext = _parsingContext.createChildObjectContext(0, 0);
                }
                //the name of the object is the next value
                //read it and discard it, since there is no equivalent JSON functionality
                //and it would mean nothing in Java.
                parseString();
                consumeDELIM();
                readLength(); //now read the length of the object itself
                consumeDELIM();
                i = _reader.read(); //now read the delimiter
                if(i != INT_LCURLY) {
                    _reportUnexpectedChar(i, INT_LCURLY);
                }
                t = JsonToken.START_OBJECT;
                break;
            case 'N':
                t = JsonToken.VALUE_NULL;
                break;
            default:
                _reportError("Unknown type: '"+((char) i)+"'");
        }
        return t;
    }

    /**
     * Reads the length of the next field if there is a length descriptor available
     *
     * @return
     * @throws IOException
     */
    private int readLength() throws IOException {
        int c = _reader.read();
        if (c != (char) ':') {
            return 0; //throw exception?
        }
        StringBuilder b = new StringBuilder();
        c = _reader.read();
        while (c != -1 && c != (int) ':') {
            b.append((char) c);
            c = _reader.read();
        }
        if (b.length() > 0) {
            return Integer.parseInt(b.toString());
        }
        return 0;
    }

    private JsonToken parseString() throws IOException {
        int strLen = readLength();

        //remove the leading "
        int ch = _reader.read();
        if(ch != '"') {
            _reportUnexpectedChar(ch, '"');
        }

        StringBuilder b = new StringBuilder(strLen);
        int byteCount = 0;
        while (byteCount != strLen) {
            ch = _reader.read();
            b.append((char) ch);
            if (_assumeUTF8) {
                if ((ch >= 0x0001) && (ch <= 0x007F)) {
                    byteCount++;
                } else if (ch > 0x07FF) {
                    byteCount += 3;
                } else {
                    byteCount += 2;
                }
            } else {
                byteCount++;
            }
        }
        _currentValue = b.toString();
        //consume the closing "
        ch = _reader.read();
        if(ch != '"') {
            _reportUnexpectedChar(ch, '"');
        }

        return JsonToken.VALUE_STRING;
    }

    private void consumeEOR() throws IOException {
        //consume the field delimiter
        int ch = _reader.read();
        if(ch != -1 && ch != EOR) {
            _reportUnexpectedChar(ch, EOR);
        }
    }

    private void consumeDELIM() throws IOException {
        //consume the field delimiter
        int ch = _reader.read();
        if(ch != -1 && ch != DELIM) {
            _reportUnexpectedChar(ch, EOR);
        }
    }

    private void readNextValue() throws IOException {
        int c = _reader.read();
        //read the delimiter
        if (c != DELIM) {
            _reportUnexpectedChar(c, DELIM);
        }
        StringBuilder b = new StringBuilder();
        c = _reader.read();
        while (c != -1 && c != EOR) {
            b.append((char) c);
            c = _reader.read();
        }
        _currentValue = b.toString();
    }

    private JsonToken parseBoolean() throws IOException {
        readNextValue();
        if (_currentValue.equals("1")) {
            _currentValue = "true";
        } else if (_currentValue.equals("0")) {
            _currentValue = "false";
        }
        return Boolean.parseBoolean(_currentValue) ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
    }

    private JsonToken parseFloat() throws IOException {
        readNextValue();
        return JsonToken.VALUE_NUMBER_FLOAT;
    }

    private JsonToken parseInt() throws IOException {
        readNextValue();
        return JsonToken.VALUE_NUMBER_INT;
    }

    @Override
    public ObjectCodec getCodec() {
        return _objectCodec;
    }

    @Override
    public void setCodec(ObjectCodec oc) {
        _objectCodec = oc;
    }

    @Override
    public Version version() {
        return PackageVersion.VERSION;
    }

    @Override
    public JsonLocation getTokenLocation() {
        return JsonLocation.NA;
    }

    @Override
    public JsonLocation getCurrentLocation() {
        //TODO: make this work for realz
        /*int col = _inputPtr - _currInputRowStart + 1; // 1-based
        return new JsonLocation(_ioContext.getSourceReference(),
                                _currInputProcessed + _inputPtr - 1,
                                _currInputRow, col);*/
        return new JsonLocation(_ioContext.getSourceReference(), 0, 0, 0);
    }

    @Override
    public Number getNumberValue() throws IOException, JsonParseException {
        //TODO: deal with BigInt and also try to use Int wherever possible
        return Long.parseLong(_currentValue);
    }

    @Override
    public NumberType getNumberType() throws IOException, JsonParseException {
        //TODO: deal with BigInt and also try to use Int wherever possible
        if(_currToken == JsonToken.VALUE_NUMBER_INT) {
            return NumberType.INT;
        } else if (_currToken == JsonToken.VALUE_NUMBER_FLOAT) {
            return NumberType.FLOAT;
        }
        return null;
    }

    @Override
    public int getIntValue() throws IOException, JsonParseException {
        return Integer.parseInt(_currentValue);
    }

    @Override
    public long getLongValue() throws IOException, JsonParseException {
        return Long.parseLong(_currentValue);
    }

    @Override
    public BigInteger getBigIntegerValue() throws IOException, JsonParseException {
        return new BigInteger(_currentValue);
    }

    @Override
    public float getFloatValue() throws IOException, JsonParseException {
        return Float.parseFloat(_currentValue);
    }

    @Override
    public double getDoubleValue() throws IOException, JsonParseException {
        return Double.parseDouble(_currentValue);
    }

    @Override
    public BigDecimal getDecimalValue() throws IOException, JsonParseException {
        return new BigDecimal(_currentValue);
    }

    @Override
    public Object getEmbeddedObject() throws IOException, JsonParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getValueAsString() throws IOException, JsonParseException {
        return _currentValue;
    }
}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lemberg Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ls.database.model;

import android.database.Cursor;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class CursorParser {

    private Cursor mCursor;

    public CursorParser(Cursor cursor) {
        mCursor = cursor;
    }

    public String readString(String columnName) {
        return mCursor.getString(mCursor.getColumnIndex(columnName));
    }

    public Byte readByte(String columnName) {
        return (byte) mCursor.getInt(mCursor.getColumnIndex(columnName));
    }

    public Short readShort(String columnName) {
        return mCursor.getShort(mCursor.getColumnIndex(columnName));
    }

    public Integer readInteger(String columnName) {
        return mCursor.getInt(mCursor.getColumnIndex(columnName));
    }

    public Long readLong(String columnName) {
        return mCursor.getLong(mCursor.getColumnIndex(columnName));
    }

    public Float readFloat(String columnName) {
        return mCursor.getFloat(mCursor.getColumnIndex(columnName));
    }

    public Double readDouble(String columnName) {
        return mCursor.getDouble(mCursor.getColumnIndex(columnName));
    }

    public Boolean readBoolean(String columnName) {
        return mCursor.getInt(mCursor.getColumnIndex(columnName)) > 0;
    }

    public byte[] readBlob(String columnName) {
        return mCursor.getBlob(mCursor.getColumnIndex(columnName));
    }

    /**
     * Just releases reference from cursor. Parser cannot be used after calling this method.
     */
    public void releaseResources() {
        mCursor = null;
    }
}

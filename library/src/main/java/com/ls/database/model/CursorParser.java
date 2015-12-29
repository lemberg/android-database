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
        return readString(mCursor.getColumnIndex(columnName));
    }

    public Byte readByte(String columnName) {
        return readByte(mCursor.getColumnIndex(columnName));
    }

    public Short readShort(String columnName) {
        return readShort(mCursor.getColumnIndex(columnName));
    }

    public Integer readInteger(String columnName) {
        return readInteger(mCursor.getColumnIndex(columnName));
    }

    public Long readLong(String columnName) {
        return readLong(mCursor.getColumnIndex(columnName));
    }

    public Float readFloat(String columnName) {
        return readFloat(mCursor.getColumnIndex(columnName));
    }

    public Double readDouble(String columnName) {
        return readDouble(mCursor.getColumnIndex(columnName));
    }

    public Boolean readBoolean(String columnName) {
        return readBoolean(mCursor.getColumnIndex(columnName));
    }

    public byte[] readBlob(String columnName) {
        return readBlob(mCursor.getColumnIndex(columnName));
    }

    public String readString(int columnIndex) {
        return mCursor.getString(columnIndex);
    }

    public Byte readByte(int columnIndex) {
        return (byte) mCursor.getInt(columnIndex);
    }

    public Short readShort(int columnIndex) {
        return mCursor.getShort(columnIndex);
    }

    public Integer readInteger(int columnIndex) {
        return mCursor.getInt(columnIndex);
    }

    public Long readLong(int columnIndex) {
        return mCursor.getLong(columnIndex);
    }

    public Float readFloat(int columnIndex) {
        return mCursor.getFloat(columnIndex);
    }

    public Double readDouble(int columnIndex) {
        return mCursor.getDouble(columnIndex);
    }

    public Boolean readBoolean(int columnIndex) {
        return mCursor.getInt(columnIndex) > 0;
    }

    public byte[] readBlob(int columnIndex) {
        return mCursor.getBlob(columnIndex);
    }

    public int getColumnIndex(String columnName) {
        return mCursor.getColumnIndex(columnName);
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public int getPosition() {
        return mCursor.getPosition();
    }

    public boolean isFirst() {
        return mCursor.isFirst();
    }

    public boolean isLast() {
        return mCursor.isLast();
    }

    public boolean isBeforeFirst() {
        return mCursor.isBeforeFirst();
    }

    public boolean isAfterLast() {
        return mCursor.isAfterLast();
    }

    public String[] getColumnNames() {
        return mCursor.getColumnNames();
    }

    public int getColumnCount() {
        return mCursor.getColumnCount();
    }

    public boolean isNull(int columnIndex) {
        return mCursor.isNull(columnIndex);
    }

    public boolean isClosed() {
        return mCursor.isClosed();
    }

    /**
     * Just releases reference from cursor. Parser cannot be used after calling this method.
     */
    public void releaseResources() {
        mCursor = null;
    }
}

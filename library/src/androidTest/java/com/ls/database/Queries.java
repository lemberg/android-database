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
package com.ls.database;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class Queries {

    public static final String CREATE_DATA_TYPES_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.DataTypes.NAME
            + " ("
            + Tables.DataTypes.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + Tables.DataTypes.COLUMN_STRING + " TEXT,"
            + Tables.DataTypes.COLUMN_BYTE + " INTEGER,"
            + Tables.DataTypes.COLUMN_SHORT + " INTEGER,"
            + Tables.DataTypes.COLUMN_INTEGER + " INTEGER,"
            + Tables.DataTypes.COLUMN_LONG + " INTEGER,"
            + Tables.DataTypes.COLUMN_FLOAT + " NUMBER,"
            + Tables.DataTypes.COLUMN_DOUBLE + " NUMBER,"
            + Tables.DataTypes.COLUMN_BOOLEAN + " INTEGER,"
            + Tables.DataTypes.COLUMN_BLOB + " BLOB,"
            + Tables.DataTypes.COLUMN_ENUM + " TEXT"
            + ");";

    public static final String CREATE_SIMPLE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " + Tables.SimpleTable.NAME
            + " ("
            + Tables.SimpleTable.COLUMN_ID + " INTEGER PRIMARY KEY,"
            + Tables.SimpleTable.COLUMN_NAME + " TEXT"
            + ");";
}

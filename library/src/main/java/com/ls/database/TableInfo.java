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

import android.text.TextUtils;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class TableInfo {

    private final String tableName;
    private final String createTableQuery;

    /**
     * Info that helps to do initialization of database table.
     */
    public TableInfo(String tableName, String createTableQuery) {
        if (TextUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("Table name query can't be empty");
        }

        if (TextUtils.isEmpty(createTableQuery)) {
            throw new IllegalArgumentException("Create: SQL query can't be empty");
        }

        this.tableName = tableName;
        this.createTableQuery = createTableQuery;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCreateTableQuery() {
        return createTableQuery;
    }
}

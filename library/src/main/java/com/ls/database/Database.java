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

import com.ls.database.model.ConflictType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class Database {

    private Context appContext;

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;

    private AtomicInteger openCounter = new AtomicInteger(0);

    Database(Context context, SQLiteOpenHelper sqLiteOpenHelper) {
        appContext = context.getApplicationContext();
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    public synchronized void open() {
        if (openCounter.incrementAndGet() == 1) {
            sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        }
    }

    public synchronized void close() {
        if (openCounter.decrementAndGet() == 0) {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
                sqLiteDatabase = null;
            }
        }
    }

    public synchronized boolean isOpened() {
        return openCounter.get() > 0;
    }

    public void beginTransaction() {
        checkState();

        sqLiteDatabase.beginTransaction();
    }

    public void setTransactionSuccessful() {
        checkState();

        sqLiteDatabase.setTransactionSuccessful();
    }

    public void endTransaction() {
        checkState();

        sqLiteDatabase.endTransaction();
    }

    public boolean inTransaction() {
        checkState();

        return sqLiteDatabase.inTransaction();
    }

    public void execSQL(String sql) {
        checkState();

        sqLiteDatabase.execSQL(sql);
    }

    public void execSQL(String sql, Object[] bindArgs) {
        checkState();

        sqLiteDatabase.execSQL(sql, bindArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues initialValues, ConflictType type) {
        checkState();

        return sqLiteDatabase.insertWithOnConflict(table, nullColumnHack, initialValues, type.getValue());
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs, ConflictType type) {
        checkState();

        return sqLiteDatabase.updateWithOnConflict(table, values, whereClause, whereArgs, type.getValue());
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        checkState();

        return sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        checkState();

        return sqLiteDatabase.rawQuery(sql, selectionArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        checkState();

        return sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public String getResourceQuery(int resId) {
        return appContext.getString(resId);
    }

    synchronized void shutdown() {
        openCounter.set(0);

        sqLiteDatabase = null;

        sqLiteOpenHelper.close();
        sqLiteOpenHelper = null;
    }

    private void checkState() {
        if (sqLiteDatabase == null) {
            throw new IllegalStateException("Database is already closed");
        }
    }
}

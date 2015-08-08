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
import com.ls.database.model.IDBHelper;
import com.ls.database.model.IDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
class Database implements IDatabase {

    private Context mContext;

    private SQLiteDatabase mSQLiteDatabase;
    private SQLiteHelper mDBHelper;

    private int mVersion;
    private String mName;

    private AtomicInteger mDbCounter = new AtomicInteger(0);

    Database(Context context, IDBHelper idbHelper) {
        mContext = context.getApplicationContext();
        mName = idbHelper.getDatabaseName(context.getApplicationContext());
        mVersion = idbHelper.getDatabaseVersion(context.getApplicationContext());

        mDBHelper = new SQLiteHelper(context, idbHelper);
    }

    @Override
    public synchronized void open() {
        if (mDbCounter.incrementAndGet() == 1) {
            mSQLiteDatabase = mDBHelper.getWritableDatabase();
        }
    }

    @Override
    public synchronized void close() {
        if (mDbCounter.decrementAndGet() == 0) {
            if (mSQLiteDatabase != null) {
                mSQLiteDatabase.close();
                mSQLiteDatabase = null;
            }
        }
    }

    @Override
    public synchronized boolean isOpened() {
        return mDbCounter.get() > 0;
    }

    @Override
    public void beginTransaction() {
        checkState();

        mSQLiteDatabase.beginTransaction();
    }

    @Override
    public void setTransactionSuccessful() {
        checkState();

        mSQLiteDatabase.setTransactionSuccessful();
    }

    @Override
    public void endTransaction() {
        checkState();

        mSQLiteDatabase.endTransaction();
    }

    @Override
    public boolean inTransaction() {
        checkState();

        return mSQLiteDatabase.inTransaction();
    }

    @Override
    public void execSQL(String sql) {
        checkState();

        mSQLiteDatabase.execSQL(sql);
    }

    @Override
    public void execSQL(String sql, Object[] bindArgs) {
        checkState();

        mSQLiteDatabase.execSQL(sql, bindArgs);
    }

    @Override
    public long insert(String table, String nullColumnHack, ContentValues initialValues, ConflictType type) {
        checkState();

        return mSQLiteDatabase.insertWithOnConflict(table, nullColumnHack, initialValues, type.getValue());
    }

    @Override
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs, ConflictType type) {
        checkState();

        return mSQLiteDatabase.updateWithOnConflict(table, values, whereClause, whereArgs, type.getValue());
    }

    @Override
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        checkState();

        return mSQLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public Cursor rawQuery(String sql, String[] selectionArgs) {
        checkState();

        return mSQLiteDatabase.rawQuery(sql, selectionArgs);
    }

    @Override
    public int delete(String table, String whereClause, String[] whereArgs) {
        checkState();

        return mSQLiteDatabase.delete(table, whereClause, whereArgs);
    }

    @Override
    public String getResourceQuery(int resId) {
        return mContext.getString(resId);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public int getVersion() {
        return mVersion;
    }

    synchronized void shutdown() {
        mDbCounter.set(0);

        mSQLiteDatabase = null;

        mDBHelper.close();
        mDBHelper = null;
    }

    private void checkState() {
        if (mSQLiteDatabase == null) {
            throw new IllegalStateException("Database is already closed");
        }
    }
}

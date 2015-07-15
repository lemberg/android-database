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

import com.ls.database.model.IDBHelper;

import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
class PrimaryDatabase extends BaseDatabase {

    private SQLiteHelper mDBHelper;

    private AtomicInteger mDbCounter = new AtomicInteger(0);

    PrimaryDatabase(Context context, IDBHelper idbHelper) {
        super(
                context.getApplicationContext(),
                idbHelper.getDatabaseName(context.getApplicationContext()),
                idbHelper.getDatabaseVersion(context.getApplicationContext())
        );

        mDBHelper = new SQLiteHelper(context, idbHelper);
    }

    @Override
    public synchronized void open() {
        if (mDbCounter.incrementAndGet() == 1) {
            setSQLiteDatabase(mDBHelper.getWritableDatabase());
        }
    }

    @Override
    public synchronized void close() {
        if (mDbCounter.decrementAndGet() == 0) {
            if (getSQLiteDatabase() != null) {
                getSQLiteDatabase().close();
                setSQLiteDatabase(null);
            }
        }
    }

    @Override
    public synchronized boolean isOpened() {
        return mDbCounter.get() > 0;
    }

    @Override
    void checkState() {
        if (getSQLiteDatabase() == null) {
            throw new IllegalStateException("Database is already closed");
        }
    }

    synchronized void shutdown() {
        mDbCounter.set(0);

        setSQLiteDatabase(null);

        mDBHelper.close();
        mDBHelper = null;
    }
}

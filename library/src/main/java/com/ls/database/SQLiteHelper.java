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
import com.ls.database.model.IMigrationTask;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
class SQLiteHelper extends SQLiteOpenHelper {

    private Context mContext;
    private IDBHelper mIDBHelper;

    SQLiteHelper(Context context, IDBHelper idbHelper) {
        super(
                context.getApplicationContext(),
                idbHelper.getDatabaseName(context.getApplicationContext()),
                null,
                idbHelper.getDatabaseVersion(context.getApplicationContext())
        );

        mContext = context.getApplicationContext();
        mIDBHelper = idbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        mIDBHelper.onConfigure(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        mIDBHelper.onOpen(mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final Map<Integer, IMigrationTask> map = getSortedMigrationTasks(MigrationType.UPGRADE);

        if (map.isEmpty()) {
            throw new SQLiteException("Can't upgrade database from version " +
                    oldVersion + " to " + newVersion);
        }

        try {
            for (Map.Entry<Integer, IMigrationTask> entry : map.entrySet()) {
                if (entry.getKey() > oldVersion && entry.getKey() <= newVersion) {
                    IMigrationTask mt = entry.getValue();
                    mt.onMigrate(db);
                }
            }
        } catch (SQLiteException e) {
            try {
                mIDBHelper.onUpgradeMigrationFailed(mContext, db, oldVersion, newVersion);
            } finally {
                drop(db);
                create(db);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final Map<Integer, IMigrationTask> map = getSortedMigrationTasks(MigrationType.DOWNGRADE);

        if (map.isEmpty()) {
            throw new SQLiteException("Can't downgrade database from version " +
                    oldVersion + " to " + newVersion);
        }

        try {
            for (Map.Entry<Integer, IMigrationTask> entry : map.entrySet()) {
                if (entry.getKey() < oldVersion && entry.getKey() >= newVersion) {
                    IMigrationTask mt = entry.getValue();
                    mt.onMigrate(db);
                }
            }
        } catch (SQLiteException e) {
            try {
                mIDBHelper.onDowngradeMigrationFailed(mContext, db, oldVersion, newVersion);
            } finally {
                drop(db);
                create(db);
            }
        }
    }

    private Map<Integer, IMigrationTask> getSortedMigrationTasks(MigrationType type) {

        Map<Integer, IMigrationTask> tasks;
        Comparator<Map.Entry<Integer, IMigrationTask>> comparator;

        switch (type) {
            case UPGRADE:
                tasks = mIDBHelper.getUpgradeMigrationTasks(mContext);

                comparator = new Comparator<Map.Entry<Integer, IMigrationTask>>() {

                    @Override
                    public int compare(Map.Entry<Integer, IMigrationTask> left, Map.Entry<Integer, IMigrationTask> right) {
                        return left.getKey().compareTo(right.getKey());
                    }
                };
                break;

            case DOWNGRADE:
                tasks = mIDBHelper.getDowngradeMigrationTasks(mContext);

                comparator = new Comparator<Map.Entry<Integer, IMigrationTask>>() {

                    @Override
                    public int compare(Map.Entry<Integer, IMigrationTask> left, Map.Entry<Integer, IMigrationTask> right) {
                        return right.getKey().compareTo(left.getKey());
                    }
                };
                break;

            default:
                throw new IllegalArgumentException("Wrong migration type");
        }

        if (tasks == null || tasks.isEmpty()) {
            return new LinkedHashMap<>();
        }

        List<Map.Entry<Integer, IMigrationTask>> entries = new ArrayList<>(tasks.entrySet());
        Collections.sort(entries, comparator);

        Map<Integer, IMigrationTask> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, IMigrationTask> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private void create(SQLiteDatabase db) {
        List<TableInfo> tables = mIDBHelper.getTables(mContext);
        if (tables != null) {
            for (TableInfo table : tables) {
                db.execSQL(table.getCreateTableQuery());
            }
        }
    }

    private void drop(SQLiteDatabase db) {
        List<TableInfo> tables = mIDBHelper.getTables(mContext);
        if (tables != null) {
            for (TableInfo table : tables) {
                db.execSQL(table.getDropTableQuery());
            }
        }
    }

    private enum MigrationType {
        UPGRADE, DOWNGRADE
    }
}

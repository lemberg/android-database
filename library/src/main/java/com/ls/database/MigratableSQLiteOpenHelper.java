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

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import com.ls.database.model.IMigrationTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public abstract class MigratableSQLiteOpenHelper extends BaseSQLiteOpenHelper {

    public MigratableSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MigratableSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Returns set of migration tasks. {@link Integer} - version of database,
     * {@link IMigrationTask} - task which will be executed for version of database.
     *
     * If map will be empty then {@link android.database.sqlite.SQLiteException} will be thrown.
     *
     * Tasks will be called in increasing order according to key versions, they will be sorted before upgrading.
     */
    public abstract Map<Integer, IMigrationTask> getUpgradeMigrationTasks(Context context);

    /**
     * Calls if database migration has failed with {@link android.database.sqlite.SQLiteException}.
     * This method serves to give possibility to correct some data which depends on database migration.
     * After this method database will be recreated automatically using data of {@link #getTablesInfo(Context)}.
     */
    public abstract void onUpgradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion);

    /**
     * Returns set of migration tasks. {@link Integer} - version of database,
     * {@link IMigrationTask} - task which will be executed for version of database.
     *
     * Called when the database needs to be downgraded.
     * It is called whenever current version is newer than requested one.
     * This method executes within a transaction.
     * If an exception is thrown, all changes will automatically be rolled back.
     *
     * It will be called with SDK version {@link android.os.Build.VERSION_CODES#HONEYCOMB}.
     *
     * Tasks will be called in decreasing order according to key versions, they will be sorted before downgrading.
     *
     * If map will be empty then {@link android.database.sqlite.SQLiteException} will be thrown.
     */
    public abstract Map<Integer, IMigrationTask> getDowngradeMigrationTasks(Context context);

    /**
     * Calls if database migration has failed with {@link android.database.sqlite.SQLiteException}.
     * This method serves to give possibility to correct some data which depends on database migration.
     * After this method database will be recreated automatically using data of {@link #getTablesInfo(Context)}.
     *
     * It will be called with SDK version {@link android.os.Build.VERSION_CODES#HONEYCOMB}.
     */
    public abstract void onDowngradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion);

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
                onUpgradeMigrationFailed(getAppContext(), db, oldVersion, newVersion);
            } finally {
                dropDatabase(db);
                createDatabase(db);
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
                onDowngradeMigrationFailed(getAppContext(), db, oldVersion, newVersion);
            } finally {
                dropDatabase(db);
                createDatabase(db);
            }
        }
    }

    private Map<Integer, IMigrationTask> getSortedMigrationTasks(MigrationType type) {

        Map<Integer, IMigrationTask> tasks;
        Comparator<Map.Entry<Integer, IMigrationTask>> comparator;

        switch (type) {
            case UPGRADE:
                tasks = getUpgradeMigrationTasks(getAppContext());

                comparator = new Comparator<Map.Entry<Integer, IMigrationTask>>() {

                    @Override
                    public int compare(Map.Entry<Integer, IMigrationTask> left, Map.Entry<Integer, IMigrationTask> right) {
                        return left.getKey().compareTo(right.getKey());
                    }
                };
                break;

            case DOWNGRADE:
                tasks = getDowngradeMigrationTasks(getAppContext());

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

    private enum MigrationType {
        UPGRADE, DOWNGRADE
    }
}

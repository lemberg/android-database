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

import com.ls.database.TableInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Map;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public interface IDBHelper {

    /**
     * Returns list of SQL scripts to create tables of database.
     */
    List<TableInfo> getTables(Context context);

    /**
     * Called when the database connection is being configured. <p> This method is called before {@link com.ls.database.SQLiteHelper#onCreate}, {@link
     * com.ls.database.SQLiteHelper#onUpgrade}, {@link com.ls.database.SQLiteHelper#onDowngrade}, or {@link #onOpen} are called.
     *
     * It should not modify the database except to configure the database connection as required.</p>
     *
     * <p> This method should only call methods that configure the parameters of the database connection, such as {@link SQLiteDatabase#enableWriteAheadLogging}
     * {@link SQLiteDatabase#setForeignKeyConstraintsEnabled}, {@link SQLiteDatabase#setLocale}, {@link
     * SQLiteDatabase#setMaximumSize}, or executing PRAGMA statements. </p>
     *
     * It will be called with SDK version {@link android.os.Build.VERSION_CODES#JELLY_BEAN}.
     */
    void onConfigure(Context context, SQLiteDatabase database);

    /**
     * Called when the database has been opened.  The implementation should check {@link SQLiteDatabase#isReadOnly} before updating the database. <p> This
     * method is called after the database connection has been configured and after the database schema has been created, upgraded or downgraded as necessary. If the database
     * connection must be configured in some way before the schema is created, upgraded, or downgraded, do it in {@link #onConfigure} instead. </p>
     */
    void onOpen(Context context, SQLiteDatabase database);

    /**
     * Returns set of migration tasks. {@link Integer} - version of database, {@link IMigrationTask} - task which will be executed for
     * version of database.
     *
     * If map will be empty then {@link android.database.sqlite.SQLiteException} will be thrown.
     *
     * Tasks will be called in increasing order according to key versions, they will be sorted before upgrading.
     */
    Map<Integer, com.ls.database.model.IMigrationTask> getUpgradeMigrationTasks(Context context);

    /**
     * Calls if database migration has failed with {@link android.database.sqlite.SQLiteException}. This method serves to give possibility to correct some data which depends on database migration. After this method database will be
     * recreated automatically using data of {@link #getTables(Context)}.
     */
    void onUpgradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion);

    /**
     * Returns set of migration tasks. {@link Integer} - version of database, {@link IMigrationTask} - task which will be executed for
     * version of database.
     *
     * Called when the database needs to be downgraded. It is called whenever current version is newer than requested one. This method executes within a transaction. If an
     * exception is thrown, all changes will automatically be rolled back.
     *
     * It will be called with SDK version {@link android.os.Build.VERSION_CODES#HONEYCOMB}.
     *
     * Tasks will be called in decreasing order according to key versions, they will be sorted before downgrading.
     *
     * If map will be empty then {@link android.database.sqlite.SQLiteException} will be thrown.
     */
    Map<Integer, com.ls.database.model.IMigrationTask> getDowngradeMigrationTasks(Context context);

    /**
     * Calls if database migration has failed with {@link android.database.sqlite.SQLiteException}. This method serves to give possibility to correct some data which depends on database migration. After this method database will be
     * recreated automatically using data of {@link #getTables(Context)}.
     *
     * It will be called with SDK version {@link android.os.Build.VERSION_CODES#HONEYCOMB}.
     */
    void onDowngradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion);

    /**
     * Returns database name.
     */
    String getDatabaseName(Context context);

    /**
     * Returns database version.
     */
    int getDatabaseVersion(Context context);
}

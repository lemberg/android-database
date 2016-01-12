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

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class DatabaseRegister {

    private Context mContext;
    private Map<String, Database> mDatabaseMap;

    /**
     * Make sure that you create only one instance of {@link DatabaseRegister} in your system to prevent issues related to database working.
     */
    public DatabaseRegister(Context context) {
        mContext = context.getApplicationContext();
        mDatabaseMap = new HashMap<>();
    }

    public void shutdownAndClear() {
        for (Database database : mDatabaseMap.values()) {
            database.shutdown();
        }

        mDatabaseMap.clear();
    }

    /**
     * @param sqLiteOpenHelper A sqLiteOpenHelper class to manage database creation and version management.
     * {@link MigratableSQLiteOpenHelper} helps to manage data migration while database upgrading or downgrading.
     */
    public void addDatabase(BaseSQLiteOpenHelper sqLiteOpenHelper) {
        if (!mDatabaseMap.containsKey(sqLiteOpenHelper.getDbName())) {
            Database database = new Database(mContext, sqLiteOpenHelper);
            mDatabaseMap.put(sqLiteOpenHelper.getDbName(), database);
        }
    }

    Database getDatabase(String databaseName) {
        if (TextUtils.isEmpty(databaseName)) {
            throw new IllegalArgumentException("Database name is not specified");
        }

        Database database = mDatabaseMap.get(databaseName);

        if (database == null) {
            throw new IllegalArgumentException("Database with name " + databaseName + " was not added");
        }

        return database;
    }
}

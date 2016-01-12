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
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public abstract class BaseSQLiteOpenHelper extends SQLiteOpenHelper {

	private Context appContext;
	private String dbName;

	public BaseSQLiteOpenHelper(
			Context context,
			String name,
			SQLiteDatabase.CursorFactory factory,
			int version) {

		super(context, name, factory, version);

		this.appContext = context.getApplicationContext();
		this.dbName = name;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public BaseSQLiteOpenHelper(
			Context context,
			String name,
			SQLiteDatabase.CursorFactory factory,
			int version,
			DatabaseErrorHandler errorHandler) {

		super(context.getApplicationContext(), name, factory, version, errorHandler);

		this.appContext = context.getApplicationContext();
		this.dbName = name;
	}

	/**
	 * Returns list of SQL scripts to create tables of database.
	 */
	public abstract List<TableInfo> getTablesInfo(Context context);

	protected String getDbName() {
		return dbName;
	}

	protected Context getAppContext() {
		return appContext;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDatabase(db);
	}

	/**
	 * Creates database structure according to list of {@link TableInfo} from {@link #getTablesInfo(Context)}
	 */
	protected void createDatabase(SQLiteDatabase db) {
		List<TableInfo> tables = getTablesInfo(appContext);
		if (tables != null) {
			for (TableInfo table : tables) {
				db.execSQL(table.getCreateTableQuery());
			}
		}
	}

	/**
	 * Drops database structure according to list of {@link TableInfo} from {@link #getTablesInfo(Context)}
	 */
	protected void dropDatabase(SQLiteDatabase db) {
		List<TableInfo> tables = getTablesInfo(getAppContext());
		if (tables != null) {
			for (TableInfo table : tables) {
				db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
			}
		}
	}
}

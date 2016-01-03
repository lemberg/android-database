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

import com.ls.database.model.IMigrationTask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.test.InstrumentationTestCase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class DBHelperTest extends InstrumentationTestCase {

    private boolean upgradedTo2;
    private boolean upgradedTo3;

    private boolean downgradedTo2;
    private boolean downgradedTo1;

    private boolean upgradeFailed;
    private boolean downgradeFailed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        resetInputData();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        resetInputData();
    }

    private void resetInputData() {
        upgradedTo2 = false;
        upgradedTo3 = false;

        downgradedTo2 = false;
        downgradedTo1 = false;

        upgradeFailed = false;
        downgradeFailed = false;
    }

    public void testUpgradeMigration1to2() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_1));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_2));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue(
                "Upgrade Version 1 to 2 - " + upgradedTo2
                        + ", Upgrade Version 2 to 3 - " + upgradedTo3
                        + ", upgrade failed - " + upgradeFailed,
                upgradedTo2 && !upgradedTo3 && !upgradeFailed
        );
    }

    public void testUpgradeMigration2to3() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_2));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_3));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue(
                "Upgrade Version 1 to 2 - " + upgradedTo2
                        + ", Upgrade Version 2 to 3 - "
                        + upgradedTo3 + ", upgrade failed - " + upgradeFailed,
                !upgradedTo2 && upgradedTo3 && !upgradeFailed
        );
    }

    public void testUpgradeMigration1to3() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_1));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_3));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue(
                "Upgrade Version 1 to 2 - " + upgradedTo2
                        + ", Upgrade Version 2 to 3 - " + upgradedTo3
                        + ", upgrade failed - " + upgradeFailed,
                upgradedTo2 && upgradedTo3 && !upgradeFailed
        );
    }

    @MinSdkVersion(versionCode = Build.VERSION_CODES.HONEYCOMB)
    public void testDowngradeMigration3to2() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_3));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_2));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        assertTrue(
                "Downgrade Version 3 to 2 - " + downgradedTo2
                        + ", Downgrade Version 2 to 1 -  - "
                        + downgradedTo1 + ", downgrade failed - " + downgradeFailed,
                downgradedTo2 && !downgradedTo1 && !downgradeFailed
        );
    }

    @MinSdkVersion(versionCode = Build.VERSION_CODES.HONEYCOMB)
    public void testDowngradeMigration2to1() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_2));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_1));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue(
                "Downgrade Version 3 to 2 - " + downgradedTo2
                        + ", Downgrade Version 2 to 1 -  - " + downgradedTo1
                        + ", downgrade failed - " + downgradeFailed,
                !downgradedTo2 && downgradedTo1 && !downgradeFailed
        );
    }

    @MinSdkVersion(versionCode = Build.VERSION_CODES.HONEYCOMB)
    public void testDowngradeMigration3to1() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_3));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_1));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue(
                "Downgrade Version 3 to 2 - " + downgradedTo2
                        + ", Downgrade Version 2 to 1 -  - " + downgradedTo1
                        + ", downgrade failed - " + downgradeFailed,
                downgradedTo2 && downgradedTo1 && !downgradeFailed
        );
    }

    public void testUpgradeFailed() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_3));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_4));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue("Upgrade has to be failed", upgradeFailed);
    }

    @MinSdkVersion(versionCode = Build.VERSION_CODES.HONEYCOMB)
    public void testDowngradeFailed() {
        DatabaseRegister databaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_5));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        resetInputData();

        databaseRegister.addDatabase(TestingHelper.DB_NAME, new TestingHelper(TestingHelper.VERSION_4));
        databaseRegister.getDatabase(TestingHelper.DB_NAME).open();
        databaseRegister.getDatabase(TestingHelper.DB_NAME).close();

        databaseRegister.shutdownAndClear();

        assertTrue("Downgrade has to be failed", downgradeFailed);
    }

    @Override
    protected void runTest() throws Throwable {
        String fName = getName();
        assertNotNull(fName);
        Method method = null;
        try {
            method = getClass().getMethod(fName, (Class[]) null);
        } catch (NoSuchMethodException e) {
            fail("Method \"" + fName + "\" not found");
        }

        boolean canRun = true;

        MinSdkVersion minSdkVersion = method.getAnnotation(MinSdkVersion.class);
        if (minSdkVersion != null) {
            canRun = Build.VERSION.SDK_INT >= minSdkVersion.versionCode();
        }

        if (canRun) {
            super.runTest();
        }
    }

    private class TestingHelper extends MigratableSQLiteOpenHelper {

        public static final String DB_NAME = "helper_test_database.db";

        public static final int VERSION_1 = 1;
        public static final int VERSION_2 = 2;
        public static final int VERSION_3 = 3;

        //Used for upgrade/downgrade fail test
        public static final int VERSION_4 = 4;

        //Used for upgrade/downgrade fail test
        public static final int VERSION_5 = 5;

        int version;

        public TestingHelper(int version) {
            super(getInstrumentation().getTargetContext(), DB_NAME, null, version);

            this.version = version;
        }

        @Override
        public List<TableInfo> getTablesInfo(Context context) {
            List<TableInfo> tableInfo = new ArrayList<>();

            tableInfo.add(new TableInfo(Tables.SimpleTable.NAME, Queries.CREATE_SIMPLE_DATA_TABLE));

            return tableInfo;
        }

        @Override
        public Map<Integer, IMigrationTask> getUpgradeMigrationTasks(Context context) {
            Map<Integer, IMigrationTask> taskMap = new HashMap<>();

            taskMap.put(VERSION_2, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    upgradedTo2 = true;
                }
            });

            taskMap.put(VERSION_3, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    upgradedTo3 = true;
                }
            });

            taskMap.put(VERSION_4, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    throw new SQLiteException("Test upgraded failed");
                }
            });

            return taskMap;
        }

        @Override
        public void onUpgradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion) {
            upgradeFailed = true;
        }

        @Override
        public Map<Integer, IMigrationTask> getDowngradeMigrationTasks(Context context) {
            Map<Integer, IMigrationTask> taskMap = new HashMap<>();

            taskMap.put(VERSION_2, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    downgradedTo2 = true;
                }
            });

            taskMap.put(VERSION_1, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    downgradedTo1 = true;
                }
            });

            taskMap.put(VERSION_4, new IMigrationTask() {
                @Override
                public void onMigrate(SQLiteDatabase sqLiteDatabase) {
                    throw new SQLiteException("Test upgraded failed");
                }
            });

            return taskMap;
        }

        @Override
        public void onDowngradeMigrationFailed(Context context, SQLiteDatabase database, int oldVersion, int newVersion) {
            downgradeFailed = true;
        }
    }
}

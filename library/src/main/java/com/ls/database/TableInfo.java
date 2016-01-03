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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class TableInfo {

    private final String tableName;
    private final String createTableQuery;
    private boolean enableForeignKey = false;

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

    /**
     * Info that helps to do initialization of database table.
     */
    public TableInfo(Builder builder) {
        if (builder == null) {
            throw new NullPointerException("Builder == null");
        }

        this.tableName = builder.tableName;
        this.createTableQuery = builder.build();
    }

    public String getTableName() {
        return tableName;
    }

    public String getCreateTableQuery() {
        return createTableQuery;
    }

    public boolean shouldEnableForeignKey() {
        return enableForeignKey;
    }

    public void setEnableForeignKey(boolean enableForeignKey) {
        this.enableForeignKey = enableForeignKey;
    }

    public static class Builder {

        private final String tableName;
        private final List<FieldInfo> fields = new ArrayList<>();
        private final List<ForeignKey> foreignKeys = new ArrayList<>();
        private String[] multiplePrimaryKeys = new String[0];
        private String[] multipleUnique = new String[0];

        public Builder(String tableName) {
            if (TextUtils.isEmpty(tableName)) {
                throw new IllegalArgumentException("Table name query can't be empty");
            }

            this.tableName = tableName;
        }

        public Builder addField(FieldInfo fieldInfo) {
            fields.add(fieldInfo);

            return this;
        }

        public Builder addForeignKey(ForeignKey foreignKey) {
            foreignKeys.add(foreignKey);

            return this;
        }

        public Builder setMultiplePrimaryKeys(String...columns) {
            if (columns == null) {
                multiplePrimaryKeys = new String[0];
            }

            multiplePrimaryKeys = columns;

            return this;
        }

        public Builder setMultipleUnique(String...columns) {
            if (columns == null) {
                multipleUnique = new String[0];
            }

            multipleUnique = columns;

            return this;
        }

        private String build() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CREATE TABLE IF NOT EXISTS");
            stringBuilder.append(" ");
            stringBuilder.append(tableName);
            stringBuilder.append(" ");
            stringBuilder.append("(");

            for (Iterator<FieldInfo> iterator = fields.iterator(); iterator.hasNext(); ) {
                FieldInfo field =  iterator.next();

                stringBuilder.append(field.name);
                stringBuilder.append(" ");
                stringBuilder.append(field.dataType.value);

                if (field.primaryKey != FieldInfo.PrimaryKey.NONE) {
                    stringBuilder.append(" ");
                    stringBuilder.append("PRIMARY KEY");

                    stringBuilder.append(" ");
                    switch (field.primaryKey) {

                        case ASC:
                            stringBuilder.append("ASC");
                            break;

                        case DESC:
                            stringBuilder.append("DESC");
                            break;
                    }
                }

                if (field.autoincrement) {
                    stringBuilder.append(" ");
                    stringBuilder.append("AUTOINCREMENT");
                }

                if (field.unique) {
                    stringBuilder.append(" ");
                    stringBuilder.append("UNIQUE");
                }

                if (field.notNull) {
                    stringBuilder.append(" ");
                    stringBuilder.append("NOT NULL");
                }

                if (!TextUtils.isEmpty(field.defaultValue)) {
                    stringBuilder.append(" ");
                    stringBuilder.append("DEFAULT");
                    stringBuilder.append(" ");
                    stringBuilder.append(field.defaultValue);
                }

                if (iterator.hasNext()) {
                    stringBuilder.append(",");
                }
            }

            if (multiplePrimaryKeys.length > 0) {
                stringBuilder.append(",");
                stringBuilder.append(" ");
                stringBuilder.append("PRIMARY KEY");
                stringBuilder.append("(");
                stringBuilder.append(TextUtils.join(",", multiplePrimaryKeys));
                stringBuilder.append(")");
            }

            if (multipleUnique.length > 0) {
                stringBuilder.append(",");
                stringBuilder.append(" ");
                stringBuilder.append("UNIQUE");
                stringBuilder.append("(");
                stringBuilder.append(TextUtils.join(",", multipleUnique));
                stringBuilder.append(")");
            }

            if (!foreignKeys.isEmpty()) {
                for (ForeignKey foreignKey : foreignKeys) {
                    stringBuilder.append(",");
                    stringBuilder.append(" ");
                    stringBuilder.append("FOREIGN KEY");
                    stringBuilder.append("(");
                    stringBuilder.append(foreignKey.columnName);
                    stringBuilder.append(")");

                    stringBuilder.append(" ");
                    stringBuilder.append("REFERENCES");
                    stringBuilder.append(foreignKey.referenceTable);
                    stringBuilder.append("(");
                    stringBuilder.append(foreignKey.referenceColumn);
                    stringBuilder.append(")");

                    if (!TextUtils.isEmpty(foreignKey.action)) {
                        stringBuilder.append(" ");
                        stringBuilder.append(foreignKey.action);
                    }
                }
            }

            stringBuilder.append(");");

            return stringBuilder.toString();
        }
    }

    public static class FieldInfo {

        private final String name;
        private final DataType dataType;
        private boolean autoincrement = false;
        private PrimaryKey primaryKey = PrimaryKey.NONE;
        private boolean unique = false;
        private boolean notNull = false;
        private String defaultValue;

        public FieldInfo(String name, DataType dataType) {
            this.name = name;
            this.dataType = dataType;
        }

        public FieldInfo setPrimaryKey(PrimaryKey primaryKey) {
            if (primaryKey == null) {
                primaryKey = PrimaryKey.NONE;
            }

            this.primaryKey = primaryKey;

            return this;
        }

        public FieldInfo setUnique(boolean unique) {
            this.unique = unique;

            return this;
        }

        public FieldInfo setNotNull(boolean notNull) {
            this.notNull = notNull;

            return this;
        }

        public FieldInfo setAutoincrement(boolean autoincrement) {
            this.autoincrement = autoincrement;

            return this;
        }

        public FieldInfo setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;

            return this;
        }

        public enum DataType {

            INTEGER("INTEGER"),
            REAL("REAL"),
            TEXT("TEXT"),
            BLOB("BLOB");

            private String value;

            DataType(String value) {
                this.value = value;
            }
        }

        public enum PrimaryKey {
            ASC, DESC, NONE
        }
    }

    public static class ForeignKey {
        private final String columnName;
        private final String referenceTable;
        private final String referenceColumn;
        private final String action;

        public ForeignKey(String columnName, String referenceTable, String referenceColumn) {
            this(columnName, referenceTable, referenceColumn, "");
        }

        public ForeignKey(String columnName, String referenceTable, String referenceColumn, String action) {
            if (TextUtils.isEmpty(columnName)) {
                throw new IllegalArgumentException("Column Name can't be empty");
            }

            if (TextUtils.isEmpty(referenceTable)) {
                throw new IllegalArgumentException("Reference Table can't be empty");
            }

            if (TextUtils.isEmpty(referenceColumn)) {
                throw new IllegalArgumentException("Reference Column can't be empty");
            }

            if (TextUtils.isEmpty(action)) {
                throw new IllegalArgumentException("Action Column can't be empty");
            }

            this.columnName = columnName;
            this.referenceTable = referenceTable;
            this.referenceColumn = referenceColumn;
            this.action = action;
        }
    }
}

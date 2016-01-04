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
import com.ls.database.model.CursorParser;
import com.ls.database.model.EntityHolder;
import com.ls.database.model.IDAO;
import com.ls.database.model.SearchCondition;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public abstract class BaseDAO<Key, Entity> implements IDAO<Key, Entity> {

    private DatabaseRegister mDatabaseRegister;

    protected BaseDAO(DatabaseRegister databaseRegister) {
        mDatabaseRegister = databaseRegister;
    }

    protected Database getDatabase() {
        String databaseName = getDatabaseName();
        Database database = mDatabaseRegister.getDatabase(databaseName);

        if (database == null) {
            throw new IllegalArgumentException("Database with name " + databaseName
                    + " is not initialized, add it via " + DatabaseRegister.class.getName());
        }

        return database;
    }

    @Override
    public long insertEntity(Entity entity) {
        return insert(entity, ConflictType.CONFLICT_NONE);
    }

    @Override
    public void insertEntities(List<Entity> entities, boolean useTransaction) {
        insert(entities, ConflictType.CONFLICT_NONE, useTransaction);
    }

    @Override
    public long insertOrReplaceEntity(Entity entity) {
        return insert(entity, ConflictType.CONFLICT_REPLACE);
    }

    @Override
    public void insertOrReplaceEntities(List<Entity> entities, boolean useTransaction) {
        insert(entities, ConflictType.CONFLICT_REPLACE, useTransaction);
    }

    @Override
    public int updateEntity(Key key, Entity entity) {
        return update(key, entity, ConflictType.CONFLICT_NONE);
    }

    @Override
    public int updateEntities(List<EntityHolder<Key, Entity>> entities, boolean useTransaction) {
        return update(entities, ConflictType.CONFLICT_NONE, useTransaction);
    }

    @Override
    public int updateOrReplaceEntity(Key key, Entity entity) {
        return update(key, entity, ConflictType.CONFLICT_REPLACE);
    }

    @Override
    public int updateOrReplaceEntities(List<EntityHolder<Key, Entity>> entities, boolean useTransaction) {
        return update(entities, ConflictType.CONFLICT_REPLACE, useTransaction);
    }

    @Override
    public int deleteEntity(Key key) {
        int rows = 0;

        SearchCondition searchCondition = getSearchCondition(key);

        Database database = getDatabase();
        try {
            database.open();
            rows = database.delete(
                    getTableName(),
                    searchCondition.getWhereClause(),
                    searchCondition.getWhereArgs()
            );
        } finally {
            database.close();
        }

        return rows;
    }

    @Override
    public int deleteEntities(List<Key> keys, boolean useTransaction) {
        if (keys == null || keys.isEmpty()) {
            return 0;
        }

        int rows = 0;

        Database database = getDatabase();
        try {
            database.open();

            if (useTransaction) {
                database.beginTransaction();
            }

            for (Key key : keys) {
                rows += deleteEntity(key);
            }

            if (useTransaction) {
                database.setTransactionSuccessful();
            }
        } finally {
            if (useTransaction) {
                database.endTransaction();
            }

            database.close();
        }

        return rows;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public List<Entity> selectEntities(SearchCondition condition, String orderBy) {
        if (condition == null) {
            throw new NullPointerException("Key can't be null");
        }

        Database database = getDatabase();
        Cursor cursor = null;
        try {
            database.open();

            cursor = database.query(
                    getTableName(),
                    null,
                    condition.getWhereClause(),
                    condition.getWhereArgs(),
                    null,
                    null,
                    orderBy,
                    null
            );

            List<Entity> result = parseCursor(cursor);
            return result;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public List<Entity> selectEntities(Key key) {
        if (key == null) {
            throw new NullPointerException("Key can't be null");
        }

        SearchCondition searchCondition = getSearchCondition(key);

        List<Entity> entities = selectEntities(searchCondition, getOrderBy());
        return entities;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public List<Entity> selectAllEntities() {
        List<Entity> entities = selectEntities(new SearchCondition(null, null), getOrderBy());
        return entities;
    }

    @Override
    public void clear() {
        Database database = getDatabase();
        try {
            database.open();
            database.delete(
                    getTableName(),
                    null,
                    null
            );
        } finally {
            database.close();
        }
    }

    @Override
    public boolean contains(Key key) {
        if (key == null) {
            throw new NullPointerException("Key can't be null");
        }

        SearchCondition searchCondition = getSearchCondition(key);

        if (searchCondition == null) {
            throw new IllegalStateException(SearchCondition.class.getName() + " can't be null");
        }

        long rowsCount = getRowCount(searchCondition);
        return rowsCount > 0;
    }

    @Override
    public long getRowCount() {
        return getRowCount(null);
    }

    @Override
    public long getRowCount(SearchCondition searchCondition) {
        long result = 0;

        Database database = getDatabase();
        Cursor cursor = null;
        try {
            database.open();

            String columnName = "count";
            String query = "SELECT count(*) AS " + columnName + " FROM " + getTableName();

            String condition = searchCondition != null ? searchCondition.getWhereClause() : null;
            if (!TextUtils.isEmpty(condition)) {
                query = query + " WHERE " + condition;
            }

            String[] arguments = searchCondition != null ? searchCondition.getWhereArgs() : null;
            cursor = database.rawQuery(query, arguments);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(columnName);
                result = cursor.getLong(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }

            database.close();
        }

        return result;
    }

    protected long insert(Entity entity, ConflictType type) {
        if (entity == null) {
            return 0;
        }

        EntityConverter<Entity> converter = getEntityConverter();
        ContentValues contentValues = converter.toContentValues(entity);

        long id = 0;

        if (contentValues.size() > 0) {
            Database database = getDatabase();
            try {
                database.open();
                id = database.insert(getTableName(), null, contentValues, type);
            } finally {
                database.close();
            }
        }

        return id;
    }

    protected void insert(List<Entity> entities, ConflictType type, boolean useTransaction) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        Database database = getDatabase();
        try {
            database.open();

            if (useTransaction) {
                database.beginTransaction();
            }

            for (Entity entity : entities) {
                insert(entity, type);
            }

            if (useTransaction) {
                database.setTransactionSuccessful();
            }
        } finally {
            if (useTransaction) {
                database.endTransaction();
            }
            database.close();
        }
    }

    protected int update(Key key, Entity entity, ConflictType type) {
        if (entity == null || key == null) {
            return 0;
        }

        int rows = 0;

        SearchCondition searchCondition = getSearchCondition(key);

        EntityConverter<Entity> converter = getEntityConverter();
        ContentValues contentValues = converter.toContentValues(entity);

        if (contentValues.size() > 0) {
            Database database = getDatabase();
            try {
                database.open();
                rows = database.update(
                        getTableName(),
                        contentValues,
                        searchCondition.getWhereClause(),
                        searchCondition.getWhereArgs(),
                        type
                );
            } finally {
                database.close();
            }
        }

        return rows;
    }

    protected int update(List<EntityHolder<Key, Entity>> entities, ConflictType type, boolean useTransaction) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }

        int rows = 0;

        Database database = getDatabase();
        try {
            database.open();

            if (useTransaction) {
                database.beginTransaction();
            }

            for (EntityHolder<Key, Entity> holder : entities) {
                rows += update(holder.getKey(), holder.getEntity(), type);
            }

            if (useTransaction) {
                database.setTransactionSuccessful();
            }
        } finally {
            if (useTransaction) {
                database.endTransaction();
            }

            database.close();
        }

        return rows;
    }

    private List<Entity> parseCursor(Cursor cursor) {
        List<Entity> result = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            CursorParser cursorParser = new CursorParser(cursor);
            do {
                EntityConverter<Entity> converter = getEntityConverter();
                Entity entityNew = converter.toEntity(cursorParser);

                result.add(entityNew);
            } while (cursor.moveToNext());

            cursorParser.releaseResources();
        }

        return result;
    }

    protected abstract String getDatabaseName();

    protected abstract String getTableName();

    protected abstract SearchCondition getSearchCondition(Key key);

    protected abstract String getOrderBy();

    protected abstract EntityConverter<Entity> getEntityConverter();

}

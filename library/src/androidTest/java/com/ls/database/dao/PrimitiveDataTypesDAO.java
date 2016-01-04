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
package com.ls.database.dao;

import com.ls.database.BaseDAO;
import com.ls.database.DatabaseRegister;
import com.ls.database.EntityConverter;
import com.ls.database.Tables;
import com.ls.database.entity.EnumValue;
import com.ls.database.entity.PrimitiveDataTypesEntity;
import com.ls.database.model.SearchCondition;
import com.ls.database.model.CursorParser;

import android.content.ContentValues;
import android.text.TextUtils;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class PrimitiveDataTypesDAO extends BaseDAO<Long, PrimitiveDataTypesEntity> implements EntityConverter<PrimitiveDataTypesEntity> {

    public PrimitiveDataTypesDAO(DatabaseRegister databaseRegister) {
        super(databaseRegister);
    }

    @Override
    protected String getDatabaseName() {
        return DAOTestingHelper.DB_NAME;
    }

    @Override
    protected String getTableName() {
        return Tables.DataTypes.NAME;
    }

    @Override
    protected SearchCondition getSearchCondition(Long key) {
        return new SearchCondition(
                Tables.DataTypes.COLUMN_ID + "=?",
                new String[]{String.valueOf(key)}
        );
    }

    @Override
    public ContentValues toContentValues(PrimitiveDataTypesEntity entity) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Tables.DataTypes.COLUMN_ID, entity.getId());
        contentValues.put(Tables.DataTypes.COLUMN_STRING, entity.getString());
        contentValues.put(Tables.DataTypes.COLUMN_BYTE, entity.getByte());
        contentValues.put(Tables.DataTypes.COLUMN_SHORT, entity.getShort());
        contentValues.put(Tables.DataTypes.COLUMN_INTEGER, entity.getInt());
        contentValues.put(Tables.DataTypes.COLUMN_LONG, entity.getLong());
        contentValues.put(Tables.DataTypes.COLUMN_FLOAT, entity.getFloat());
        contentValues.put(Tables.DataTypes.COLUMN_DOUBLE, entity.getDouble());
        contentValues.put(Tables.DataTypes.COLUMN_BOOLEAN, entity.isBoolean() ? 1 : 0);
        contentValues.put(Tables.DataTypes.COLUMN_BLOB, entity.getBytes());
        contentValues.put(Tables.DataTypes.COLUMN_ENUM, entity.getEnumValue() != null ? entity.getEnumValue().name() : null);

        return contentValues;
    }

    @Override
    public PrimitiveDataTypesEntity toEntity(CursorParser parser) {
        PrimitiveDataTypesEntity entity = new PrimitiveDataTypesEntity();

        entity.setId(parser.readLong(Tables.DataTypes.COLUMN_ID));
        entity.setString(parser.readString(Tables.DataTypes.COLUMN_STRING));
        entity.setByte(parser.readByte(Tables.DataTypes.COLUMN_BYTE));
        entity.setShort(parser.readShort(Tables.DataTypes.COLUMN_SHORT));
        entity.setInt(parser.readInteger(Tables.DataTypes.COLUMN_INTEGER));
        entity.setLong(parser.readLong(Tables.DataTypes.COLUMN_LONG));
        entity.setFloat(parser.readFloat(Tables.DataTypes.COLUMN_FLOAT));
        entity.setDouble(parser.readDouble(Tables.DataTypes.COLUMN_DOUBLE));
        entity.setBoolean(parser.readInteger(Tables.DataTypes.COLUMN_BOOLEAN) == 1);
        entity.setBytes(parser.readBlob(Tables.DataTypes.COLUMN_BLOB));

        String enumName = parser.readString(Tables.DataTypes.COLUMN_ENUM);
        entity.setEnumValue(!TextUtils.isEmpty(enumName) ? EnumValue.valueOf(enumName) : null);

        return entity;
    }

    @Override
    protected String getOrderBy() {
        return Tables.DataTypes.COLUMN_ID;
    }

    @Override
    protected EntityConverter<PrimitiveDataTypesEntity> getEntityConverter() {
        return this;
    }
}

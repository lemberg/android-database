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

import com.ls.database.BaseSQLiteOpenHelper;
import com.ls.database.entity.DataTypesEntity;
import com.ls.database.entity.EnumValue;
import com.ls.database.model.SearchCondition;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class DataTypesTest extends AbsDaoTest<Long, DataTypesEntity, DataTypesDAO> {

    @Override
    protected BaseSQLiteOpenHelper getDatabase() {
        return new DAOTestingHelper(getInstrumentation().getTargetContext());
    }

    @Override
    protected DataTypesDAO getDao() {
        return new DataTypesDAO(getDatabaseRegister());
    }

    @Override
    protected List<DataTypesEntity> generateEntities() {
        List<DataTypesEntity> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            DataTypesEntity entity = new DataTypesEntity();
            entity.setId((i + 1));
            entity.setString("Test " + (i + 2));
            entity.setByte((byte) (i + 3));
            entity.setShort((short) (i + 4));
            entity.setInt((i + 5));
            entity.setLong((i + 6));
            entity.setFloat((i + 7.6f));
            entity.setDouble((i + 8.7d));
            entity.setBoolean(true);
            entity.setBytes("test bytes".getBytes());
            entity.setEnumValue(EnumValue.TEST_2);

            result.add(entity);
        }

        return result;
    }

    @Override
    protected List<DataTypesEntity> onUpdateEntities(List<DataTypesEntity> list) {
        int counter = 1;
        for (com.ls.database.entity.DataTypesEntity entity : list) {
            entity.setString("Test " + entity.getId());
            entity.setByte((byte) (counter + entity.getByte()));
            entity.setShort((short) (counter + entity.getShort()));
            entity.setInt((counter + entity.getInt()));
            entity.setLong((counter + entity.getLong()));
            entity.setFloat((counter + entity.getFloat()));
            entity.setDouble((counter + entity.getDouble()));
            entity.setBoolean(true);
            try {
                entity.setBytes((new String(entity.getBytes(), "UTF-8") + "test2 bytes2").getBytes());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            entity.setEnumValue(com.ls.database.entity.EnumValue.TEST_1);

            counter++;
        }

        return list;
    }

    @Override
    protected Long extractKey(DataTypesEntity entity) {
        return entity.getId();
    }

    @Override
    protected SearchCondition getSearchCondition(Long key) {
        return getDao().getSearchCondition(key);
    }

    @Override
    protected String getOrderBy() {
        return getDao().getOrderBy();
    }
}

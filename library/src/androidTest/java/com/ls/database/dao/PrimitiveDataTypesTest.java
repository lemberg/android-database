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

import com.ls.database.entity.EnumValue;
import com.ls.database.entity.PrimitiveDataTypesEntity;
import com.ls.database.model.SearchCondition;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class PrimitiveDataTypesTest extends AbsDaoTest<Long, PrimitiveDataTypesEntity, PrimitiveDataTypesDAO> {

    @Override
    protected DbInfo getDatabase() {
        return new DbInfo(DAOTestingHelper.DB_NAME, new DAOTestingHelper(getInstrumentation().getTargetContext()));
    }

    @Override
    protected PrimitiveDataTypesDAO getDao() {
        return new PrimitiveDataTypesDAO(getDatabaseRegister());
    }

    @Override
    protected List<PrimitiveDataTypesEntity> generateEntities() {
        List<PrimitiveDataTypesEntity> result = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            com.ls.database.entity.PrimitiveDataTypesEntity entity = new PrimitiveDataTypesEntity();
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
            entity.setEnumValue(com.ls.database.entity.EnumValue.TEST_2);

            result.add(entity);
        }

        return result;
    }

    @Override
    protected List<com.ls.database.entity.PrimitiveDataTypesEntity> onUpdateEntities(List<PrimitiveDataTypesEntity> list) {
        int counter = 1;
        for (PrimitiveDataTypesEntity entity : list) {
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
            entity.setEnumValue(EnumValue.TEST_1);

            counter++;
        }

        return list;
    }

    @Override
    protected Long extractKey(PrimitiveDataTypesEntity entity) {
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

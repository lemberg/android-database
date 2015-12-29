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
package com.ls.databasedemo.model.db.dao;

import android.content.Context;
import android.test.InstrumentationTestCase;
import com.ls.database.DatabaseRegister;
import com.ls.databasedemo.model.db.DatabaseHelper;
import com.ls.databasedemo.model.db.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class UserDaoTest extends InstrumentationTestCase {

    private DatabaseRegister databaseRegister;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = getInstrumentation().getTargetContext();
        databaseRegister = new DatabaseRegister(context);
        databaseRegister.addDatabase(new DatabaseHelper());

        new UserDAO(databaseRegister).clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new UserDAO(databaseRegister).clear();

        databaseRegister.shutdownAndClear();
        databaseRegister = null;
    }

    private List<User> createUserEntities() {
        List<User> entities = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int value = (i + 1);
            entities.add(new User(value, "test" + value, "test" + value, "test" + value));
        }

        return entities;
    }

    public void testInsertEntity() throws Exception {
        User expectedUser = new User(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedUser);

        List<User> actualEntities = userDao.selectAllEntities();
        User actualUser = actualEntities.get(0);

        assertEquals("Entities are not equal", expectedUser, actualUser);
    }

    public void testUpdateEntity() {
        User expectedUser = new User(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedUser);

        User updatedUser = new User(1, "test_2", "test_2", "test_2");

        userDao.updateEntity(updatedUser.getId(), updatedUser);

        List<User> actualEntities = userDao.selectAllEntities();
        User actualUser = actualEntities.get(0);

        assertEquals("Entities are not equal", updatedUser, actualUser);
    }

    public void testDeleteEntity() throws Exception {
        User expectedUser = new User(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedUser);

        int deletedRows = userDao.deleteEntity(expectedUser.getId());
        List<User> actualEntities = userDao.selectAllEntities();

        assertTrue("Test entity is not deleted", !actualEntities.contains(expectedUser) && deletedRows == 1);
    }

    public void testClear() throws Exception {
        List<User> entities = createUserEntities();
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntities(entities, true);
        userDao.clear();

        List<User> actualEntities = userDao.selectAllEntities();

        assertTrue("Table should be empty", actualEntities.size() == 0);
    }

    public void testSelectEntities() throws Exception {
        List<User> entities = createUserEntities();
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntities(entities, true);

        List<User> actualEntities = userDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }
}

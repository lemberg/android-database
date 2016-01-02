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

import com.ls.database.DatabaseRegister;
import com.ls.databasedemo.model.db.DatabaseHelper;
import com.ls.databasedemo.model.db.entity.Contact;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class ContactDaoTest extends InstrumentationTestCase {

    private DatabaseRegister databaseRegister;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = getInstrumentation().getTargetContext();
        databaseRegister = new DatabaseRegister(context);
        databaseRegister.addDatabase(DatabaseHelper.DB_NAME, new DatabaseHelper(context));

        new UserDAO(databaseRegister).clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new UserDAO(databaseRegister).clear();

        databaseRegister.shutdownAndClear();
        databaseRegister = null;
    }

    private List<Contact> createUserEntities() {
        List<Contact> entities = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int value = (i + 1);
            entities.add(new Contact(value, "test" + value, "test" + value, "test" + value));
        }

        return entities;
    }

    public void testInsertEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedContact);

        List<Contact> actualEntities = userDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", expectedContact, actualContact);
    }

    public void testUpdateEntity() {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedContact);

        Contact updatedContact = new Contact(1, "test_2", "test_2", "test_2");

        userDao.updateEntity(updatedContact.getId(), updatedContact);

        List<Contact> actualEntities = userDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", updatedContact, actualContact);
    }

    public void testDeleteEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntity(expectedContact);

        int deletedRows = userDao.deleteEntity(expectedContact.getId());
        List<Contact> actualEntities = userDao.selectAllEntities();

        assertTrue("Test entity is not deleted", !actualEntities.contains(expectedContact) && deletedRows == 1);
    }

    public void testClear() throws Exception {
        List<Contact> entities = createUserEntities();
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntities(entities, true);
        userDao.clear();

        List<Contact> actualEntities = userDao.selectAllEntities();

        assertTrue("Table should be empty", actualEntities.size() == 0);
    }

    public void testSelectEntities() throws Exception {
        List<Contact> entities = createUserEntities();
        UserDAO userDao = new UserDAO(databaseRegister);

        userDao.insertEntities(entities, true);

        List<Contact> actualEntities = userDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }
}

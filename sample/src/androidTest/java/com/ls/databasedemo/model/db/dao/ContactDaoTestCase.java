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
import com.ls.database.model.EntityHolder;
import com.ls.database.model.SearchCondition;
import com.ls.databasedemo.model.db.DatabaseHelper;
import com.ls.databasedemo.model.db.entity.Contact;

import android.content.Context;
import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class ContactDaoTestCase extends InstrumentationTestCase {

    private DatabaseRegister databaseRegister;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = getInstrumentation().getTargetContext();
        databaseRegister = new DatabaseRegister(context);
        databaseRegister.addDatabase(new DatabaseHelper(context));

        new ContactDAO(databaseRegister).clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new ContactDAO(databaseRegister).clear();

        databaseRegister.shutdownAndClear();
        databaseRegister = null;
    }

    private List<Contact> createContactEntities() {
        List<Contact> entities = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int value = (i + 1);
            entities.add(new Contact(value, "test" + value, "test" + value, "test" + value));
        }

        return entities;
    }

    public void testInsertEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntity(expectedContact);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", expectedContact, actualContact);
    }

    public void testInsertEntities() throws Exception {
        List<Contact> entities = createContactEntities();
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntities(entities, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testInsertOrReplaceEntity() throws Exception {
        Contact contact = new Contact(1, "test", "test", "test");

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertOrReplaceEntity(contact);

        Contact expectedContact = new Contact(1, "test1", "test1", "test1");
        contactDao.insertOrReplaceEntity(expectedContact);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", expectedContact, actualContact);
    }

    public void testInsertOrReplaceEntities() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertOrReplaceEntities(entities, true);

        //add same entities, system must not crash
        contactDao.insertOrReplaceEntities(entities, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testUpdateEntity() {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntity(expectedContact);

        Contact updatedContact = new Contact(1, "test_2", "test_2", "test_2");

        contactDao.updateEntity(updatedContact.getId(), updatedContact);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", updatedContact, actualContact);
    }

    public void testUpdateEntities() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        //add one item
        contactDao.insertEntities(entities, true);

        List<EntityHolder<Long, Contact>> entityHolders = new ArrayList<>();
        for (Contact contact : entities) {
            contact.setFirstName("new test");
            contact.setLastName("new test");
            contact.setEmail("new test");
            entityHolders.add(new EntityHolder<>(contact.getId(), contact));
        }

        contactDao.updateEntities(entityHolders, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testUpdateOrReplaceEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertEntity(expectedContact);

        expectedContact.setFirstName("new test");
        expectedContact.setLastName("new test");
        expectedContact.setEmail("new test");

        contactDao.updateOrReplaceEntity(expectedContact.getId(), expectedContact);
        //do same operation, system must not crash
        contactDao.updateOrReplaceEntity(expectedContact.getId(), expectedContact);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        Contact actualEntity = actualEntities.get(0);
        assertEquals("Entities are not equal", expectedContact, actualEntity);
    }

    public void testUpdateOrReplaceEntities() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        //add one item
        contactDao.insertEntities(entities, true);

        List<EntityHolder<Long, Contact>> entityHolders = new ArrayList<>();
        for (Contact contact : entities) {
            contact.setFirstName("new test");
            contact.setLastName("new test");
            contact.setEmail("new test");
            entityHolders.add(new EntityHolder<>(contact.getId(), contact));
        }

        contactDao.updateOrReplaceEntities(entityHolders, true);

        //do same operation, system must not crash
        contactDao.updateOrReplaceEntities(entityHolders, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testDeleteEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntity(expectedContact);

        int deletedRows = contactDao.deleteEntity(expectedContact.getId());
        List<Contact> actualEntities = contactDao.selectAllEntities();

        assertTrue("Test entity is not deleted", !actualEntities.contains(expectedContact) && deletedRows == 1);
    }

    public void testDeleteEntities() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        //add items
        contactDao.insertEntities(entities, true);
        //delete items

        List<Long> keys = new ArrayList<>();
        for (Contact entity : entities) {
            keys.add(entity.getId());
        }
        contactDao.deleteEntities(keys, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertTrue("Table should be empty", actualEntities.isEmpty());
    }

    public void testClear() throws Exception {
        List<Contact> entities = createContactEntities();
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntities(entities, true);
        contactDao.clear();

        List<Contact> actualEntities = contactDao.selectAllEntities();

        assertTrue("Table should be empty", actualEntities.size() == 0);
    }

    public void testSelectEntities_all() throws Exception {
        List<Contact> entities = createContactEntities();
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntities(entities, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testSelectEntities_bySearchCondition() {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        //add items
        contactDao.insertEntities(entities, true);

        Contact entity = contactDao.selectAllEntities().get(0);

        List<Contact> selectedEntities = contactDao.selectEntities(
                contactDao.getSearchCondition(entity.getId()),
                contactDao.getOrderBy()
        );

        List<Contact> allEntities = contactDao.selectAllEntities();

        assertTrue("Test entities are not present in database", allEntities.containsAll(selectedEntities));
    }

    public void testSelectEntities_key() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertEntities(entities, true);

        Contact entity = contactDao.selectAllEntities().get(0);

        List<Contact> selectedEntities = contactDao.selectEntities(entity.getId());
        List<Contact> allEntities = contactDao.selectAllEntities();

        assertTrue("Test entities are not present in database", allEntities.containsAll(selectedEntities));
    }

    public void testContains() throws Exception {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertEntities(entities, true);

        Contact actualEntity = entities.get(0);
        assertTrue("Test entity is not present in database", contactDao.contains(actualEntity.getId()));
    }

    public void testRowsCount_all() {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertEntities(entities, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        long actualRowsCount = contactDao.getRowCount();
        assertTrue("Wrong count", actualRowsCount == actualEntities.size());
    }

    public void testRowsCount_bySearchCondition() {
        List<Contact> entities = createContactEntities();

        ContactDAO contactDao = new ContactDAO(databaseRegister);
        contactDao.insertEntities(entities, true);

        List<Contact> all = contactDao.selectAllEntities();
        Contact entity = all.get(0);
        Long key = entity.getId();
        SearchCondition searchCondition = contactDao.getSearchCondition(key);

        long actualRowsCount = contactDao.getRowCount(searchCondition);
        List<Contact> actualEntities = contactDao.selectEntities(key);
        assertTrue("Wrong count", actualRowsCount == actualEntities.size());
    }
}

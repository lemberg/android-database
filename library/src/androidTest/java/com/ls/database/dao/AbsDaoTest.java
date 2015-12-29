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

import com.ls.database.DatabaseRegister;
import com.ls.database.model.EntityHolder;
import com.ls.database.model.IDAO;
import com.ls.database.model.IDBHelper;
import com.ls.database.model.SearchCondition;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public abstract class AbsDaoTest<Key, Entity, DAO extends IDAO<Key, Entity>> extends InstrumentationTestCase {

    private DatabaseRegister mDatabaseRegister;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mDatabaseRegister = new DatabaseRegister(getInstrumentation().getTargetContext());
        //read table info using testing instrumentation context
        mDatabaseRegister.addDatabase(getDatabase());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        getDao().clear();

        mDatabaseRegister.shutdownAndClear();
        mDatabaseRegister = null;
    }

    protected DatabaseRegister getDatabaseRegister() {
        return mDatabaseRegister;
    }

    protected abstract IDBHelper getDatabase();

    protected abstract DAO getDao();

    /**
     * @return list of entities to test, Entity class should override {@link #equals(Object)} and {@link #hashCode()}.
     */
    protected abstract List<Entity> generateEntities();

    protected abstract List<Entity> onUpdateEntities(List<Entity> entities);

    protected abstract Key extractKey(Entity entity);

    /**
     * Used for {@link #testSelectEntitiesBySearchCondition()}.
     */
    protected abstract SearchCondition getSearchCondition(Key key);

    /**
     * Used for {@link #testSelectEntitiesBySearchCondition()}.
     */
    protected abstract String getOrderBy();

    private List<Entity> getEntities() {
        List<Entity> entities = generateEntities();

        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("List of generated entities is empty");
        }

        return entities;
    }

    private List<Entity> updateEntities(List<Entity> original, List<Entity> updateList) {
        List<Entity> updatedEntities = onUpdateEntities(updateList);

        if (updatedEntities == null || updatedEntities.isEmpty()) {
            throw new IllegalStateException("List of updated entities is empty");
        }

        if (updatedEntities.equals(original)) {
            throw new IllegalStateException("List of entities is not updated");
        }

        return updatedEntities;
    }

    public void testInsertEntity() throws Exception {
        List<Entity> entities = getEntities();
        Entity entity = entities.get(0);

        //clear list and add item that was added into database
        entities.clear();
        entities.add(entity);

        DAO dao = getDao();
        //add one item
        dao.insertEntity(entity);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testInsertEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testInsertOrReplaceEntity() throws Exception {
        List<Entity> entities = getEntities();
        Entity entity = entities.get(0);

        DAO dao = getDao();
        //add one item
        dao.insertOrReplaceEntity(entity);

        //clear list and add item that was added into database
        entities.clear();
        entities.add(entity);

        //add same entity, system must not crash
        dao.insertOrReplaceEntity(entity);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testInsertOrReplaceEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertOrReplaceEntities(entities, true);

        //add same entities, system must not crash
        dao.insertOrReplaceEntities(entities, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testUpdateEntity() {
        List<Entity> entities = getEntities();
        Entity entity = entities.get(0);

        List<Entity> entitiesToUpdate = getEntities();
        Entity entityToUpdate = entitiesToUpdate.get(0);

        //clear list and add item that was added into database
        entities.clear();
        entities.add(entity);

        DAO dao = getDao();
        //add one item
        dao.insertEntity(entity);

        entitiesToUpdate.clear();
        entitiesToUpdate.add(entityToUpdate);

        List<Entity> updatedEntities = updateEntities(entities, entitiesToUpdate);

        Entity updatedEntity = updatedEntities.get(0);
        dao.updateEntity(extractKey(updatedEntity), updatedEntity);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", updatedEntities, actualEntities);
    }

    public void testUpdateEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add one item
        dao.insertEntities(entities, true);

        List<Entity> updatedEntities = updateEntities(entities, getEntities());

        List<EntityHolder<Key, Entity>> entityHolders = new ArrayList<>();
        for (Entity updatedEntity : updatedEntities) {
            entityHolders.add(new EntityHolder<>(extractKey(updatedEntity), updatedEntity));
        }

        dao.updateEntities(entityHolders, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", updatedEntities, actualEntities);
    }

    public void testUpdateOrReplaceEntity() throws Exception {
        List<Entity> entities = getEntities();
        Entity entity = entities.get(0);

        List<Entity> entitiesToUpdate = getEntities();
        Entity entityToUpdate = entitiesToUpdate.get(0);

        //clear list and add item that was added into database
        entities.clear();
        entities.add(entity);

        DAO dao = getDao();
        //add one item
        dao.insertEntity(entity);

        entitiesToUpdate.clear();
        entitiesToUpdate.add(entityToUpdate);

        List<Entity> updatedEntities = updateEntities(entities, entitiesToUpdate);

        Entity updatedEntity = updatedEntities.get(0);

        dao.updateOrReplaceEntity(extractKey(updatedEntity), updatedEntity);

        //do same operation, system must not crash
        dao.updateOrReplaceEntity(extractKey(updatedEntity), updatedEntity);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", updatedEntities, actualEntities);
    }

    public void testUpdateOrReplaceEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add one item
        dao.insertEntities(entities, true);

        List<Entity> updatedEntities = updateEntities(entities, getEntities());

        List<EntityHolder<Key, Entity>> entityHolders = new ArrayList<>();
        for (Entity updatedEntity : updatedEntities) {
            entityHolders.add(new EntityHolder<>(extractKey(updatedEntity), updatedEntity));
        }

        dao.updateOrReplaceEntities(entityHolders, true);

        //do same operation, system must not crash
        dao.updateOrReplaceEntities(entityHolders, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", updatedEntities, actualEntities);
    }

    public void testDeleteEntity() throws Exception {
        List<Entity> entities = getEntities();
        Entity entity = entities.get(0);
        DAO dao = getDao();
        //add items
        dao.insertEntity(entity);

        int deletedRows = dao.deleteEntity(extractKey(entity));

        List<Entity> baseList = dao.selectAllEntities();

        assertTrue("Test entity is not deleted", !baseList.contains(entity) && deletedRows == 1);
    }

    public void testDeleteEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);
        //delete items

        List<Key> keys = new ArrayList<>();
        for (Entity entity : entities) {
            keys.add(extractKey(entity));
        }
        dao.deleteEntities(keys, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertTrue("Table should be empty", actualEntities.isEmpty());
    }

    public void testSelectEntitiesBySearchCondition() {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        Entity entity = dao.selectAllEntities().get(0);

        List<Entity> selectedEntities = dao.selectEntities(getSearchCondition(extractKey(entity)), getOrderBy());

        List<Entity> allEntities = dao.selectAllEntities();

        assertTrue("Test entities are not present in database", allEntities.containsAll(selectedEntities));
    }

    public void testSelectKeyEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        Entity entity = dao.selectAllEntities().get(0);

        List<Entity> selectedEntities = dao.selectEntities(extractKey(entity));

        List<Entity> allEntities = dao.selectAllEntities();

        assertTrue("Test entities are not present in database", allEntities.containsAll(selectedEntities));
    }

    public void testSelectAllEntities() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }

    public void testContains() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        Entity actualEntity = entities.get(0);

        assertTrue("Test entity is not present in database", dao.contains(extractKey(actualEntity)));
    }

    public void testClear() throws Exception {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        dao.clear();

        assertTrue("Table should be empty", dao.selectAllEntities().size() == 0);
    }

    public void testRowsCountAll() {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        List<Entity> actualEntities = dao.selectAllEntities();
        long actualRowsCount = dao.getRowCount();
        assertTrue("Wrong count", actualRowsCount == actualEntities.size());
    }

    public void testRowsCountBySearchCondition() {
        List<Entity> entities = getEntities();

        DAO dao = getDao();
        //add items
        dao.insertEntities(entities, true);

        List<Entity> all = dao.selectAllEntities();
        Entity entity = all.get(0);
        Key key = extractKey(entity);
        SearchCondition searchCondition = getSearchCondition(key);

        long actualRowsCount = dao.getRowCount(searchCondition);
        List<Entity> actualEntities = dao.selectEntities(key);
        assertTrue("Wrong count", actualRowsCount == actualEntities.size());
    }
}

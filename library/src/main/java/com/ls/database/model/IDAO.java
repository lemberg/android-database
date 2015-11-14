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
package com.ls.database.model;

import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public interface IDAO<Key, Entity> {

    long insertEntity(Entity entity);

    void insertEntities(List<Entity> entities);

    long insertOrReplaceEntity(Entity entity);

    void insertOrReplaceEntities(List<Entity> entities);

    int updateEntity(Key key, Entity entity);

    int updateEntities(List<EntityHolder<Key, Entity>> entities);

    int updateOrReplaceEntity(Key key, Entity entity);

    int updateOrReplaceEntities(List<EntityHolder<Key, Entity>> entities);

    int deleteEntity(Key key);

    int deleteEntities(List<Key> keys);

    List<Entity> selectEntities(SearchCondition condition, String orderBy);

    List<Entity> selectEntities(Key key);

    List<Entity> selectAllEntities();

    void clear();

    boolean contains(Key key);

    long getRowCount();

    long getRowCount(SearchCondition searchCondition);
}

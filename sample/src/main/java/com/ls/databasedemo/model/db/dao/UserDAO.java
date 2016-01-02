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

import com.ls.database.BaseDAO;
import com.ls.database.DatabaseRegister;
import com.ls.database.model.CursorParser;
import com.ls.database.model.SearchCondition;
import com.ls.databasedemo.model.db.DatabaseHelper;
import com.ls.databasedemo.model.db.Tables;
import com.ls.databasedemo.model.db.entity.Contact;

import android.content.ContentValues;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class UserDAO extends BaseDAO<Long, Contact> {

    public UserDAO(DatabaseRegister databaseRegister) {
        super(databaseRegister);
    }

    @Override
    protected String getDatabaseName() {
        return DatabaseHelper.DB_NAME;
    }

    @Override
    protected String getTableName() {
        return Tables.Contacts.NAME;
    }

    @Override
    protected SearchCondition getSearchCondition(Long key) {
        return new SearchCondition(
                Tables.Contacts.COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(key)
                }
        );
    }

    @Override
    protected String getOrderBy() {
        return Tables.Contacts.COLUMN_ID;
    }

    @Override
    protected ContentValues toContentValues(Contact contact) {
        ContentValues contentValues = new ContentValues();

        //for case when user got entity from database where id is autoincrement column
        if (contact.getId() > 0) {
            contentValues.put(Tables.Contacts.COLUMN_ID, contact.getId());
        }

        contentValues.put(Tables.Contacts.COLUMN_FIRST_NAME, contact.getFirstName());
        contentValues.put(Tables.Contacts.COLUMN_LAST_NAME, contact.getLastName());
        contentValues.put(Tables.Contacts.COLUMN_EMAIL, contact.getEmail());

        return contentValues;
    }

    @Override
    protected Contact toEntity(CursorParser parser) {
        Contact contact = new Contact();

        contact.setId(parser.readLong(Tables.Contacts.COLUMN_ID));
        contact.setFirstName(parser.readString(Tables.Contacts.COLUMN_FIRST_NAME));
        contact.setLastName(parser.readString(Tables.Contacts.COLUMN_LAST_NAME));
        contact.setEmail(parser.readString(Tables.Contacts.COLUMN_EMAIL));

        return contact;
    }
}

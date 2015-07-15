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
package com.ls.databasedemo.model.db;

import com.ls.database.DatabaseRegister;
import com.ls.database.model.IDBHelper;
import com.ls.databasedemo.model.db.dao.ContactDAO;
import com.ls.databasedemo.model.db.entity.Contact;

import android.content.Context;

import java.util.List;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class DatabaseManager {

    private static DatabaseManager INSTANCE;

    public synchronized static void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager(context);
        }
    }

    public synchronized static DatabaseManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Manager is not initialized. Please call init(...) to initialize manager.");
        }

        return INSTANCE;
    }

    private Context mContext;
    private DatabaseRegister mDatabaseRegister;
    private ContactDAO mContactDAO;

    private DatabaseManager(Context context) {
        mContext = context.getApplicationContext();

        mDatabaseRegister = new DatabaseRegister(mContext);

        mContactDAO = new ContactDAO(mDatabaseRegister);
    }

    public void registerDatabase(IDBHelper helper) {
        mDatabaseRegister.addDatabase(helper);
    }

    public  void addContacts(List<Contact> contacts) {
        mContactDAO.insertOrReplaceEntities(contacts);
    }

    public List<Contact> loadContacts() {
        List<Contact> contacts = mContactDAO.selectAllEntities();
        return contacts;
    }

    public void clearContacts() {
        mContactDAO.clear();
    }
}

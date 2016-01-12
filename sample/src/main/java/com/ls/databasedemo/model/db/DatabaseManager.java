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

import android.content.Context;
import com.ls.database.BaseSQLiteOpenHelper;
import com.ls.database.DatabaseRegister;
import com.ls.databasedemo.model.db.dao.ContactDAO;
import com.ls.databasedemo.model.db.entity.Contact;

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

    private DatabaseRegister databaseRegister;
    private ContactDAO contactDAO;

    private DatabaseManager(Context context) {
        Context appContext = context.getApplicationContext();

        databaseRegister = new DatabaseRegister(appContext);

        contactDAO = new ContactDAO(databaseRegister);
    }

    public void registerDatabase(BaseSQLiteOpenHelper sqLiteOpenHelper) {
        databaseRegister.addDatabase(sqLiteOpenHelper);
    }

    public  void addContacts(List<Contact> contacts) {
        contactDAO.insertOrReplaceEntities(contacts, true);
    }

    public List<Contact> loadContacts() {
        List<Contact> contacts = contactDAO.selectAllEntities();
        return contacts;
    }

    public void clearContacts() {
        contactDAO.clear();
    }
}

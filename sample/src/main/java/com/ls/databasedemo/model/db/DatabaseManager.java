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
import com.ls.databasedemo.model.db.dao.UserDAO;
import com.ls.databasedemo.model.db.entity.Contact;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

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
    private UserDAO userDAO;

    private DatabaseManager(Context context) {
        Context appContext = context.getApplicationContext();

        databaseRegister = new DatabaseRegister(appContext);

        userDAO = new UserDAO(databaseRegister);
    }

    public void registerDatabase(String databaseName, SQLiteOpenHelper sqLiteOpenHelper) {
        databaseRegister.addDatabase(databaseName, sqLiteOpenHelper);
    }

    public  void addContacts(List<Contact> contacts) {
        userDAO.insertOrReplaceEntities(contacts, true);
    }

    public List<Contact> loadContacts() {
        List<Contact> contacts = userDAO.selectAllEntities();
        return contacts;
    }

    public void clearContacts() {
        userDAO.clear();
    }
}

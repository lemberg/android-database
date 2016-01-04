**SQLite database sample**

This sample will show you an easy way to:

 1. create and register one or several databases;
 2. manage operations with database and its tables;
 3. handle database migration tasks;
 4. save/load data in one line.

**Implementation guide**

To implement the following database structure in your app, you need pay your attention on 3 main classes: `MigratableSQLiteOpenHelper` or `SQLiteOpenHelper`, `DatabaseRegister`, `BaseDAO`.


 1. Make your `DatabaseHelper` extends `MigratableSQLiteOpenHelper` or `SQLiteOpenHelper` and override all the methods to configure your database.
 `MigratableSQLiteOpenHelper` helps to manage data migration while database upgrade or downgrade.

 2. In your `DatabaseManager` create a global instance of `DatabaseRegister
    class` and `registerDatabase(String databaseName, SQLiteOpenHelper sqLiteOpenHelper)` method.

 3. In Application class you will now be able to init your
    `DatabaseManager` and `registerDatabase(DatabaseHelper.DB_NAME, new DatabaseHelper())`

 4. The next step is implementation of functionality to access data of
    database. To make it work just `extend BaseDAO<Key, Entity>` which
    adds ability to access data of database table. Using DAO object you can save, update, delete, select data of database table. Feel free, you can also add additional functionality into DAO.

**Usage guide**

Using this database model, makes your calls to database easy and simple, here how it would looks like:

```java
    private void saveContacts() {
        List<Contact> contacts = generateContactList();
        DatabaseManager.getInstance().addContacts(contacts);
    }

    private  List<Contact> loadContacts() {
        return DatabaseManager.getInstance().loadContacts();
    }
```

**More Info**

If you want to see full code, check out sample app.

**License**
```
The MIT License (MIT)

Copyright (c) 2015 Lemberg Solutions

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

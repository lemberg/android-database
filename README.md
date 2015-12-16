**SQLite database sample**

This sample will show you an easy way to:

 1. create and register one or several databases;
 2. manage operations with database and its tables;
 3. handle database migration tasks;
 4. save/load data in one line.

**Implementation guide**

To implement the following database structure in your app, you need pay your attention on 3 main classes: `IDBHelper`, `DatabaseRegister`, `BaseDAO`.


 1. Make your `DatabaseHelper` implements `IDBHelper` and override all the methods to configure your database.

 2. In your `DatabaseManager` create a global instance of `DatabaseRegister
    class` and `registerDatabase(IDBHelper helper)` method.

 3. In Application class you will now be able to init your
    `DatabaseManager` and `registerDatabase(new DatabaseHelper())`

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

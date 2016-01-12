
Basically unit tests for database and service layers require instance of Application Context. In this case developer should use AndroidTestCase or InstrumentationTestCase. All test case classes of Android SDK extend `TestCase` class from **JUnit** framework that gives ability easy to implement tests.

###Database

Next examples contain tests check database functionality.
While testing database tables each test method should clear table before and after its execution. For this `TestCase` classes have methods `setUp` and `tearDown` that will be called before and after test method executing. These methods can be overridden and supplemented by required functionality.


```java

public class ContactDaoTest extends InstrumentationTestCase {

    private DatabaseRegister databaseRegister;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Context context = getInstrumentation().getTargetContext();
        databaseRegister = new DatabaseRegister(context);
        databaseRegister.addDatabase(DatabaseHelper.DB_NAME, new DatabaseHelper(context));

        new ContactDAO(databaseRegister).clear();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        new ContactDAO(databaseRegister).clear();

        databaseRegister.shutdownAndClear();
        databaseRegister = null;
    }
}

```

The easiest way to check that two entities are equal is to use `assertEquals(...)` method. This method requires correctly overridden methods `equals` and `hashCode` of entity class.

Check that entity was saved correctly:

```java

    public void testInsertEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntity(expectedContact);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        Contact actualContact = actualEntities.get(0);

        assertEquals("Entities are not equal", expectedContact, actualContact);
    }
    
```

Check that entity was updated correctly:

```java

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
    
```

Check that entity was deleted correctly:

```java

    public void testDeleteEntity() throws Exception {
        Contact expectedContact = new Contact(1, "test", "test", "test");
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntity(expectedContact);

        int deletedRows = contactDao.deleteEntity(expectedContact.getId());
        List<Contact> actualEntities = contactDao.selectAllEntities();

        assertTrue("Test entity is not deleted", !actualEntities.contains(expectedContact) && deletedRows == 1);
    }
    
```

Check that table was cleared correctly:

```java

    public void testClear() throws Exception {
        List<Contact> entities = createContactEntities();
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntities(entities, true);
        contactDao.clear();

        List<Contact> actualEntities = contactDao.selectAllEntities();

        assertTrue("Table should be empty", actualEntities.size() == 0);
    }
    
```

Check data selection:

```java

    public void testSelectEntities_all() throws Exception {
        List<Contact> entities = createContactEntities();
        ContactDAO contactDao = new ContactDAO(databaseRegister);

        contactDao.insertEntities(entities, true);

        List<Contact> actualEntities = contactDao.selectAllEntities();
        assertEquals("Entities are not equal", entities, actualEntities);
    }
    
```

[Implementation you can find here](https://github.com/lemberg/android-database/blob/master/sample/src/androidTest/java/com/ls/databasedemo/model/db/dao/ContactDaoTestCase.java)

###Parcelable

Next example contains test to check serialization and deserialization of Parcelable class.

```java

public class ContactParcelableTestCase extends TestCase {

   public void testParcelable_filled() {
       Contact expected = new Contact(1, "test", "test", "test");

       Parcel parcel = Parcel.obtain();
       expected.writeToParcel(parcel, 0);
       parcel.setDataPosition(0);

       Contact actual = Contact.CREATOR.createFromParcel(parcel);

       assertEquals(expected, actual);
   }

   public void testParcelable_empty() {
       Contact expected = new Contact(0, null, null, null);

       Parcel parcel = Parcel.obtain();
       expected.writeToParcel(parcel, 0);
       parcel.setDataPosition(0);

       Contact actual = Contact.CREATOR.createFromParcel(parcel);

       assertEquals(expected, actual);
   }
}
```

[Implementation you can find here](https://github.com/lemberg/android-database/blob/master/sample/src/androidTest/java/com/ls/databasedemo/model/db/entity/ContactParcelableTestCase.java)

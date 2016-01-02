package com.ls.databasedemo.model.db.dao;

import com.ls.databasedemo.model.db.entity.Contact;

import junit.framework.TestCase;

import android.os.Parcel;

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

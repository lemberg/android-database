package com.ls.databasedemo.model.db.dao;

import android.os.Parcel;
import com.ls.databasedemo.model.db.entity.User;
import junit.framework.TestCase;

public class UserParcelableTestCase extends TestCase {

   public void testParcelable_filled() {
       User expected = new User(1, "test", "test", "test");

       Parcel parcel = Parcel.obtain();
       expected.writeToParcel(parcel, 0);
       parcel.setDataPosition(0);

       User actual = User.CREATOR.createFromParcel(parcel);

       assertEquals(expected, actual);
   }

   public void testParcelable_empty() {
       User expected = new User(0, null, null, null);

       Parcel parcel = Parcel.obtain();
       expected.writeToParcel(parcel, 0);
       parcel.setDataPosition(0);

       User actual = User.CREATOR.createFromParcel(parcel);

       assertEquals(expected, actual);
   }
}

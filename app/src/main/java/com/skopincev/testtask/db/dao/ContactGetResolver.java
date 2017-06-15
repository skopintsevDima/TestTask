package com.skopincev.testtask.db.dao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.skopincev.testtask.db.DBContract;
import com.skopincev.testtask.db.entity.Contact;

/**
 * Created by skopi on 16.06.2017.
 */

public class ContactGetResolver extends DefaultGetResolver<Contact> {
    @NonNull
    @Override
    public Contact mapFromCursor(@NonNull Cursor cursor) {
        return new Contact(
                cursor.getInt(cursor.getColumnIndex(DBContract.ContactContract.KEY_ID)),
                cursor.getString(cursor.getColumnIndex(DBContract.ContactContract.KEY_OWNER_EMAIL)),
                cursor.getString(cursor.getColumnIndex(DBContract.ContactContract.KEY_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(DBContract.ContactContract.KEY_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(DBContract.ContactContract.KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndex(DBContract.ContactContract.KEY_PHONE_NUMBER)));
    }
}

package com.skopincev.testtask.db.dao;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;
import com.skopincev.testtask.db.DBContract;
import com.skopincev.testtask.db.entity.Contact;

/**
 * Created by skopi on 16.06.2017.
 */

public class ContactPutResolver extends DefaultPutResolver<Contact> {
    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull Contact contact) {
        return InsertQuery.builder()
                .table(DBContract.ContactContract.TABLE_NAME)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull Contact contact) {
        return UpdateQuery.builder()
                .table(DBContract.ContactContract.TABLE_NAME)
                .where(DBContract.ContactContract.KEY_ID + " = " + contact.get_id())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull Contact contact) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.ContactContract.KEY_OWNER_EMAIL, contact.getOwnerEmail());
        cv.put(DBContract.ContactContract.KEY_FIRST_NAME, contact.getFirstName());
        cv.put(DBContract.ContactContract.KEY_LAST_NAME, contact.getLastName());
        cv.put(DBContract.ContactContract.KEY_EMAIL, contact.getEmail());
        cv.put(DBContract.ContactContract.KEY_PHONE_NUMBER, contact.getPhoneNumber());

        return cv;
    }
}

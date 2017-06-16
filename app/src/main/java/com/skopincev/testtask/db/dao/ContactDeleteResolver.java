package com.skopincev.testtask.db.dao;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.skopincev.testtask.db.DBContract;
import com.skopincev.testtask.db.entity.Contact;

/**
 * Created by skopi on 16.06.2017.
 */

public class ContactDeleteResolver extends DefaultDeleteResolver<Contact> {
    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull Contact contact) {
        return DeleteQuery.builder()
                .table(DBContract.ContactContract.TABLE_NAME)
                .where(DBContract.ContactContract.KEY_ID + " = " + contact.get_id())
                .build();
    }
}

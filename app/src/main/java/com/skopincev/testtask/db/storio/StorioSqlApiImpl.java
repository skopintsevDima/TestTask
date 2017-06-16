package com.skopincev.testtask.db.storio;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;
import com.skopincev.testtask.db.DBContract;
import com.skopincev.testtask.db.DBHelper;
import com.skopincev.testtask.db.dao.ContactDeleteResolver;
import com.skopincev.testtask.db.dao.ContactGetResolver;
import com.skopincev.testtask.db.dao.ContactPutResolver;
import com.skopincev.testtask.db.entity.Contact;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by skopi on 16.06.2017.
 */

public class StorioSqlApiImpl implements StorioSqlApi {
    private final StorIOSQLite storIOSQLite;

    private final ContactPutResolver contactPutResolver;
    private final ContactGetResolver contactGetResolver;
    private final ContactDeleteResolver contactDeleteResolver;

    @Inject
    public StorioSqlApiImpl(DBHelper dbHelper) {
        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(dbHelper)
                .build();

        contactPutResolver = new ContactPutResolver();
        contactGetResolver = new ContactGetResolver();
        contactDeleteResolver = new ContactDeleteResolver();
    }

    @Override
    public void put(Contact contact) {
        storIOSQLite.put()
                .object(contact)
                .withPutResolver(contactPutResolver)
                .prepare()
                .executeAsBlocking();
    }

    @Override
    public void putContacts(List<Contact> contacts) {
        for (Contact contact: contacts){
            put(contact);
        }
    }

    @Override
    public List<Contact> getContactsByOwnerEmail(String email) {
        return storIOSQLite.get()
                .listOfObjects(Contact.class)
                .withQuery(
                        Query.builder()
                        .table(DBContract.ContactContract.TABLE_NAME)
                        .where(DBContract.ContactContract.KEY_OWNER_EMAIL + " = " + "\"" + email + "\"")
                        .build()
                )
                .withGetResolver(contactGetResolver)
                .prepare()
                .executeAsBlocking();
    }

    @Override
    public Contact getContactByEmail(String email) {
        return storIOSQLite.get()
                .object(Contact.class)
                .withQuery(Query.builder()
                        .table(DBContract.ContactContract.TABLE_NAME)
                        .where(DBContract.ContactContract.KEY_EMAIL + " = " + "\"" + email + "\"")
                        .build())
                .withGetResolver(contactGetResolver)
                .prepare()
                .executeAsBlocking();
    }

    @Override
    public void clearDB() {
        storIOSQLite.executeSQL()
                .withQuery(RawQuery.builder()
                        .query("DELETE FROM " + DBContract.ContactContract.TABLE_NAME)
                        .build())
                .prepare()
                .executeAsBlocking();
    }

    @Override
    public void remove(Contact contact) {
        storIOSQLite.delete()
                .object(contact)
                .withDeleteResolver(contactDeleteResolver)
                .prepare()
                .executeAsBlocking();
    }
}

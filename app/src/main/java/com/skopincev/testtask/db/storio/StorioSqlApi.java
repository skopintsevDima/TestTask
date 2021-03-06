package com.skopincev.testtask.db.storio;

import com.skopincev.testtask.db.entity.Contact;

import java.util.List;

/**
 * Created by skopi on 16.06.2017.
 */

public interface StorioSqlApi {
    void put(Contact contact);

    void putContacts(List<Contact> contacts);

    List<Contact> getContactsByOwnerEmail(String email);

    Contact getContact(Contact contact);

    void clearDB();

    void remove(Contact contact);
}

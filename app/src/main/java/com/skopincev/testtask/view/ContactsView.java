package com.skopincev.testtask.view;

import com.skopincev.testtask.db.entity.Contact;

import java.util.List;

/**
 * Created by skopi on 13.06.2017.
 */

public interface ContactsView {
    void onSignOut();
    void showMessage(String message);
    void onContactsLoaded(List<Contact> contacts);
    void onContactAdded(Contact contact);
    void onContactsDeleted();
}

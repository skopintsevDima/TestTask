package com.skopincev.testtask.presenter;

import com.skopincev.testtask.db.entity.Contact;
import com.skopincev.testtask.view.ContactsView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by skopi on 13.06.2017.
 */

public interface ContactsPresenter {
    void attach(ContactsView view);
    void detach();
    void signOut();
    void loadContacts(String email);
    void addContact(String email);
    void deleteContacts(List<Contact> contacts);
}

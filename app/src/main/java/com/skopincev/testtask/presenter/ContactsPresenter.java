package com.skopincev.testtask.presenter;

import com.skopincev.testtask.db.entity.Contact;
import com.skopincev.testtask.view.ContactsView;

import java.util.List;

/**
 * Created by skopi on 13.06.2017.
 */

public interface ContactsPresenter {
    void attach(ContactsView view);
    void detach();
    void signOut();
    void loadContacts(String email);
    void addContact(Contact contact);
    void deleteContacts(List<Contact> contacts);
    boolean isEmailValid(String email);
    boolean isPhoneNumberValid(String phoneNumber);
}

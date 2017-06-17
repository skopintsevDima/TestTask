package com.skopincev.testtask.presenter;

import android.content.SharedPreferences;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.skopincev.testtask.db.entity.Contact;
import com.skopincev.testtask.db.storio.StorioSqlApi;
import com.skopincev.testtask.view.ContactsView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by skopi on 13.06.2017.
 */

public class ContactsPresenterImpl implements ContactsPresenter {
    private static final String TAG = ContactsPresenterImpl.class.getSimpleName();
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";

    private ContactsView view;

    @Inject
    StorioSqlApi sqlApi;
    @Inject
    SharedPreferences mainPref;
    @Inject
    GoogleApiClient googleApiClient;

    @Inject
    public ContactsPresenterImpl(){

    }

    @Override
    public void attach(ContactsView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    private void deleteToken(){
        mainPref.edit()
                .remove(KEY_USER_TOKEN)
                .apply();
    }

    private void revokeAccess(){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()){
                            deleteToken();
                            sqlApi.clearDB();
                        }
                    }
                });
    }

    @Override
    public void signOut() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            if (status.isSuccess()) {
                                revokeAccess();
                                googleApiClient.disconnect();
                                view.onSignOut();
                            }
                        }
                    });
        }
        else {
            view.showMessage("Sign out failed. Try again later!");
        }
    }

    @Override
    public void loadContacts(String email) {
        //TODO: remove mock
//        sqlApi.clearDB();
//        List<Contact> mockContacts = new ArrayList<>();
//        Contact contact1 = new Contact(email, "Dima", "Skopintsev", "skopincev2015@ukr.net", "+380689840854");
//        Contact contact2 = new Contact(email, "Ruslan", "Buriak", "buriak2015@ukr.net", "+380689840855");
//        Contact contact3 = new Contact(email, "Vasya", "Dikiy", "dikiy2015@ukr.net", "+380689840856");
//        Contact contact4 = new Contact(email, "Mariya", "Nuzhna", "nuzhna2015@ukr.net", "+380689840856");
//        mockContacts.add(contact1);
//        mockContacts.add(contact2);
//        mockContacts.add(contact3);
//        mockContacts.add(contact4);
//        sqlApi.putContacts(mockContacts);

        List<Contact> contacts = new ArrayList<>(sqlApi.getContactsByOwnerEmail(email));

        view.onContactsLoaded(contacts);
    }

    @Override
    public void addContact(Contact contact) {
        sqlApi.put(contact);
        view.onContactAdded(sqlApi.getContactByEmail(contact.getEmail()));
    }

    @Override
    public void deleteContacts(List<Contact> contacts) {
        for (Contact contact: contacts){
            sqlApi.remove(contact);
        }
        view.onContactsDeleted();
    }

    @Override
    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        String regExpn = "^[+]?[0-9]{10,13}$";

        CharSequence inputStr = phoneNumber;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}

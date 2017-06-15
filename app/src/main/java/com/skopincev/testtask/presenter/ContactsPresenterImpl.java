package com.skopincev.testtask.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.skopincev.testtask.db.entity.Contact;
import com.skopincev.testtask.db.storio.StorioSqlApi;
import com.skopincev.testtask.ui.MainActivity;
import com.skopincev.testtask.view.ContactsView;

import java.util.ArrayList;
import java.util.List;

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
            view.showMessage("User isn't connected!");
        }
    }

    @Override
    public void loadContacts(String email) {
        //TODO: remove mock
//        List<Contact> mockContacts = new ArrayList<>();
//        Contact contact1 = new Contact(email, "Dima", "Skopintsev", "skopincev2015@ukr.net", "+380689840854");
//        Contact contact2 = new Contact(email, "Ruslan", "Buriak", "buriak2015@ukr.net", "+380689840855");
//        Contact contact3 = new Contact(email, "Vasya", "Dikiy", "dikiy2015@ukr.net", "+380689840856");
//        mockContacts.add(contact1);
//        mockContacts.add(contact2);
//        mockContacts.add(contact3);
//        sqlApi.putContacts(mockContacts);

        List<Contact> contacts = sqlApi.getContactsByEmail(email);

        if (contacts != null){
            view.onContactsLoaded(contacts);
        }
    }
}

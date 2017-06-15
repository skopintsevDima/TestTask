package com.skopincev.testtask.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.skopincev.testtask.ui.MainActivity;
import com.skopincev.testtask.view.ContactsView;

import javax.inject.Inject;

/**
 * Created by skopi on 13.06.2017.
 */

public class ContactsPresenterImpl implements ContactsPresenter {
    private static final String TAG = ContactsPresenterImpl.class.getSimpleName();
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";

    private ContactsView view;

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
}

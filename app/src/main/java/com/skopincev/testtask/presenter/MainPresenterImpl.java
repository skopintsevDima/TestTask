package com.skopincev.testtask.presenter;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skopincev.testtask.R;
import com.skopincev.testtask.view.MainView;

import javax.inject.Inject;

/**
 * Created by skopi on 13.06.2017.
 */

public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private MainView view;

    @Inject
    GoogleApiClient googleApiClient;

    @Inject
    public MainPresenterImpl(){

    }

    @Override
    public void attach(MainView view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        view.startValidationActivity(signInIntent);
    }

    @Override
    public void onSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            googleApiClient.connect();
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                String userEmail = account.getEmail();
                Log.d(TAG, "userEmail = " + userEmail);
                view.openContacts(userEmail);
            }
            else {
                Log.d(TAG, "Account is null!");
            }
        } else {
            view.showMessage(Resources.getSystem().getString(R.string.sign_in_failed_message));
        }
    }
}

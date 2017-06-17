package com.skopincev.testtask.presenter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.skopincev.testtask.R;
import com.skopincev.testtask.ui.MainActivity;
import com.skopincev.testtask.view.MainView;

import javax.inject.Inject;

/**
 * Created by skopi on 13.06.2017.
 */

public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();
    private static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";
    private static final String DEFAULT_TOKEN = "DEFAULT_TOKEN";

    private MainView view;

    @Inject
    SharedPreferences mainPref;
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
    public void silentSignIn() {
        if (getToken().equals(DEFAULT_TOKEN)){
            view.initUI();
        } else {
            Thread silentSignInThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final GoogleSignInResult googleSignInResult =
                            Auth.GoogleSignInApi.silentSignIn(googleApiClient).await();
                    ((MainActivity)view).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (googleSignInResult.isSuccess()) {
                                handleSignInResult(googleSignInResult);
                            } else {
                                view.initUI();
                            }
                        }
                    });
                }
            });
            silentSignInThread.start();
        }
    }

    @Override
    public void onSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void putToken(String token){
        mainPref.edit()
                .putString(KEY_USER_TOKEN, token)
                .apply();
    }

    @NonNull
    private String getToken(){
        return mainPref.getString(KEY_USER_TOKEN, DEFAULT_TOKEN);
    }

    private void handleSignInResult(final GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                String userToken = account.getIdToken();
                String userEmail = account.getEmail();
                Log.d(TAG, "userEmail = " + userEmail);
                if (getToken().equals(userToken)) {
                    view.openContacts(userEmail);
                }
                else {
                    putToken(userToken);
                    view.openContacts(userEmail);
                }
            }
            else {
                Log.d(TAG, "Account is null!");
            }
        }
    }

}

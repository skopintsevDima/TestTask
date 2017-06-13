package com.skopincev.testtask.presenter;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.skopincev.testtask.MainActivity;
import com.skopincev.testtask.view.MainView;

/**
 * Created by skopi on 13.06.2017.
 */

public class MainPresenterImpl implements MainPresenter {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();

    private MainView view;
    private GoogleApiClient googleApiClient;

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
        //TODO: remove to dagger inject
        initGoogleApiClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        view.startValidationActivity(signInIntent);
    }

    @Override
    public void signOut() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            googleApiClient.stopAutoManage((MainActivity)view);
                            googleApiClient.disconnect();
                            view.onSignOut(status.getStatusMessage());
                        }
                    });
        }
        else {
            Log.d(TAG, "User disconnected!");
        }
    }

    @Override
    public void onSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void initGoogleApiClient(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder((MainActivity)view)
                .enableAutoManage((MainActivity)view, (MainActivity)view)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                String userEmail = account.getEmail();
                Log.d(TAG, "userEmail = " + userEmail);
                view.openContacts(userEmail);
            }
            else {
                Log.d(TAG, "Account is NULL!");
            }
        } else {
            view.onSignInNotSucceed();
        }
    }
}

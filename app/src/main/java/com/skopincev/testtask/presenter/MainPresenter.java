package com.skopincev.testtask.presenter;

import android.content.Intent;

import com.skopincev.testtask.view.MainView;

/**
 * Created by skopi on 13.06.2017.
 */

public interface MainPresenter {
    void attach(MainView view);
    void detach();
    void signIn();
    void silentSignIn();
    void onSignInResult(Intent data);
}

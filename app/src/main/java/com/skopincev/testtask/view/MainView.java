package com.skopincev.testtask.view;

import android.content.Intent;

/**
 * Created by skopi on 13.06.2017.
 */

public interface MainView {
    void startValidationActivity(Intent intent);
    void openContacts(String userEmail);
    void onSignInNotSucceed();
    void onSignOut(String message);
}

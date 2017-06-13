package com.skopincev.testtask.presenter;

import com.skopincev.testtask.view.ContactsView;

/**
 * Created by skopi on 13.06.2017.
 */

public interface ContactsPresenter {
    void attach(ContactsView view);
    void detach();
    void signOut();
}

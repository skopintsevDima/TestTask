package com.skopincev.testtask.dagger.module;

import com.skopincev.testtask.presenter.ContactsPresenter;
import com.skopincev.testtask.presenter.ContactsPresenterImpl;
import com.skopincev.testtask.presenter.MainPresenter;
import com.skopincev.testtask.presenter.MainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Stormtrooper on 16.04.2017.
 */

@Module
public class PresenterModule {

    @Singleton
    @Provides
    public MainPresenter getMainPresenter(MainPresenterImpl presenter) {
        return presenter;
    }

    @Singleton
    @Provides
    public ContactsPresenter getContactsPresenter(ContactsPresenterImpl presenter) {
        return presenter;
    }
}

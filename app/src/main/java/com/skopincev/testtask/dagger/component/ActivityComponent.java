package com.skopincev.testtask.dagger.component;

import com.skopincev.testtask.presenter.ContactsPresenter;
import com.skopincev.testtask.ui.ContactsActivity;
import com.skopincev.testtask.ui.MainActivity;

import dagger.Subcomponent;

/**
 * Created by raisa on 19.03.17.
 */

@Subcomponent
/** Activity component. Used to handle activity related DI into activities. */

public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(ContactsActivity activity);
}


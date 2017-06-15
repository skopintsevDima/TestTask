package com.skopincev.testtask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.skopincev.testtask.R;
import com.skopincev.testtask.dagger.base.BaseActivity;
import com.skopincev.testtask.dagger.component.ActivityComponent;
import com.skopincev.testtask.db.entity.Contact;
import com.skopincev.testtask.presenter.ContactsPresenter;
import com.skopincev.testtask.view.ContactsView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends BaseActivity
        implements ContactsView {

    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";

    @Inject
    ContactsPresenter presenter;

    @BindView(R.id.rv_contacts_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        presenter.attach(this);
        loadResults();
    }

    private void loadResults()
    {
        String email = null;
        if (getIntent() != null) {
            email = getIntent().getStringExtra(KEY_USER_EMAIL);
        }
        presenter.loadContacts(email);
    }

    @Override
    public void inject(ActivityComponent injector) {
        injector.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_logout:{
                presenter.signOut();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
        presenter = null;
    }

    @Override
    public void onSignOut() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onContactsLoaded(List<Contact> contacts) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, llm.getOrientation());
        recyclerView.addItemDecoration(divider);

        ContactsRecyclerAdapter adapter = new ContactsRecyclerAdapter(this, contacts);
        recyclerView.setAdapter(adapter);
    }
}

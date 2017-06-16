package com.skopincev.testtask.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.skopincev.testtask.ui.other.ContactsRecyclerAdapter;
import com.skopincev.testtask.ui.other.WrapContentLinearLayoutManager;
import com.skopincev.testtask.view.ContactsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends BaseActivity
        implements ContactsView {

    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final int DELETING_CONFIRMATION = 1;

    @Inject
    ContactsPresenter presenter;

    @BindView(R.id.rv_contacts_list)
    RecyclerView recyclerView;
    private ContactsRecyclerAdapter adapter;
    private List<Contact> contacts;
    private String userEmail;
    private boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);

        presenter.attach(this);
        if (getIntent() != null) {
            userEmail = getIntent().getStringExtra(KEY_USER_EMAIL);
            if (userEmail != null)
                presenter.loadContacts(userEmail);
        }
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

    private Dialog createDeletingConfirmation(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.confirm_dialog_title);
        dialogBuilder.setMessage(R.string.confirm_dialog_message);
        dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        dialogBuilder.setPositiveButton(getString(R.string.confirm_dialog_positive_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Contact> deletedContacts = new ArrayList<>();
                for (Integer position: adapter.getCheckedItems()){
                    deletedContacts.add(contacts.get(position));
                }
                presenter.deleteContacts(deletedContacts);
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.confirm_dialog_negative_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.onContactsDeletingDismissed();
            }
        });
        return dialogBuilder.create();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DELETING_CONFIRMATION:{
                return createDeletingConfirmation();
            }
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mi_logout:{
                presenter.signOut();
                break;
            }
            case R.id.mi_add:{
                if (!deleteMode) {
                    presenter.addContact(userEmail);
                }
                break;
            }
            case R.id.mi_delete:{
                if (deleteMode){
                    if (adapter.getCheckedItems().size() > 0) {
                        showDialog(DELETING_CONFIRMATION);
                    } else {
                        adapter.onContactsDeletingDismissed();
                    }
                    deleteMode = false;
                    item.setIcon(R.drawable.ic_delete_enabled_mode);
                } else {
                    deleteMode = true;
                    adapter.onDeleteModeActivated();
                    item.setIcon(R.drawable.ic_delete_disabled_mode);
                }
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
        this.contacts = contacts;
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, llm.getOrientation());
        recyclerView.addItemDecoration(divider);
        adapter = new ContactsRecyclerAdapter(this, contacts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onContactAdded(Contact contact) {
        adapter.add(contact);
    }

    @Override
    public void onContactsDeleted() {
        List<Integer> positions = adapter.getCheckedItems();
        adapter.removeItems(positions);
        adapter.onContactsDeleted();
    }
}

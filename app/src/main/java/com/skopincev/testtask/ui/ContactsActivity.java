package com.skopincev.testtask.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skopincev.testtask.AboutActivity;
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
    private static final int ADD_CONTACT = 2;
    private static final int CHOOSE_ALPHABET_SORTING_MODE = 3;
    private static final int TYPE_SUBSTRING = 4;
    private static final int TYPE_PHONE_CODE = 5;

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
        dialogBuilder.setPositiveButton(R.string.confirm_dialog_positive_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Contact> deletingContacts = new ArrayList<>();
                for (Integer position: adapter.getCheckedItems()){
                    if (position >= 0 && position <= contacts.size() - 1) {
                        deletingContacts.add(contacts.get(position));
                    }
                    else {
                        showMessage("Position: " + position + " is out of bound");
                    }
                }
                presenter.deleteContacts(deletingContacts);
            }
        });
        dialogBuilder.setNegativeButton(R.string.confirm_dialog_negative_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.onContactsDeletingDismissed();
            }
        });
        Dialog confirmation = dialogBuilder.create();
        confirmation.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.black));

                Button positiveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
            }
        });
        return confirmation;
    }

    private Dialog createAddContactDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_add_contact, null);
        final EditText et_first_name = (EditText) view.findViewById(R.id.et_first_name);

        final EditText et_last_name = (EditText) view.findViewById(R.id.et_last_name);

        final EditText et_phone_number = (EditText) view.findViewById(R.id.et_phone_number);

        final EditText et_email = (EditText) view.findViewById(R.id.et_email);

        final ColorStateList hintColorStateList = et_first_name.getHintTextColors();
        final ColorStateList textColorStateList = et_first_name.getTextColors();
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    ((EditText)view).setHintTextColor(hintColorStateList);
                    ((EditText)view).setTextColor(textColorStateList);
                }
            }
        };
        et_first_name.setOnFocusChangeListener(onFocusChangeListener);
        et_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_first_name.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        et_last_name.setOnFocusChangeListener(onFocusChangeListener);
        et_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_last_name.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        et_phone_number.setOnFocusChangeListener(onFocusChangeListener);
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_phone_number.setTextColor(textColorStateList);
                et_phone_number.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        et_email.setOnFocusChangeListener(onFocusChangeListener);
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_email.setTextColor(textColorStateList);
                et_email.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(R.string.dialog_add_positive_answer, null);
        dialogBuilder.setNeutralButton(getString(R.string.dialog_add_neutral_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button neutralButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setTextColor(getResources().getColor(R.color.black));

                Button positiveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_first_name.getText().length() == 0) {
                            et_first_name.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        }
                        if (et_last_name.getText().length() == 0) {
                            et_last_name.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        }
                        if (et_phone_number.getText().length() == 0) {
                            et_phone_number.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        } else if (!presenter.isPhoneNumberValid(et_phone_number.getText().toString())) {
                            et_phone_number.setTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            showMessage(getString(R.string.add_dialog_phone_not_valid));
                            return;
                        }
                        if (et_email.getText().length() == 0) {
                            et_email.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        } else if (!presenter.isEmailValid(et_email.getText().toString())) {
                            et_email.setTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            showMessage(getString(R.string.add_dialog_email_not_valid));
                            return;
                        }
                        Contact newContact = new Contact(userEmail,
                                et_first_name.getText().toString(),
                                et_last_name.getText().toString(),
                                et_email.getText().toString(),
                                et_phone_number.getText().toString());
                        presenter.addContact(newContact);
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    private Dialog createAlphabetSortingModeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose the sorting mode");
        final CharSequence[] items = {ContactsRecyclerAdapter.ALPHABET_NORMAL_ORDER, ContactsRecyclerAdapter.ALPHABET_REVERSE_ORDER};
        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.sortByAlphabet(items[which].toString());
            }
        });
        Dialog choosing = dialogBuilder.create();
        return choosing;
    }

    private Dialog createTypePhoneCodeDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_type_phone_code, null);
        final EditText et_phone_code = (EditText) view.findViewById(R.id.et_phone_code);

        final ColorStateList hintColorStateList = et_phone_code.getHintTextColors();
        final ColorStateList textColorStateList = et_phone_code.getTextColors();
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    ((EditText)view).setHintTextColor(hintColorStateList);
                    ((EditText)view).setTextColor(textColorStateList);
                }
            }
        };
        et_phone_code.setOnFocusChangeListener(onFocusChangeListener);
        et_phone_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_phone_code.setTextColor(textColorStateList);
                et_phone_code.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(R.string.dialog_add_positive_answer, null);
        dialogBuilder.setNeutralButton(getString(R.string.dialog_add_neutral_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button neutralButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setTextColor(getResources().getColor(R.color.black));

                Button positiveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_phone_code.getText().length() == 0) {
                            et_phone_code.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        } else if (!presenter.isPhoneCodeValid(et_phone_code.getText().toString())) {
                            et_phone_code.setTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            showMessage(getString(R.string.dialog_phone_code_not_valid));
                            return;
                        }
                        adapter.sortByPhoneCode(et_phone_code.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    private Dialog createTypeSubstringDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_type_substring, null);
        final EditText et_substring = (EditText) view.findViewById(R.id.et_substring);

        final ColorStateList hintColorStateList = et_substring.getHintTextColors();
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    ((EditText)view).setHintTextColor(hintColorStateList);
                }
            }
        };
        et_substring.setOnFocusChangeListener(onFocusChangeListener);
        et_substring.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_substring.setHintTextColor(hintColorStateList);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(view);
        dialogBuilder.setPositiveButton(R.string.dialog_add_positive_answer, null);
        dialogBuilder.setNeutralButton(getString(R.string.dialog_add_neutral_answer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button neutralButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                neutralButton.setTextColor(getResources().getColor(R.color.black));

                Button positiveButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_substring.getText().length() == 0) {
                            et_substring.setHintTextColor(getResources().getColor(R.color.dialogInfoNotAdded));
                            return;
                        }
                        adapter.sortBySubstring(et_substring.getText().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
        return dialog;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DELETING_CONFIRMATION:{
                return createDeletingConfirmation();
            }
            case ADD_CONTACT:{
                return createAddContactDialog();
            }
            case CHOOSE_ALPHABET_SORTING_MODE:{
                return createAlphabetSortingModeDialog();
            }
            case TYPE_SUBSTRING:{
                return createTypeSubstringDialog();
            }
            case TYPE_PHONE_CODE:{
                return createTypePhoneCodeDialog();
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
                    showDialog(ADD_CONTACT);
                }
                break;
            }
            case R.id.mi_delete:{
                if (deleteMode){
                    //TODO: refactor delete mode
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
            case R.id.mi_sort_by_alphabet:{
                if (!deleteMode) {
                    showDialog(CHOOSE_ALPHABET_SORTING_MODE);
                }
                break;
            }
            case R.id.mi_sort_by_substring:{
                if (!deleteMode) {
                    showDialog(TYPE_SUBSTRING);
                }
                break;
            }
            case R.id.mi_sort_by_phone_code:{
                if (!deleteMode) {
                    showDialog(TYPE_PHONE_CODE);
                }
                break;
            }
            case R.id.mi_about:{
                startActivity(new Intent(this, AboutActivity.class));
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

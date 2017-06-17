package com.skopincev.testtask.ui.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skopincev.testtask.R;
import com.skopincev.testtask.db.entity.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by skopi on 16.06.2017.
 */

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ContactViewHolder> {

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private RelativeLayout rl_contact;
        private TextView tv_full_name;
        private TextView tv_phone_number;
        private TextView tv_email;
        private CheckBox cb_checked;

        public ContactViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            rl_contact = (RelativeLayout) itemView.findViewById(R.id.rl_contact);
            rl_contact.setOnClickListener(this);

            tv_full_name = (TextView) itemView.findViewById(R.id.tv_full_name);
            tv_phone_number = (TextView) itemView.findViewById(R.id.tv_phone_number);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);

            cb_checked = (CheckBox) itemView.findViewById(R.id.cb_checked);
        }

        public void bind(Contact data){
            String fullName = data.getFirstName() + " " + data.getLastName();
            tv_full_name.setText(fullName);

            String phoneNumber = "Phone number: " + data.getPhoneNumber();
            tv_phone_number.setText(phoneNumber);

            String email = "Email: " + data.getEmail();
            tv_email.setText(email);
        }

        public void setCheckerState(boolean checked){
            if (checked){
                cb_checked.setChecked(true);

            } else {
                cb_checked.setChecked(false);
            }
        }

        public void setCheckerVisibility(int visibility){
            cb_checked.setVisibility(visibility);
        }

        @Override
        public void onClick(View v) {
            if (deleteMode){
                if (cb_checked.isChecked()){
                    checkedItems.remove(this);
                    setCheckerState(false);
                } else {
                    checkedItems.add(this);
                    setCheckerState(true);
                }
            }
        }
    }

    private List<Contact> items;
    private LayoutInflater inflater;
    private List<ContactViewHolder> holders = new ArrayList<>();
    private List<ContactViewHolder> checkedItems = new ArrayList<>();
    private boolean deleteMode = false;

    public List<Integer> getCheckedItems() {
        List<Integer> positions = new ArrayList<>();
        for (ContactViewHolder holder: checkedItems){
            positions.add(holder.getPosition());
        }
        return positions;
    }

    public void sortByAlphabet(){
        Collections.sort(items, new Comparator<Contact>() {
            @Override
            public int compare(Contact contact1, Contact contact2) {
                String fullName1 = contact1.getFirstName() + " " + contact1.getLastName();
                String fullName2 = contact2.getFirstName() + " " + contact2.getLastName();
                return fullName1.compareTo(fullName2);
            }
        });
        notifyDataSetChanged();
    }

    public ContactsRecyclerAdapter(Context context, List<Contact> items)
    {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    public void onDeleteModeActivated(){
        deleteMode = true;
        for (ContactViewHolder holder: holders){
            holder.setCheckerState(false);
            holder.setCheckerVisibility(View.VISIBLE);
        }
    }

    public void onContactsDeleted(){
        deleteMode = false;
        checkedItems = new ArrayList<>();
        for (ContactViewHolder holder: holders){
            holder.setCheckerVisibility(View.INVISIBLE);
        }
    }

    public void onContactsDeletingDismissed(){
        deleteMode = false;
        for (ContactViewHolder holder: holders){
            holder.setCheckerState(false);
            holder.setCheckerVisibility(View.INVISIBLE);
        }
        checkedItems = new ArrayList<>();
    }

    public void add(Contact contact){
        items.add(contact);
        notifyItemInserted(items.size() - 1);
    }

    public void removeItems(List<Integer> positions){
        List<Contact> removeItems = new ArrayList<>();
        for (Integer position: positions){
            if (position >= 0 && position <= items.size() - 1)
            removeItems.add(items.get(position));
        }
        items.removeAll(removeItems);
        notifyDataSetChanged();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rvi_contact, parent, false);
        ContactViewHolder holder = new ContactViewHolder(parent, view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (deleteMode){
            holder.setCheckerVisibility(View.VISIBLE);
        }
        Contact data = items.get(position);
        if (data != null) {
            holder.bind(data);
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }
}
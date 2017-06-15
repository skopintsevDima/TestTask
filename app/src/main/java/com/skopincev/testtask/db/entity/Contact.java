package com.skopincev.testtask.db.entity;

/**
 * Created by skopi on 16.06.2017.
 */

public class Contact {
    private int _id;

    private String ownerEmail;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    public int get_id() {
        return _id;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Contact(String ownerEmail,
                   String firstName,
                   String lastName,
                   String email,
                   String phoneNumber){

        this.ownerEmail = ownerEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Contact(int _id,
                   String ownerEmail,
                   String firstName,
                   String lastName,
                   String email,
                   String phoneNumber){

        this._id = _id;
        this.ownerEmail = ownerEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

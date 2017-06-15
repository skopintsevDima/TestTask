package com.skopincev.testtask.db;

/**
 * Created by skopi on 16.06.2017.
 */

public class DBContract {
    public static class ContactContract{
        public static final String TABLE_NAME = "Contacts";

        public static final String KEY_ID = "_id";
        public static final String KEY_OWNER_EMAIL = "ownerEmail";
        public static final String KEY_FIRST_NAME = "firstName";
        public static final String KEY_LAST_NAME = "lastName";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_PHONE_NUMBER = "phoneNumber";

        public static final String CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        KEY_ID + " INTEGER PRIMARY KEY, " +
                        KEY_OWNER_EMAIL + " TEXT, " +
                        KEY_FIRST_NAME + " TEXT, " +
                        KEY_LAST_NAME + " TEXT, " +
                        KEY_EMAIL + " TEXT, " +
                        KEY_PHONE_NUMBER + " TEXT " +
                        ")";

        public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}

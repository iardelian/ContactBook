package com.iardel.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.HashMap;

public class EditContact extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText emailAddress;
    EditText homeAddress;

    DBTools dbTools;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_contact);
        firstName = (EditText) findViewById(R.id.firstNameEditText);
        lastName = (EditText) findViewById(R.id.lastNameEditText);
        phoneNumber = (EditText) findViewById(R.id.phoneNumberEditText);
        emailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        homeAddress = (EditText) findViewById(R.id.homeAddressEditText);

        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");

        dbTools = new DBTools(this, new UserPreference(this).getEmail());
        HashMap<String, String> contactList = dbTools.getContactInfo(contactId);

        if (contactList.size() != 0) {
            firstName.setText(contactList.get("firstName"));
            lastName.setText(contactList.get("lastName"));
            phoneNumber.setText(contactList.get("phoneNumber"));
            emailAddress.setText(contactList.get("emailAddress"));
            homeAddress.setText(contactList.get("homeAddress"));
        }

    }

    public void editContact(View view) {
        HashMap<String, String> queryValuesMap = new HashMap<>();
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");

        queryValuesMap.put("contactId", contactId);
        queryValuesMap.put("firstName", firstName.getText().toString());
        queryValuesMap.put("lastName", lastName.getText().toString());
        queryValuesMap.put("phoneNumber", phoneNumber.getText().toString());
        queryValuesMap.put("emailAddress", emailAddress.getText().toString());
        queryValuesMap.put("homeAddress", homeAddress.getText().toString());

        dbTools.updateContact(queryValuesMap);
        this.callMyActivity();


    }

    public void removeContact(View view) {
        Intent intent = getIntent();
        String contactId = intent.getStringExtra("contactId");
        dbTools.deleteContact(contactId);
        this.callMyActivity();
    }

    private void callMyActivity() {
        Intent mainIntent = new Intent(getApplication(), MainActivity.class);
        mainIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mainIntent);
    }
}

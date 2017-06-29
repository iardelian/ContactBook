package com.iardel.contactbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.HashMap;

public class NewContact extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText emailAddress;
    EditText homeAddress;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_new_contact);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        emailAddress = (EditText) findViewById(R.id.emailAddress);
        homeAddress = (EditText) findViewById(R.id.homeAddress);

    }

    public void addNewContact(View view) {
        HashMap<String, String> queryValuesMap = new HashMap<>();

        queryValuesMap.put("firstName", firstName.getText().toString());
        queryValuesMap.put("lastName", lastName.getText().toString());
        queryValuesMap.put("phoneNumber", phoneNumber.getText().toString());
        queryValuesMap.put("emailAddress", emailAddress.getText().toString());
        queryValuesMap.put("homeAddress", homeAddress.getText().toString());

        DBTools dbTools = new DBTools(this, new UserPreference(this).getEmail());
        dbTools.insertContact(queryValuesMap);

        this.callMainActivity(view);

    }

    public void callMainActivity(View view) {
        Intent mainIntent = new Intent(getApplication(), MainActivity.class);
        mainIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mainIntent);
    }
}

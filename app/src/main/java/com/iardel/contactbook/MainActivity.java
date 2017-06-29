package com.iardel.contactbook;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView contactId;
    DBTools mDBTools;
    ArrayList<HashMap<String, String>> contactList;
    ListView listView;
    ListAdapter adapter;
    boolean doubleBackToExitPressedOnce = false;
    private GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mDBTools = new DBTools(this, new UserPreference(this).getEmail());
        contactList = mDBTools.getAllContacts();

        displayUserInfo();


        if (contactList.size() != 0) {
            listView = (ListView) findViewById(R.id.listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    contactId = (TextView) view.findViewById(R.id.contactId);
                    String contactIdValue = contactId.getText().toString();
                    Intent editIntent = new Intent(getApplicationContext(), EditContact.class);
                    editIntent.putExtra("contactId", contactIdValue);
                    editIntent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
                            Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(editIntent);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView pNumber = (TextView) view.findViewById(R.id.contactPhone);
                    String phoneNumber = pNumber.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                    return true;
                }
            });

            adapter = new SimpleAdapter(this, contactList, R.layout.contact_element
                    , new String[]{"contactId", "lastName", "firstName", "phoneNumber", "emailAddress"},
                    new int[]{R.id.contactId, R.id.contactLastName,
                            R.id.contactName, R.id.contactPhone, R.id.contactEmail});
            listView.setAdapter(adapter);
        }

    }

    private void displayUserInfo() {

        UserPreference userPreference = new UserPreference(this);

        String userName = userPreference.getName();
        String userEmail = userPreference.getEmail();
        String userPhotoUrl = userPreference.getPhoto();

        ImageView imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        TextView uName = (TextView) findViewById(R.id.userName);
        TextView uEmail = (TextView) findViewById(R.id.userEmail);

        uName.setText(userName);
        uEmail.setText(userEmail);
        Glide.with(getApplicationContext())
                .load(userPhotoUrl)
                .into(imgProfilePic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contact: {
                showAddContact();
                return true;
            }
            case R.id.sort_firstname: {
                sortList("firstName");
                return true;
            }
            case R.id.sort_lastname: {
                sortList("lastName");
                return true;
            }
            case R.id.sort_date: {
                sortList("contactId");
                return true;
            }
            case R.id.log_out: {
                logOut();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        new UserPreference(this).clearPreferences();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
                                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    private void sortList(String key) {
        if (contactList.size() != 0) {
            Collections.sort(contactList, new MapComparator(key));
            ((BaseAdapter) adapter).notifyDataSetChanged();
        }
    }

    public void showAddContact() {
        Intent mainIntent = new Intent(getApplication(), NewContact.class);
        mainIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(mainIntent);
    }

    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press \"Back\" again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }

}

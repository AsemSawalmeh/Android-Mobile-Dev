package com.example.trackmaniaexchange;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class loginActivity extends AppCompatActivity {
    public static final String DATA = "DATA";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_PASSWORD = "PREF_PASSWORD";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static List<user> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupSharedPrefs();
        userList = loadUsers();

        System.out.println(userList);
        System.out.println(PREF_USERNAME);

        EditText nameText = findViewById(R.id.nameText);
        EditText passwordText = findViewById(R.id.passwordText);
        CheckBox rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        if (prefs.contains(PREF_USERNAME) && prefs.contains(PREF_PASSWORD)) {
            nameText.setText(prefs.getString(PREF_USERNAME, ""));
            passwordText.setText(prefs.getString(PREF_PASSWORD, ""));
            rememberMeCheckbox.setChecked(true);
        }

        String newUsername = getIntent().getStringExtra("NEW_USERNAME");
        String newPassword = getIntent().getStringExtra("NEW_PASSWORD");

        if (newUsername != null && newPassword != null) {
            nameText.setText(newUsername);
            passwordText.setText(newPassword);
        }

        TextView directRegisterText = findViewById(R.id.directRegisterText);
        directRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginActivity.this, registerActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = nameText.getText().toString();
                String enteredPassword = passwordText.getText().toString();

                if (isValidUser(enteredUsername, enteredPassword)) {
                    if (rememberMeCheckbox.isChecked()) {
                        saveRememberMePreferences(enteredUsername, enteredPassword);
                    }

                    Intent intent = new Intent(loginActivity.this, landingPage.class);
                    intent.putExtra("USERNAME", enteredUsername);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(loginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private List<user> loadUsers() {
        Gson gson = new Gson();
        String str = prefs.getString(DATA, "");

        if (!str.isEmpty()) {
            try {
                Type userListType = new TypeToken<List<user>>() {
                }.getType();
                List<user> users = gson.fromJson(str, userListType);

                return users != null ? new ArrayList<>(users) : new ArrayList<>();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private boolean isValidUser(String username, String password) {
        for (user u : userList) {
            if (u.getName().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private void saveRememberMePreferences(String username, String password) {
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);
        editor.apply();
    }

}
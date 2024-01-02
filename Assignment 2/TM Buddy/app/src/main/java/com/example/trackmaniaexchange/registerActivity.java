package com.example.trackmaniaexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class registerActivity extends AppCompatActivity {
    public static final String DATA = "DATA";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setupSharedPrefs();

        TextView directLoginText = findViewById(R.id.directLoginText);
        directLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        EditText nameText = findViewById(R.id.nameText);
        EditText passwordText = findViewById(R.id.passwordText);
        EditText confirmPasswordText = findViewById(R.id.confirmPasswordText);
        Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = nameText.getText().toString();
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(registerActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.length() < 6 || username.length() > 32) {
                    Toast.makeText(registerActivity.this, "Username must be between 6 and 32 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6 || password.length() > 32) {
                    Toast.makeText(registerActivity.this, "Password must be between 6 and 32 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(registerActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<user> userList = loginActivity.userList;

                for (user user : userList) {
                    if (user.getName().equals(username)) {
                        Toast.makeText(registerActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                user newUser = new user(username, password);
                userList.add(newUser);

                saveUsers(userList);

                Intent intent = new Intent(registerActivity.this, loginActivity.class);

                if (userList != null && !userList.isEmpty()) {
                    user lastUser = userList.get(userList.size() - 1);
                    intent.putExtra("NEW_USERNAME", lastUser.getName());
                    intent.putExtra("NEW_PASSWORD", lastUser.getPassword());
                }

                startActivity(intent);
                finish();
            }

        });
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    void saveUsers(List<user> userList) {
        Gson gson = new Gson();
        String userString = gson.toJson(userList);

        editor.putString(DATA, userString);
        editor.commit();
    }
}
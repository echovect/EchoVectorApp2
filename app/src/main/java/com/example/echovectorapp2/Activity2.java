package com.example.echovectorapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.tasks.Task.*;

public class Activity2 extends AppCompatActivity {

    private static final String TAG = "Activity2";
    private EditText name;
    private EditText email;
    private EditText password;
    private Button saveButton;


    //Keys
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        saveButton = findViewById(R.id.button2);
        email = findViewById(R.id.emailInput);
        name = findViewById(R.id.nameInput);
        password= findViewById(R.id.passwordInput);

        firebaseAuth = FirebaseAuth.getInstance();

        // Create a new user with a name, email, and password
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameField = name.getText().toString().trim();
                String emailField = email.getText().toString().trim();
                String passwordField = password.getText().toString().trim();
               final Map<String, Object> user = new HashMap<>();

                user.put(KEY_NAME, nameField);
                user.put(KEY_EMAIL, emailField);
                user.put(KEY_PASSWORD, passwordField);


                if (TextUtils.isEmpty(nameField)) {

                    Toast.makeText(Activity2.this, "Please enter fullname", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(emailField)) {

                    Toast.makeText(Activity2.this, "Please enter email", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(passwordField)) {

                    Toast.makeText(Activity2.this, "Please enter password", Toast.LENGTH_LONG).show();
                }

                if (passwordField.length() < 6) {
                    Toast.makeText(Activity2.this, "your password is too short", Toast.LENGTH_LONG).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(emailField, passwordField)
                        .addOnCompleteListener(Activity2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    db.collection("Users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(Activity2.this, "Successfully registered!", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Activity2.this, "Error! adding data to database.", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }

                            });
                                    startActivity(new Intent(getApplicationContext(), Activity4.class));

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Activity2.this, "Authentication failed", Toast.LENGTH_LONG).show();

                                }


                            }
                        });

            }
            });}}
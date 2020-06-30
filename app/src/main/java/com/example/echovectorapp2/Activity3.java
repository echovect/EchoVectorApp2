package com.example.echovectorapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Activity3 extends AppCompatActivity {

    ExpandableListView expandableListView;

    /** Opens settings page */
    public void openSettings(View v) {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    /** Opens order page */
    public void openOrders(View v) {
        Intent intent = new Intent(this, orders.class);
        startActivity(intent);
    }

    /** Opens first page of sequence */
    public void startSequence(View v) {
        Intent intent = new Intent(this, sequencePage1.class);
        startActivity(intent);
    }



    private static final String TAG = "Activity3";
    private TextView product;
    private TextView description;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        //expandableListView = (ExpandableListView) findViewById(R.id.)

        //Getting by ids
        description = findViewById(R.id.productDescription);
        product = findViewById(R.id.productTitle);

        db.collection("products_uploaded")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}

package com.example.echovectorapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class businessActivity3 extends AppCompatActivity {

    /** Opens business order page */
    public void openPostProductOfferings(View v) {
        Intent intent = new Intent(this, businessActivity3.class);
        startActivity(intent);
    }

    /** Opens settings page */
    public void openSettings(View v) {
        Intent intent = new Intent(this, influencerSettings.class);
        startActivity(intent);
    }

    /** Opens order page */
    public void openProductOfferings(View v) {
        Intent intent = new Intent(this, postProductOfferings.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business3);
    }

    /** Expands product menu */
    public void expandProducts(View v) {

        findViewById(R.id.expandableImage1).setVisibility(View.VISIBLE);
        findViewById(R.id.expandableImage2).setVisibility(View.VISIBLE);


    }
}
package com.example.echovectorapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class sequencePage1 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_page1);
    }

    /** Calls product upload page - intended for business owner log in*/
    public void openActivity3_2(View v) {
        Intent intent = new Intent(this, Activity3_2.class);
        startActivity(intent);
    }

    /** Opens third activity which is the product page*/
    public void openActivity3(View v) {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }


}
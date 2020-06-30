package com.example.echovectorapp2;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button button; // Button for Activity 2 which is registration
    private Button button2; //Button for Activity 3 which is products
    private Button button3; //Button for Activity 4 which is upload product page
    private Button button4; // Button for Activity 5 which is product status page
    private Button startSequence;


    private Button button3_2; // Button for Activity 5
    private Button buttonList; // Button for Activity 5
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2(); // registration page
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3(); // Product Page
            }
        });

        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4(); //  Upload Page
            }
        });

        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5(); //  Product Status Page
            }
        });

        startSequence = (Button) findViewById(R.id.startSequence);
        startSequence.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startSequence();
            }
        });


        button3_2 = (Button) findViewById(R.id.button3_2);
        button3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3_2(); //  Product upload Page
            }
        });

        buttonList = (Button) findViewById(R.id.activity_product_list);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityList(); // List of Products Page
            }
        });
    }

    /** Opens second activity which is the registraion page*/
    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    /** Opens third activity which is the product page*/
    public void openActivity3() {
        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);
    }

    /** Opens fourth activity which is the upload page*/
    public void openActivity4() {
        Intent intent = new Intent(this, Activity4.class);
        startActivity(intent);
    }

    /** Opens fifth activity which is the product status page */
    public void openActivity5() {
        Intent intent = new Intent(this, Activity5.class);
        startActivity(intent);
    }

    /** Opens first page of sequence */
    public void startSequence() {
        Intent intent = new Intent(this, sequencePage1.class);
        startActivity(intent);
    }










    /** Opens fifth activity which is the product status page */
    public void openActivity3_2() {
        Intent intent = new Intent(this, Activity3_2.class);
        startActivity(intent);
    }

    /** Opens fifth activity which is the product status page */
    public void openActivityList() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
}
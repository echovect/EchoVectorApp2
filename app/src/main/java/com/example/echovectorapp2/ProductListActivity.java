package com.example.echovectorapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Product;
import ui.ProductRecyclerAdapter;

public class ProductListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
//    private FirebaseAuth.AuthStateListener authStateListener;
//    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Product> productList;
    private RecyclerView recyclerView;
    private ProductRecyclerAdapter productRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Product_Posted");
    private TextView noProductEntry;

    /** Opens fifth activity which is the product status page */
    public void openActivity5(View v) {
        Intent intent = new Intent(this, Activity5.class);
        startActivity(intent);
    }

    /** Opens first page of sequence */
    public void startSequence(View v) {
        Intent intent = new Intent(this, sequencePage1.class);
        startActivity(intent);
    }

    /** Opens product list*/
    public void openActivityList(View v) {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

//        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();

        noProductEntry = findViewById(R.id.list_no_product);

        productList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater findMenuItems = getMenuInflater();
//        findMenuItems.inflate(R.menu.main_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                //Take users to add Journal
//                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(ProductListActivity.this,
                            Activity3_2.class));
                    //finish();
//                }
                break;
//            case R.id.action_signout:
//                //sign user out!
//                if (user != null && firebaseAuth != null) {
//                    firebaseAuth.signOut();
//
//                    startActivity(new Intent(JournalListActivity.this,
//                            MainActivity.class));
//                    //finish();
//                }
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

//        collectionReference.whereEqualTo("userId", JournalApi.getInstance()
//                .getUserId())
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot product : queryDocumentSnapshots) {
                                Product product1 =  product.toObject(Product.class);
                                productList.add(product1);
                            }

                            //Invoke recyclerview
                            productRecyclerAdapter = new ProductRecyclerAdapter(ProductListActivity.this,
                                    productList);


                            recyclerView.setAdapter(productRecyclerAdapter);
                            productRecyclerAdapter.notifyDataSetChanged();

                        }else {
                            noProductEntry.setVisibility(View.VISIBLE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
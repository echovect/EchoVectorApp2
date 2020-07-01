package com.example.echovectorapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Objects;

import model.Product;

public class Activity3_2 extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "Activity3_2";
    private Button uploadButton;
    private ProgressBar progressBar;
    private ImageView addPhotoButton;
    private EditText titleEditText;
    private EditText descriptionEditText;

//    private TextView currentUserTextView;
    private ImageView imageView;

//    private String currentUserId;
//    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
//    private FirebaseUser user;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Product_Posted");
    private Uri imageUri;

    /** Opens first page of sequence */
    public void startSequence(View v) {
        Intent intent = new Intent(this, sequencePage1.class);
        startActivity(intent);
    }


    /** Opens settings page */
    public void openBusinessSettings(View v) {
        Intent intent = new Intent(this, settingsBusiness.class);
        startActivity(intent);
    }

    /** Opens product offerings page*/
    public void openOfferings(View v) {
        Intent intent = new Intent(this, productOfferings.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3_2);

//        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar_button);
        titleEditText = findViewById(R.id.product_title);
        descriptionEditText = findViewById(R.id.product_description);
//        currentUserTextView = findViewById(R.id.post_username_textview);

        imageView = findViewById(R.id.product_imageView);
        uploadButton = findViewById(R.id.upload_product_button);
        uploadButton.setOnClickListener(this);
        addPhotoButton = findViewById(R.id.productCameraButton);
        addPhotoButton.setOnClickListener(this);

        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_product_button:
                //saveProduct
                saveProduct();
                break;
            case R.id.productCameraButton:
                //get image from gallery/phone
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
        }

    }

    private void saveProduct() {
        final String title = titleEditText.getText().toString().trim();
        final String description = descriptionEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(description)
                && imageUri != null) {

            final StorageReference filepath = storageReference //.../journal_images/our_image.jpeg
                    .child("product_images").child("product_" + Timestamp.now().getSeconds()); // my_image_887474737

            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUrl = uri.toString();
                                    //Todo: create a Journal Object - model
                                    Product product = new Product();
                                    product.setTitle(title);
                                    product.setDescription(description);
                                    product.setImageUrl(imageUrl);
                                    product.setTimeAdded(new Timestamp(new Date()));
//                                    journal.setUserName(currentUserName);
//                                    journal.setUserId(currentUserId);

                                    //Todo:invoke our collectionReference
                                    collectionReference.add(product)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    Toast.makeText(Activity3_2.this, "Uploaded!", Toast.LENGTH_LONG).show();

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(Activity3_2.this,
                                                            ProductListActivity.class));
                                                    finish();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    for (int i=0; i < 3; i++)
                                                    Toast.makeText(Activity3_2.this, "Failed on 'Firestore'. --> "+e.getMessage(), Toast.LENGTH_LONG).show();
                                                    Log.d(TAG, "onFailure: " + e.getMessage());

                                                }
                                            });
                                    //Todo: and save a Journal instance.

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            for(int i=0; i<3; i++)
                            Toast.makeText(Activity3_2.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });


        } else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(Activity3_2.this, "Please input all details.", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData(); // we have the actual path to the image
                imageView.setImageURI(imageUri);//show image

            }
        }
    }
}
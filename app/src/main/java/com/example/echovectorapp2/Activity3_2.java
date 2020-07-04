package com.example.echovectorapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.google.common.io.Files;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.compiler.PluginProtos;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

import java.io.File;
import model.Product;

public class Activity3_2 extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "Activity3_2";
    private static final int THUMBNAIL_SIZE = 64 ;
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
    private Bitmap bmp;


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
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData(); // we have the actual path to the image
                imageView.setImageURI(imageUri);//show image

                try {
                    bitmap = getThumbnail(imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(bitmap != null) {
                    try {
                        addWatermarkOnImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



            }
        }
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input1;
        input1 = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input1, null, onlyBoundsOptions);
        input1.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
//        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input1 = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input1, null, bitmapOptions);
        input1.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }


    private void  addWatermarkOnImage(Bitmap sourceImage) throws IOException {

//        imageView.buildDrawingCache();
        Bitmap watermark;

            int w, h;
            Canvas c;
            Paint paint;
            Matrix matrix;
            float scale;
            RectF r;
            w = sourceImage.getWidth();
            h = sourceImage.getHeight();

//            Create the new bitmap
            bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
            // Copy the original bitmap into the new one
            c = new Canvas(bmp);
            c.drawBitmap(sourceImage, 0, 0, paint);
            // Load the watermark
            Drawable drawable = getResources().getDrawable(R.drawable.logo2);

            watermark = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo2);


        if (drawable instanceof BitmapDrawable) {
            watermark = ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        watermark =bitmap;


            // Scale the watermark to be approximately 35% of the source image height
            if(watermark != null)
                scale = (float) (((float) h * 0.4) / (float) watermark.getHeight());
            else
                scale=2;
            // Create the matrix
            matrix = new Matrix();
            matrix.postScale(scale, scale);
            // Determine the post-scaled size of the watermark

            if(watermark !=null)
                r = new RectF(0, 0, watermark.getWidth(), watermark.getHeight());
            else
                r= new RectF(0, 0, 30, 30);
            matrix.mapRect(r);
            // Move the watermark to the bottom right corner
            matrix.postTranslate(w - r.width(), h - r.height());
            // Draw the watermark
            c.drawBitmap(watermark, matrix, paint);
            // Free up the bitmap memory
            watermark.recycle();
//
//        byte[] decodedString = Base64.decode(person_object.getPhoto(),Base64.NO_WRAP);
//        InputStream inputStream  = new ByteArrayInputStream(decodedString);
//        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//        user_image.setImageBitmap(bitmap);
//
            imageView.setImageBitmap(bmp);

         imageUri= getImageUri(this,bmp);

    }

    private Uri getImageUri(Context context, Bitmap inImage) throws IOException {

        FileOutputStream fileOutputStream=null;

        File sdCard = new File(Environment.getExternalStorageState());

        File directory = new File(sdCard.getAbsoluteFile()+"/EchoVector");

        directory.mkdir();
        String imageName=String.format("%d.jpg",System.currentTimeMillis());
        File outFile =new File(directory,imageName);

        Toast.makeText(Activity3_2.this,"Image Saved!",Toast.LENGTH_LONG).show();

        fileOutputStream = new FileOutputStream(outFile);
        inImage.compress(Bitmap.CompressFormat.JPEG,100, fileOutputStream);
        try {
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileOutputStream.close();

        return Uri.fromFile(outFile);

    }
}
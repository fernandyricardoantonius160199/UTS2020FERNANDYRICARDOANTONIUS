package com.example.uts_amub_ti7jm_1711500123_fernandy_ricardo_antonius;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {
    ImageView pic_photo_register_user;
    ImageButton img_btn_register2, img_btn_add_photo;
    EditText ed_hobi, ed_alamat;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();
        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        img_btn_add_photo = findViewById(R.id.img_btn_add_photo);
        img_btn_register2 = findViewById(R.id.img_btn_register2);
        ed_hobi = findViewById(R.id.ed_hobi);
        ed_alamat = findViewById(R.id.ed_alamat);

        img_btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPhoto();
            }
        });

        img_btn_register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key);

                storage = FirebaseStorage.getInstance().getReference()
                        .child("Photousers");

                if (photo_location != null) {
                    final StorageReference storageReference1 =
                            storage.child(System.currentTimeMillis() + "." +
                                    getFileExtension(photo_location));
                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                            reference.getRef().child("hobi").setValue(ed_hobi.getText().toString());
                                            reference.getRef().child("alamat").setValue(ed_alamat.getText().toString());
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Intent gotosuccess = new Intent(RegisterTwoActivity.this, MainActivity.class);
                                            startActivity(gotosuccess);
                                        }
                                    });
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent gotosuccess = new Intent(RegisterTwoActivity.this, MainActivity.class);
                            startActivity(gotosuccess);
                        }
                    });
                }

            }
        });
    }

        String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key = sharedPreferences.getString(username_key_new,"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() !=null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user);
        }
    }
}
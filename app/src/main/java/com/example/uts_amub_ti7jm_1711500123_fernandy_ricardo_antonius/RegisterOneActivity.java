package com.example.uts_amub_ti7jm_1711500123_fernandy_ricardo_antonius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {
    ImageButton img_btn_next;
    EditText ed_username1, ed_password1, ed_email;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);
        ed_username1 = findViewById(R.id.ed_username1);
        ed_password1 = findViewById(R.id.ed_password1);
        ed_email = findViewById(R.id.ed_email);

        img_btn_next = findViewById(R.id.img_btn_next);
        img_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, ed_username1.getText().toString());
                editor.apply();

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(ed_username1.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("username").setValue(ed_username1.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(ed_password1.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(ed_email.getText().toString());
                        dataSnapshot.getRef().child("user_balance").setValue(100000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent gotonextregister = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                startActivity(gotonextregister);
            }
        });
    }
}
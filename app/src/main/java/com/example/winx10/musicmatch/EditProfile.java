package com.example.winx10.musicmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    private DatabaseReference dbreference;
    EditText editName, editLocation, editAge, editPhone, editInstrument;


    public void saveUser(View view){

        SaveUserInfo();
        startActivity(new Intent(this, Home_start.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        editName = (EditText) findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editPhone = findViewById(R.id.editPhone);
        editInstrument = findViewById(R.id.editInstrument);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button backBtn = findViewById(R.id.backBtn);
        Button cancelBtn = findViewById(R.id.cancel_button);

        dbreference = FirebaseDatabase.getInstance().getReference();

    }

    private void SaveUserInfo(){

        String name = editName.getText().toString().trim();
        String age = editAge.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String instrument = editInstrument.getText().toString().trim();

        UserInformation userInformation = new UserInformation(name, age, phone, instrument);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dbreference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this,"Information Saved!",Toast.LENGTH_SHORT).show();

    }


    public void BackActivity(View view){

        Intent backIntent = new Intent (this,Home_start.class);
        startActivity(backIntent);

    }


}

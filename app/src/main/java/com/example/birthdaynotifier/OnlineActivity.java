package com.example.birthdaynotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birthdaynotifier.Database.BirthDateSQLDbHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OnlineActivity extends AppCompatActivity {

    public static final String FIREBASE_USER =
            "privateKeyofData.firebaseUser";
    public static final String FIREBASE_Email =
            "privateKeyofData.firebaseEmail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");


        TextView user_email = findViewById(R.id.textView_online_email);
        TextView backUp_btn = findViewById(R.id.textView_online_backUpButton);
        TextView restore_btn = findViewById(R.id.textView_online_restoreButton);
        TextView clear_btn = findViewById(R.id.textView_online_clearDataButton);
        TextView signOut_btn = findViewById(R.id.textView_online_signOut);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        final String userId = firebaseUser.getUid();
        //Toast.makeText(this,firebaseUser.getUid(),Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        String userEmailIntent = intent.getStringExtra(FIREBASE_Email);
        user_email.setText(userEmailIntent);

        signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });


        backUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<BirthDateSQL> onlineBirthdates = new ArrayList<>() ;
                final int[] counter = {0};
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            BirthDateSQL birthDateSQL = new BirthDateSQL();
                            birthDateSQL.setId(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getId());
                            birthDateSQL.setName(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getName());
                            birthDateSQL.setDay(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getDay());
                            birthDateSQL.setMonth(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getMonth());
                            birthDateSQL.setNotification(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getNotification());

                            onlineBirthdates.add(birthDateSQL);
                            counter[0] +=1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OnlineActivity.this , error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                //----------------------------database inisialization---------------------------------
                final BirthDateSQLDbHelper birthDateSQLDbHelper = BirthDateSQLDbHelper.getInstance(getApplicationContext());
                //sqLiteDatabase = birthDateDbHelper.getWritableDatabase();
                //-------------------------------------------------------------------------------------
                ArrayList<BirthDateSQL> localBirthDates = birthDateSQLDbHelper.getAllBirthDatesList();

                if(counter[0]>=1){
                    for(BirthDateSQL onlinebirthDateSQL : onlineBirthdates){
                        for(BirthDateSQL localBirthDateSQL: localBirthDates){
                            if(onlinebirthDateSQL.getId() == localBirthDateSQL.getId()){
                                if(!onlinebirthDateSQL.getName().equals(localBirthDateSQL.getName())){
                                    
                                }
                            }
                        }
                    }
                }
            }
        });


        restore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
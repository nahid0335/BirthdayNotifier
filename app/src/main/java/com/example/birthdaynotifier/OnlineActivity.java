package com.example.birthdaynotifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birthdaynotifier.Database.BirthDateSQLDbHelper;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static final String EXTRA_DATA =
            "privateKeyofData.data";

    private BirthDateViewModel birthDateViewModel;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        TextView user_email = findViewById(R.id.textView_online_email);
        TextView backUp_btn = findViewById(R.id.textView_online_backUpButton);
        TextView restore_btn = findViewById(R.id.textView_online_restoreButton);
        TextView clear_btn = findViewById(R.id.textView_online_clearDataButton);
        TextView signOut_btn = findViewById(R.id.textView_online_signOut);

        final ProgressBar progressBar = findViewById(R.id.progressBar_online_service);
        //progressBar.setVisibility(View.GONE);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        final String userId = firebaseUser.getUid();
        //Toast.makeText(this,userId,Toast.LENGTH_LONG).show();

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
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<BirthDateSQL> onlineBirthdates = new ArrayList<>() ;
                final int[] counter = {0};
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            BirthDateSQL birthDateSQL = dataSnapshot.child(userId).getValue(BirthDateSQL.class);
/*
                            BirthDateSQL birthDateSQL = new BirthDateSQL();
                            birthDateSQL.setId(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getId());
                            birthDateSQL.setName(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getName());
                            birthDateSQL.setDay(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getDay());
                            birthDateSQL.setMonth(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getMonth());
                            birthDateSQL.setNotification(dataSnapshot.child(userId).getValue(BirthDateSQL.class).getNotification());*/

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

                ArrayList<BirthDateSQL> temp = new ArrayList<>();

                if(counter[0]>=1){
                    for(BirthDateSQL localBirthDateSQL : localBirthDates){
                        boolean status = true;
                        for(BirthDateSQL onlinebirthDateSQL: onlineBirthdates){
                            if(onlinebirthDateSQL.getId() == localBirthDateSQL.getId()){
                                if(!onlinebirthDateSQL.getName().equals(localBirthDateSQL.getName())){
                                    myRef.child(userId).child(Long.toString(onlinebirthDateSQL.getId())).setValue(localBirthDateSQL);
                                }
                                else if(onlinebirthDateSQL.getDay()!=localBirthDateSQL.getDay()){
                                    myRef.child(userId).child(Long.toString(onlinebirthDateSQL.getId())).setValue(localBirthDateSQL);
                                }
                                else if(onlinebirthDateSQL.getMonth()!=localBirthDateSQL.getMonth()){
                                    myRef.child(userId).child(Long.toString(onlinebirthDateSQL.getId())).setValue(localBirthDateSQL);
                                }
                                else if(onlinebirthDateSQL.getNotification()!=localBirthDateSQL.getNotification()){
                                    myRef.child(userId).child(Long.toString(onlinebirthDateSQL.getId())).setValue(localBirthDateSQL);
                                }
                                status = false;
                            }
                        }
                        if(status){
                            temp.add(localBirthDateSQL);
                        }
                    }
                    for(BirthDateSQL birthDateSQL: temp){
                        myRef.child(userId).child(Long.toString(birthDateSQL.getId())).setValue(birthDateSQL);
                    }
                    Toast.makeText(OnlineActivity.this,"All Data has been backup in server .. ",Toast.LENGTH_LONG).show();
                }else{
                    for(BirthDateSQL birthDateSQL: localBirthDates){
                        Toast.makeText(OnlineActivity.this,birthDateSQL.getName(),Toast.LENGTH_LONG).show();
                        myRef.child(userId).child(Long.toString(birthDateSQL.getId())).setValue(birthDateSQL);
                    }
                    Toast.makeText(OnlineActivity.this,"All Data has been backup in server .. ",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


        restore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<BirthDateSQL> onlineBirthdates = new ArrayList<>() ;
                final int[] counter = {0};
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            BirthDateSQL birthDateSQL = dataSnapshot.child(userId).getValue(BirthDateSQL.class);

                            onlineBirthdates.add(birthDateSQL);
                            counter[0] +=1;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OnlineActivity.this , error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                if(counter[0]>0){
                    final BirthDateSQLDbHelper birthDateSQLDbHelper = BirthDateSQLDbHelper.getInstance(OnlineActivity.this);
                    birthDateViewModel = ViewModelProviders.of(OnlineActivity.this).get(BirthDateViewModel.class);

                    for(BirthDateSQL birthDateSQL:onlineBirthdates){
                        birthDateSQLDbHelper.insertBirthDate(birthDateSQL);

                        String nameb = birthDateSQL.getName();
                        int day = birthDateSQL.getDay();
                        int month = birthDateSQL.getMonth();
                        int noti = birthDateSQL.getNotification();

                        boolean notification = noti==1;
                        BirthDate birthDate = new BirthDate(nameb, day, month, notification);


                        birthDateViewModel.insert(birthDate);
                    }
                    Toast.makeText(OnlineActivity.this,"All Data has been restored .. ",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(OnlineActivity.this,"There is no data to restore .. ",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                myRef.child(userId).removeValue();
                Toast.makeText(OnlineActivity.this,"All Data of server has been deleted .. !!",Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
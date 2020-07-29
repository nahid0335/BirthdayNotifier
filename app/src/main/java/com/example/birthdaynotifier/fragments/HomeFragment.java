package com.example.birthdaynotifier.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.AddEditActivity;
import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.BirthDateSQL;
import com.example.birthdaynotifier.Database.BirthDateSQLDbHelper;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.SearchActivity;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;
import com.example.birthdaynotifier.notification.AlertReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private BirthDateViewModel birthDateViewModel;
    private static final int ADD_NEW_REQUEST = 1;
    private static final int EDIT_NEW_REQUEST = 2;


    private SharedPreferences sharedpreferences;
    private static final String sharedPreferenceKey = "BirthdatePrivateKey";
    private static final String sortTitle = "sortTitleKey";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView addNewItem = rootview.findViewById(R.id.imageView_mainHome_add);
        ImageView searchButton = rootview.findViewById(R.id.imageView_mainHome_search);
        final ProgressBar progressBar = rootview.findViewById(R.id.progressBar_homeFragment);
        final Spinner sortList = rootview.findViewById(R.id.spinner_mainHome_sortList);

        sharedpreferences = Objects.requireNonNull(getContext()).getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE);


        //----------------------------topber button ---------------------------------
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);
                startActivityForResult(intent, ADD_NEW_REQUEST);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        //----------------------------topber button---------------------------------


        //----------------------------spinner inisialization---------------------------------
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.mainHome_sortList,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortList.setAdapter(spinnerAdapter);
        //----------------------------spinner inisialization---------------------------------


        //----------------------------spinner set---------------------------------
        if(!sharedpreferences.contains(sortTitle)){
            //Toast.makeText(getContext(),"sharedpreference",Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(sortTitle, "Alphabetic");
            editor.apply();
            sortList.setSelection(spinnerAdapter.getPosition("Alphabetic"));
        }else{
            String sortListName = sharedpreferences.getString(sortTitle,null);
            sortList.setSelection(spinnerAdapter.getPosition(sortListName));
        }
        //----------------------------spinner set---------------------------------


        //----------------------------SQL database inisialization---------------------------------
        final BirthDateSQLDbHelper birthDateSQLDbHelper = BirthDateSQLDbHelper.getInstance(getContext());
        //SQLiteDatabase sqLiteDatabase = birthDateSQLDbHelper.getWritableDatabase();
        //----------------------------SQL database inisialization---------------------------------


        setAlarm();

        //----------------------------recycleview inisialization---------------------------------
        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView_home_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);

        final BirthDateAdapter adapter = new BirthDateAdapter();
        recyclerView.setAdapter(adapter);
        //----------------------------recycleview inisialization---------------------------------


        //----------------------------viewmodel inisialization---------------------------------
        birthDateViewModel = ViewModelProviders.of(this).get(BirthDateViewModel.class);
        birthDateViewModel.getAllBirthDate().observe(getViewLifecycleOwner(), new Observer<List<BirthDate>>() {
            @Override
            public void onChanged(@Nullable List<BirthDate> birthDates) {
                progressBar.setVisibility(View.VISIBLE);
                //update RecyclerView
                String sortListName = sharedpreferences.getString(sortTitle,null);
                sortList.setSelection(spinnerAdapter.getPosition(sortListName));
                assert sortListName != null;
                assert birthDates != null;
                if(sortListName.equals("Date")){
                    Collections.sort(birthDates, new Comparator<BirthDate>() {
                        @Override
                        public int compare(BirthDate t1, BirthDate t2) {
                            if(t1.getMonth()<t2.getMonth()){
                                return -1;
                            }else if(t1.getMonth()>t2.getMonth()){
                                return 1;
                            }else{
                                if(t1.getDay()<t2.getDay()){
                                    return -1;
                                }else{
                                    return 1;
                                }
                            }
                        }
                    });
                }else{
                    Collections.sort(birthDates, new Comparator<BirthDate>() {
                        @Override
                        public int compare(BirthDate birthDate, BirthDate t1) {
                            return birthDate.getName().compareToIgnoreCase(t1.getName());
                        }
                    });
                }
                adapter.submitList(birthDates);

                progressBar.setVisibility(View.GONE);

            }
        });
        //----------------------------viewmodel inisialization---------------------------------


        sortList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                progressBar.setVisibility(View.VISIBLE);
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                List<BirthDate>birthDates;
                birthDates = birthDateViewModel.getAllBirthDate().getValue();
                assert birthDates != null;
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if(selectedItem.equals("Date")){
                    editor.putString(sortTitle, "Date");
                    editor.apply();

                    Collections.sort(birthDates, new Comparator<BirthDate>() {
                        @Override
                        public int compare(BirthDate t1, BirthDate t2) {
                            if(t1.getMonth()<t2.getMonth()){
                                return -1;
                            }else if(t1.getMonth()>t2.getMonth()){
                                return 1;
                            }else{
                                if(t1.getDay()<t2.getDay()){
                                    return -1;
                                }else{
                                    return 1;
                                }
                            }
                        }
                    });
                }else{
                    editor.putString(sortTitle, "Alphabetic");
                    editor.apply();
                    Collections.sort(birthDates, new Comparator<BirthDate>() {
                            @Override
                            public int compare(BirthDate birthDate, BirthDate t1) {
                                return birthDate.getName().compareToIgnoreCase(t1.getName());
                            }
                    });
                }
                adapter.submitList(birthDates);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                birthDateViewModel.delete(adapter.getBirthDateAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(getContext(), "BirthDate deleted", Toast.LENGTH_SHORT).show();

                                BirthDate birthDate = adapter.getBirthDateAt(viewHolder.getAdapterPosition());
                                int id = birthDate.getId();
                                birthDateSQLDbHelper.deleteBirthDate(id);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new BirthDateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BirthDate birthDate) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);

                intent.putExtra(AddEditActivity.EXTRA_ID, birthDate.getId());
                intent.putExtra(AddEditActivity.EXTRA_NAME, birthDate.getName());
                intent.putExtra(AddEditActivity.EXTRA_DAY, birthDate.getDay());
                intent.putExtra(AddEditActivity.EXTRA_MONTH, birthDate.getMonth());
                intent.putExtra(AddEditActivity.EXTRA_NOTIFICATION, birthDate.getNotification());
                startActivityForResult(intent, EDIT_NEW_REQUEST);
            }
        });


        return rootview;
    }

    private void setAlarm() {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1607005, intent, 0);

        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        ComponentName receiver = new ComponentName(getContext(), AlertReceiver.class);
        PackageManager pm = getContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final BirthDateSQLDbHelper birthDateSQLDbHelper = BirthDateSQLDbHelper.getInstance(getContext());
        if (requestCode == ADD_NEW_REQUEST && resultCode == RESULT_OK) {
            //int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            int day = data.getIntExtra(AddEditActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddEditActivity.EXTRA_MONTH, 1);
            boolean notification = data.getBooleanExtra(AddEditActivity.EXTRA_NOTIFICATION,true);

            BirthDate birthDate = new BirthDate(name, day, month, notification);
            birthDateViewModel.insert(birthDate);

            int sqlnotification = (notification) ? 1 : 0;
            BirthDateSQL birthDateSQL = new BirthDateSQL(name, day, month, sqlnotification);
            birthDateSQLDbHelper.insertBirthDate(birthDateSQL);

            Toast.makeText(getContext(), "BirthDay saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_NEW_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "BirthDate can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            int day = data.getIntExtra(AddEditActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddEditActivity.EXTRA_MONTH, 1);
            boolean notification = data.getBooleanExtra(AddEditActivity.EXTRA_NOTIFICATION, true);

            BirthDate birthDate = new BirthDate(name, day, month, notification);
            birthDate.setId(id);
            birthDateViewModel.update(birthDate);

            int sqlnotification = (notification) ? 1 : 0;

            BirthDateSQL birthDateSQL = new BirthDateSQL(name, day, month, sqlnotification);
            birthDate.setId(id);
            birthDateSQLDbHelper.updateBirthDate(id, birthDateSQL);
            Toast.makeText(getContext(), "BirthDay updated", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getContext(), "Birthday not saved", Toast.LENGTH_SHORT).show();
        }
    }

}

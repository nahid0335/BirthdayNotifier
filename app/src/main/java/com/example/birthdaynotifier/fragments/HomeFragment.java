package com.example.birthdaynotifier.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.AddEditActivity;
import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;
import com.example.birthdaynotifier.notification.AlertReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    public static final int ADD_NEW_REQUEST = 1;
    public static final int EDIT_NEW_REQUEST = 2;


    SharedPreferences sharedpreferences;
    public static final String sharedPreferenceKey = "BirthdatePrivateKey";
    public static final String sortTitle = "sortTitleKey";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView addNewItem = rootview.findViewById(R.id.imageView_mainHome_add);
        final Spinner sortList = rootview.findViewById(R.id.spinner_mainHome_sortList);

        sharedpreferences = getContext().getSharedPreferences(sharedPreferenceKey, Context.MODE_PRIVATE);


        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);
                startActivityForResult(intent, ADD_NEW_REQUEST);
            }
        });

        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.mainHome_sortList,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortList.setAdapter(spinnerAdapter);

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

        RecyclerView recyclerView = rootview.findViewById(R.id.recyclerView_home_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);

        final BirthDateAdapter adapter = new BirthDateAdapter();
        recyclerView.setAdapter(adapter);

        birthDateViewModel = ViewModelProviders.of(this).get(BirthDateViewModel.class);
        birthDateViewModel.getAllBirthDate().observe(getViewLifecycleOwner(), new Observer<List<BirthDate>>() {
            @Override
            public void onChanged(@Nullable List<BirthDate> birthDates) {
                //update RecyclerView
                String sortListName = sharedpreferences.getString(sortTitle,null);
                sortList.setSelection(spinnerAdapter.getPosition(sortListName));
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

            }
        });
        //Toast.makeText(getContext(),"home",Toast.LENGTH_LONG).show();


        sortList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = adapterView.getItemAtPosition(i).toString();
                List<BirthDate>birthDates = birthDateViewModel.getAllBirthDate().getValue();
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
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                birthDateViewModel.delete(adapter.getBirthDateAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(getContext(), "BirthDate deleted", Toast.LENGTH_SHORT).show();

                                BirthDate birthDate = adapter.getBirthDateAt(viewHolder.getAdapterPosition());
                                String time = birthDate.getTime();
                                int month = birthDate.getMonth();
                                int day = birthDate.getDay();
                                String name = birthDate.getName();
                                int id = birthDate.getId();

                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                                Date date = null;
                                try {
                                    date = sdf.parse(time);
                                } catch (ParseException e) { }

                                Calendar calendar = Calendar.getInstance();
                                int year;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    LocalDate currentDate = LocalDate.now();
                                    year = currentDate.getYear();
                                }else{
                                    Date today = new Date();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(today);
                                    year = cal.get(Calendar.DAY_OF_YEAR);
                                }
                                calendar.setTime(date);
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH,month-1);
                                calendar.set(Calendar.DAY_OF_MONTH,day);
                                calendar.set(Calendar.SECOND, 0);

                                /*Toast.makeText(getContext(),calendar.get(Calendar.HOUR)+" : "+calendar.get(Calendar.MINUTE)+
                                        " ( "+calendar.get(Calendar.YEAR)+" - "+calendar.get(Calendar.MONTH)+" - "+calendar.get(Calendar.DAY_OF_MONTH)+
                                        " Date : "+calendar.get(Calendar.DATE)+" )",Toast.LENGTH_LONG ).show();*/

                                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getContext(), AlertReceiver.class);
                                intent.putExtra("message",name);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

                                alarmManager.cancel(pendingIntent);



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
                intent.putExtra(AddEditActivity.EXTRA_TIME, birthDate.getTime());
                intent.putExtra(AddEditActivity.EXTRA_DAY, birthDate.getDay());
                intent.putExtra(AddEditActivity.EXTRA_MONTH, birthDate.getMonth());
                intent.putExtra(AddEditActivity.EXTRA_NOTIFICATION, birthDate.getNotification());
                startActivityForResult(intent, EDIT_NEW_REQUEST);
            }
        });


        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            String time = data.getStringExtra(AddEditActivity.EXTRA_TIME);
            int day = data.getIntExtra(AddEditActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddEditActivity.EXTRA_MONTH, 1);
            boolean notificatiion = data.getBooleanExtra(AddEditActivity.EXTRA_NOTIFICATION,true);

            BirthDate birthDate = new BirthDate(name, time, day, month, notificatiion);
            birthDateViewModel.insert(birthDate);


            if(notificatiion){
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                Date date = null;
                try {
                    date = sdf.parse(time);
                } catch (ParseException e) { }
                Calendar calendar = Calendar.getInstance();
                Calendar c = Calendar.getInstance();

                int year;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    year = currentDate.getYear();
                }else{
                    Date today = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(today);
                    year = cal.get(Calendar.YEAR);
                }


                calendar.setTime(date);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month-1);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendar.set(Calendar.SECOND, 0);

                /*Toast.makeText(getContext(),calendar.get(Calendar.HOUR)+" : "+calendar.get(Calendar.MINUTE)+
                        " ( "+calendar.get(Calendar.YEAR)+" - "+calendar.get(Calendar.MONTH)+" - "+calendar.get(Calendar.DAY_OF_MONTH)+
                        " Date : "+calendar.get(Calendar.DATE)+" )",Toast.LENGTH_LONG ).show();*/

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlertReceiver.class);
                intent.putExtra("message",name);
                intent.putExtra("Id",id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

                c.setTime(new Date());
                int numOfDays = c.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.YEAR, 1);
                }

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), DateUtils.YEAR_IN_MILLIS,pendingIntent);

            }

            Toast.makeText(getContext(), "BirthDay saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NEW_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getContext(), "BirthDate can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            String time = data.getStringExtra(AddEditActivity.EXTRA_TIME);
            int day = data.getIntExtra(AddEditActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddEditActivity.EXTRA_MONTH, 1);
            boolean notificatiion = data.getBooleanExtra(AddEditActivity.EXTRA_NOTIFICATION,true);

            BirthDate birthDate = new BirthDate(name, time, day, month, notificatiion);
            birthDate.setId(id);
            birthDateViewModel.update(birthDate);


            if(notificatiion){
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                Date date = null;
                try {
                    date = sdf.parse(time);
                } catch (ParseException e) { }
                Calendar calendar = Calendar.getInstance();
                Calendar c = Calendar.getInstance();

                int year;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    year = currentDate.getYear();
                }else{
                    Date today = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(today);
                    year = cal.get(Calendar.YEAR);
                }


                calendar.setTime(date);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month-1);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendar.set(Calendar.SECOND, 0);

                /*Toast.makeText(getContext(),calendar.get(Calendar.HOUR)+" : "+calendar.get(Calendar.MINUTE)+
                        " ( "+calendar.get(Calendar.YEAR)+" - "+calendar.get(Calendar.MONTH)+" - "+calendar.get(Calendar.DAY_OF_MONTH)+
                        " Date : "+calendar.get(Calendar.DATE)+" )",Toast.LENGTH_LONG ).show();*/

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlertReceiver.class);
                intent.putExtra("message",name);
                intent.putExtra("Id",id);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

                c.setTime(new Date());
                int numOfDays = c.getActualMaximum(Calendar.DAY_OF_YEAR);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, numOfDays);
                }
               // Toast.makeText(getContext(),numOfDays+" ",Toast.LENGTH_LONG).show();

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), DateUtils.YEAR_IN_MILLIS,pendingIntent);

            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                Date date = null;
                try {
                    date = sdf.parse(time);
                } catch (ParseException e) { }

                Calendar calendar = Calendar.getInstance();
                int year;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate currentDate = LocalDate.now();
                    year = currentDate.getYear();
                }else{
                    Date today = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(today);
                    year = cal.get(Calendar.DAY_OF_YEAR);
                }
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month-1);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendar.set(Calendar.SECOND, 0);

                /*Toast.makeText(getContext(),calendar.get(Calendar.HOUR)+" : "+calendar.get(Calendar.MINUTE)+
                        " ( "+calendar.get(Calendar.YEAR)+" - "+calendar.get(Calendar.MONTH)+" - "+calendar.get(Calendar.DAY_OF_MONTH)+
                        " Date : "+calendar.get(Calendar.DATE)+" )",Toast.LENGTH_LONG ).show();*/

                AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getContext(), AlertReceiver.class);
                intent.putExtra("message",name);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

                alarmManager.cancel(pendingIntent);

            }


            Toast.makeText(getContext(), "BirthDay updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Birthday not saved", Toast.LENGTH_SHORT).show();
        }
    }

}

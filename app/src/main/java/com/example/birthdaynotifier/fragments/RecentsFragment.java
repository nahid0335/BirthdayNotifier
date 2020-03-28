package com.example.birthdaynotifier.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentsFragment extends Fragment {
    private BirthDateViewModel birthDateViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recents, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_recents_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);

        final BirthDateAdapter adapter = new BirthDateAdapter();
        recyclerView.setAdapter(adapter);

        birthDateViewModel = ViewModelProviders.of(this).get(BirthDateViewModel.class);
        birthDateViewModel.getAllBirthDate().observe(getViewLifecycleOwner(), new Observer<List<BirthDate>>() {
            @Override
            public void onChanged(@Nullable List<BirthDate> birthDates) {
                //update RecyclerView

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

                ArrayList<BirthDate> futureBirthDates = new ArrayList<BirthDate>();
                ArrayList<BirthDate> pastBirthDates = new ArrayList<BirthDate>();

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                int currentday = calendar.get(Calendar.DAY_OF_MONTH);
                int currentmonth = calendar.get(Calendar.MONTH)+1;
                int len = birthDates.size();
                for(int i=0;i<len;i++){
                    int day = birthDates.get(i).getDay();
                    int month = birthDates.get(i).getMonth();
                    if(month>currentmonth){
                        futureBirthDates.add(birthDates.get(i));
                    }else if(month<currentmonth){
                        pastBirthDates.add(birthDates.get(i));
                    }else{
                        if(day>=currentday){
                            futureBirthDates.add(birthDates.get(i));
                        }else {
                            pastBirthDates.add(birthDates.get(i));
                        }
                    }
                }
                for(BirthDate temp : pastBirthDates){
                    futureBirthDates.add(temp);
                }

                adapter.submitList(futureBirthDates);

            }
        });

        return rootView;
    }
}


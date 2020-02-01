package com.example.birthdaynotifier.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private BirthDateViewModel birthDateViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

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
                //Toast.makeText(getContext(),"update",Toast.LENGTH_LONG).show();
                adapter.setBirthDates(birthDates);
                //Toast.makeText(getContext(),"updatelater",Toast.LENGTH_LONG).show();

            }
        });
        //Toast.makeText(getContext(),"home",Toast.LENGTH_LONG).show();
        return rootview;
    }
}

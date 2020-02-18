package com.example.birthdaynotifier.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.AddActivity;
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

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    private BirthDateViewModel birthDateViewModel;
    public static final int ADD_NEW_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView addNewItem = rootview.findViewById(R.id.imageView_mainHome_add);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(intent, ADD_NEW_REQUEST);
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NEW_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddActivity.EXTRA_NAME);
            String time = data.getStringExtra(AddActivity.EXTRA_TIME);
            int day = data.getIntExtra(AddActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddActivity.EXTRA_MONTH, 1);
            boolean notificatiion = data.getBooleanExtra(AddActivity.EXTRA_NOTIFICATION,true);

            BirthDate birthDate = new BirthDate(name, time, day, month, notificatiion);
            birthDateViewModel.insert(birthDate);

            Toast.makeText(getContext(), "BirthDay saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Birthday not saved", Toast.LENGTH_SHORT).show();
        }
    }
}

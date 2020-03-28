package com.example.birthdaynotifier.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.AddEditActivity;
import com.example.birthdaynotifier.BirthDate;
import com.example.birthdaynotifier.R;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView addNewItem = rootview.findViewById(R.id.imageView_mainHome_add);
        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditActivity.class);
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
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            String time = data.getStringExtra(AddEditActivity.EXTRA_TIME);
            int day = data.getIntExtra(AddEditActivity.EXTRA_DAY, 1);
            int month = data.getIntExtra(AddEditActivity.EXTRA_MONTH, 1);
            boolean notificatiion = data.getBooleanExtra(AddEditActivity.EXTRA_NOTIFICATION,true);

            BirthDate birthDate = new BirthDate(name, time, day, month, notificatiion);
            birthDateViewModel.insert(birthDate);

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

            Toast.makeText(getContext(), "BirthDay updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Birthday not saved", Toast.LENGTH_SHORT).show();
        }
    }
}

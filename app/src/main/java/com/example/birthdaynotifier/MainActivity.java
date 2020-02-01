package com.example.birthdaynotifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.birthdaynotifier.Adapter.BirthDateAdapter;
import com.example.birthdaynotifier.ViewModel.BirthDateViewModel;
import com.example.birthdaynotifier.fragments.HomeFragment;
import com.example.birthdaynotifier.fragments.RecentsFragment;
import com.example.birthdaynotifier.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation_mainMenu_home);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.menuItem_mainmenu_home);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_mainActivity_home,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.menuItem_mainmenu_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.menuItem_mainmenu_recents:
                            selectedFragment = new RecentsFragment();
                            break;
                        case R.id.menuItem_mainmenu_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_mainActivity_home,
                            selectedFragment).commit();

                    return true;
                }
            };
}

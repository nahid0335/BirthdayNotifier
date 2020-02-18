package com.example.birthdaynotifier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.content.Intent.EXTRA_TITLE;

public class AddActivity extends AppCompatActivity {

    public static final String EXTRA_NAME =
            "privateKeyofData.name";
    public static final String EXTRA_TIME =
            "privateKeyofData.time";
    public static final String EXTRA_DAY =
            "privateKeyofData.day";
    public static final String EXTRA_MONTH =
            "privateKeyofData.month";
    public static final String EXTRA_NOTIFICATION =
            "privateKeyofData.notification";

    private TextInputLayout nameTextInputLayout;
    private TimePicker timePicker;
    private NumberPicker dayNumberPicker,monthNumberPicker;
    private RadioGroup notificationRadioGroup;
    private RadioButton radioButton;
    boolean notification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar ();
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close_white_24dp );

        nameTextInputLayout = findViewById(R.id.TextInputLayout_addNew_name);
        timePicker = findViewById(R.id.timePicker);
        dayNumberPicker = findViewById(R.id.numberPicker_day);
        monthNumberPicker = findViewById(R.id.numberPicker_month);
        notificationRadioGroup = findViewById(R.id.radioGroup_addNew_Notification);


        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        setTitle("Add New Birthday");

        timePicker.setIs24HourView(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_addnew, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItem_addNew_save:
                saveBirthday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void saveBirthday() {
        if(!validName()) {
            return;
        }else{
            String name = nameTextInputLayout.getEditText().getText().toString();
            int hour, minute;
            String am_pm;
            if (Build.VERSION.SDK_INT >= 23 ){
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            }
            else{
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }
            if(hour > 12) {
                am_pm = "PM";
                hour = hour - 12;
            }
            else
            {
                am_pm="AM";
            }
            String time = hour+":"+minute+" "+am_pm;
            int day = dayNumberPicker.getValue();
            int month = monthNumberPicker.getValue();


            Intent data = new Intent();
            data.putExtra(EXTRA_NAME, name);
            data.putExtra(EXTRA_TIME, time);
            data.putExtra(EXTRA_DAY, day);
            data.putExtra(EXTRA_MONTH, month);
            data.putExtra(EXTRA_NOTIFICATION, notification);


            setResult(RESULT_OK, data);
            finish();
        }
    }

    private boolean validName(){
        String usernameInput = nameTextInputLayout.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            nameTextInputLayout.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            nameTextInputLayout.setError("Username too long");
            return false;
        } else {
            nameTextInputLayout.setError(null);
            return true;
        }
    }

    public void radioButtonClick(View view) {
        int radioId = notificationRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if(radioButton.getText() == "ON"){
            notification = true;
        }
        else{
            notification = false;
        }
    }
}

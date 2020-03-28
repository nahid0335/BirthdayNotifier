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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.EXTRA_TITLE;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "privateKeyofData.id";
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
    private TextInputEditText nameTextInputEditText;
    private TimePicker timePicker;
    private NumberPicker dayNumberPicker,monthNumberPicker;
    private RadioGroup notificationRadioGroup;
    private RadioButton radioButton;
    boolean notification = true;

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
        nameTextInputEditText = findViewById(R.id.TextInputEditText_addNew_name);
        timePicker = findViewById(R.id.timePicker);
        dayNumberPicker = findViewById(R.id.numberPicker_day);
        monthNumberPicker = findViewById(R.id.numberPicker_month);
        notificationRadioGroup = findViewById(R.id.radioGroup_addNew_Notification);


        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);

        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Birthday");

            nameTextInputEditText.setText(intent.getStringExtra(EXTRA_NAME));
            dayNumberPicker.setValue(intent.getIntExtra(EXTRA_DAY,1));
            monthNumberPicker.setValue(intent.getIntExtra(EXTRA_MONTH,1));
            notification = intent.getBooleanExtra(EXTRA_NOTIFICATION,true);
            if(notification){
                notificationRadioGroup.check(R.id.radioButton_addNew_on);
            }else{
                notificationRadioGroup.check(R.id.radioButton_addNew_off);
            }
            String time = intent.getStringExtra(EXTRA_TIME);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date date = null;
            try {
                date = sdf.parse(time);
            } catch (ParseException e) { }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (Build.VERSION.SDK_INT >= 23 ) {
                timePicker.setHour(calendar.get(Calendar.HOUR));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
            }else{
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
            }

        }else {
            setTitle("Add New Birthday");
        }

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


            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);
            finish();
        }
    }

    private boolean validName(){
        String usernameInput = nameTextInputLayout.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            nameTextInputLayout.setError("Field can't be empty !!");
            return false;
        } else if (usernameInput.length() > 15) {
            nameTextInputLayout.setError("Name is too long !!");
            return false;
        } else if (!((usernameInput.charAt(0)>='A'&& usernameInput.charAt(0)<='Z')||(usernameInput.charAt(0)>='a'&& usernameInput.charAt(0)<='z'))) {
            nameTextInputLayout.setError("Name has to start with alphabet !!");
            return false;
        }else {
            nameTextInputLayout.setError(null);
            return true;
        }
    }

    public void radioButtonClick(View view) {
        int radioId = notificationRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if(radioButton.getText().equals("ON")){
            notification = true;
        }
        else{
            notification = false;
        }
    }
}

package com.example.birthdaynotifier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;


public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ID =
            "privateKeyofData.id";
    public static final String EXTRA_NAME =
            "privateKeyofData.name";
    public static final String EXTRA_DAY =
            "privateKeyofData.day";
    public static final String EXTRA_MONTH =
            "privateKeyofData.month";
    public static final String EXTRA_NOTIFICATION =
            "privateKeyofData.notification";

    private TextInputLayout nameTextInputLayout;
    private NumberPicker dayNumberPicker,monthNumberPicker;
    private RadioGroup notificationRadioGroup;
    boolean notification = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar ();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled ( true );
        actionbar.setHomeAsUpIndicator ( R.drawable.ic_close_white_24dp );

        nameTextInputLayout = findViewById(R.id.TextInputLayout_addNew_name);
        TextInputEditText nameTextInputEditText = findViewById(R.id.TextInputEditText_addNew_name);
        dayNumberPicker = findViewById(R.id.numberPicker_day);
        monthNumberPicker = findViewById(R.id.numberPicker_month);
        notificationRadioGroup = findViewById(R.id.radioGroup_addNew_Notification);


        dayNumberPicker.setMinValue(1);
        dayNumberPicker.setMaxValue(31);
        monthNumberPicker.setMinValue(1);
        monthNumberPicker.setMaxValue(12);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        dayNumberPicker.setValue(day);
        monthNumberPicker.setValue(month);

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
        }else {
            setTitle("Add New Birthday");
        }
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
        if(validName()) {
            String name = Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString();
            int day = dayNumberPicker.getValue();
            int month = monthNumberPicker.getValue();


            Intent data = new Intent();
            data.putExtra(EXTRA_NAME, name);
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
        String usernameInput = Objects.requireNonNull(nameTextInputLayout.getEditText()).getText().toString().trim();

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
        RadioButton radioButton = findViewById(radioId);
        notification = radioButton.getText().equals("ON");
    }
}

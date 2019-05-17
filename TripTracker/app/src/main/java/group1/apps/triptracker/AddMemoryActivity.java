package group1.apps.triptracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class AddMemoryActivity extends Activity {

    private static final String TAG = "AddMemoryActivity";

    private Button mDisplayDate;
    private TextView mTextDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

//    variables voor navigatiebalk
    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        mDisplayDate = (Button) findViewById(R.id.tvDate);
        mTextDate = (TextView) findViewById(R.id.tvDate_text);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddMemoryActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mTextDate.setText(date);
            }
        };

//        Hier begint de code voor het starten van nieuwe activiteit onClick
        navMap = (ImageView) findViewById(R.id.nav_bg_1);
        navMap.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        navMemory = (ImageView) findViewById(R.id.nav_bg_2);
        navMemory.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemory();
            }
        });

        navCamera = (ImageView) findViewById(R.id.nav_bg_3);
        navCamera.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        navProfile = (ImageView) findViewById(R.id.nav_bg_4);
        navProfile.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
    }

    public void openMap() {
    }

    public void openMemory() {
        Intent intent = new Intent(this, MemoryActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void openCamera() {
    }

    public void openProfile() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
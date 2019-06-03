package group1.apps.triptracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddMemoryActivity extends Activity {

    private static final String TAG = "AddMemoryActivity";

    private MemoryDbHelper dbHelper = new MemoryDbHelper(this);

    //    variables voor set date dialog
    private Button mDisplayDate;
    private TextView mTextDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView mThumbnail;
    private ConstraintLayout mLayoutImageThumbnail;
    private ConstraintLayout mLayoutAddPhoto;

    private EditText nameTextInput;
    private EditText locationText_Input;
    private EditText storyTextInput;

    //    variables voor navigatiebalk
    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;
    private Button buttonAddMemory;

    private Bitmap thumbnail;

    //    code voor set date dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        mDisplayDate = (Button) findViewById(R.id.tvDate);
        mTextDate = (TextView) findViewById(R.id.tvDate_text);
        mThumbnail = (ImageView) findViewById(R.id.image_thumbnail);
        mLayoutImageThumbnail = (ConstraintLayout) findViewById(R.id.layout_image_thumbnail);

        nameTextInput = findViewById(R.id.name_text_input);
        locationText_Input = findViewById(R.id.location_text_input);
        storyTextInput = findViewById(R.id.story_text_input);

        thumbnail = getIntent().getParcelableExtra("image_thumbnail");

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
                        year, month, day);
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
        navMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        navMemory = (ImageView) findViewById(R.id.nav_bg_2);
        navMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMemory();
            }
        });

        navCamera = (ImageView) findViewById(R.id.nav_bg_3);
        navCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        navProfile = (ImageView) findViewById(R.id.nav_bg_4);
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        buttonAddMemory = (Button) findViewById(R.id.button_add_memory);
        buttonAddMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMemory();

                finish();
                startActivity(new Intent(AddMemoryActivity.this, MainActivity.class));
            }
        });

        mLayoutAddPhoto = (ConstraintLayout) findViewById(R.id.layout_add_photo);
        mLayoutAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        displayThumbnail();
    }

    public void openMap() {
        Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
        startActivity(intent);
        this.finish();
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

    private void displayThumbnail() {
        if (thumbnail != null) {
            mThumbnail.setImageBitmap(thumbnail);
            mLayoutAddPhoto.setVisibility(View.GONE);
        } else {
            mLayoutImageThumbnail.setVisibility(View.GONE);
        }
    }

    private void saveMemory() {
        if (thumbnail == null) {
            Toast.makeText(this, "Memory not saved: no image specified...", Toast.LENGTH_LONG).show();

            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE, nameTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION, storyTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DATE_ADDED, "1-1-2000");
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE, bitmapToByteArray(thumbnail));
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_LOCATION, locationText_Input.getText().toString());

        db.insert(MemoryContract.MemoryEntry.TABLE_NAME, null, values);
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stream.toByteArray();
    }
}
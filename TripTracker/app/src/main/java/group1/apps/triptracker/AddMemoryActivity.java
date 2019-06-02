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

//IMPORTS VOOR DE LOCATIE

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.SupportMapFragment;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import static com.google.android.apps.gmm.map.util.jni.NativeHelper.context;

public class AddMemoryActivity extends Activity {

    private static final String TAG = "AddMemoryActivity";

//    variables voor set date dialog
    private Button mDisplayDate;
    private TextView mTextDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

//    variables voor navigatiebalk
    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;
    private Button gpsButton;


//variable voor locatie
    private Task mLocation;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

//    code voor set date dialog
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

//        alles hieronder is gecomment zodat we geen errors krijgen. wat we hier willen is dat we de huidige latitude en longitude printen
//        en we willen die lat en long printen als de button button_gps is ingedrukt waarna de
//        text_location_gps wat een textview is veranderd naar de lat en longitude

//        gpsButton = (Button) findViewById(R.id.button_gps);
//        gpsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Task getGPS(){
////                    R.id.text_location_gps = mFusedLocationProviderClient.getLastLocation();
//                }
//            }
//        });


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
}
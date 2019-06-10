package group1.apps.triptracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddMemoryActivity extends FragmentActivity {

    private static final String TAG = "AddMemoryActivity";

    private MemoryDbHelper dbHelper = new MemoryDbHelper(this);

    //    variables voor set date dialog
    private ImageView mThumbnail;
    private ConstraintLayout mLayoutImageThumbnail;
    private ConstraintLayout mLayoutAddPhoto;

    private EditText nameTextInput;
    private EditText storyTextInput;

    private Button buttonAddMemory;

    private Bitmap thumbnail;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;

    private LocationManager locationManager;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;

            Log.d("543665", "onLocationChanged. location: " + currentLocation);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //    code voor set date dialog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 14);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        mThumbnail = (ImageView) findViewById(R.id.image_thumbnail);
        mLayoutImageThumbnail = (ConstraintLayout) findViewById(R.id.layout_image_thumbnail);

        nameTextInput = findViewById(R.id.name_text_input);
        storyTextInput = findViewById(R.id.story_text_input);

        thumbnail = getIntent().getParcelableExtra("image_thumbnail");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

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
                startActivity(new Intent(AddMemoryActivity.this, CameraActivity.class));
            }
        });

        displayThumbnail();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(AddMemoryActivity.this, MainActivity.class));
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

        getLocation();

        // if the location could not be found...
        if (currentLocation == null) {
            // ...try this:
            currentLocation = getLastKnownLocation();

            Log.d("7623492", "currentLocation is null and getLastKnownLocation() is executed");
        }

        // if the location is still null...
        if (currentLocation == null) {
            // ...the memory cannot be saved.
            Toast.makeText(this, "Cannot save memory: location could not be found...", Toast.LENGTH_LONG).show();

            Log.d("7623492", "currentLocation is null and cannot be found...");

            return;
        }

        // at this point, the current location is not null and can be saved in the memory
        Log.d("7623492", "currentLocation has been found: " + currentLocation);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();

        String currentDate = dateFormat.format(date);

        ContentValues values = new ContentValues();
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE, nameTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION, storyTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DATE_ADDED, currentDate);
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE, bitmapToByteArray(thumbnail));

        // get location data
        String strLat = Double.toString(currentLocation.getLatitude());
        String strLong = Double.toString(currentLocation.getLongitude());
        // location is stored as a string, for example: ""
        String strLocation = strLat + "," + strLong;

        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_LOCATION, strLocation);

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

    // finds the current location ans stores the value in the global variable currentLocation
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 14);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                currentLocation = new Location(LocationManager.GPS_PROVIDER);
                currentLocation.setLatitude(location.getLatitude());
                currentLocation.setLatitude(location.getLongitude());
            } else {
                // request the location every 5 seconds
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        return bestLocation;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your gps connection")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
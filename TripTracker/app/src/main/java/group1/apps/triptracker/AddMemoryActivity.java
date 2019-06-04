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

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE, nameTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION, storyTextInput.getText().toString());
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DATE_ADDED, "1-1-2000");
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE, bitmapToByteArray(thumbnail));

        if (currentLocation != null) {
            String strLat = Double.toString(currentLocation.getLatitude());
            String strLong = Double.toString(currentLocation.getLongitude());
            String strLocation = strLat + "," + strLong;

            values.put(MemoryContract.MemoryEntry.COLUMN_NAME_LOCATION, strLocation);
        } else {
            Toast.makeText(this, "Current location could not be found...", Toast.LENGTH_LONG).show();
        }

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
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
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
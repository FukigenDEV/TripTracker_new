package group1.apps.triptracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private ImageView pf_settings;
    private TextView pf_name;
    private TextView pf_biography;
    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;
    private ImageView mscAddMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mscAddMemory = (ImageView) findViewById(R.id.pf_memory_add);
        mscAddMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMemory();
            }
        });

        pf_settings = findViewById(R.id.pf_settings);
        pf_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChangeProfile();
            }
        });

        pf_name = findViewById(R.id.pf_name);
        pf_biography = findViewById(R.id.pf_biography);

        updateProfileInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateProfileInfo();
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

    public void openAddMemory() {
        Intent intent = new Intent(this, AddMemoryActivity.class);
        startActivity(intent);
    }

    public void openChangeProfile() {
        Intent intent = new Intent(this, ChangeProfileActivity.class);
        startActivity(intent);
    }

    public void updateProfileInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String name = sharedPreferences.getString(getString(R.string.profile_name), "Name");
        String biography = sharedPreferences.getString(getString(R.string.profile_biography), "Biography");

        pf_name.setText(name);
        pf_biography.setText(biography);
    }
}

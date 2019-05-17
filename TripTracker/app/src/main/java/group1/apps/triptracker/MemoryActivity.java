package group1.apps.triptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MemoryActivity extends Activity {

    private static final String TAG = "MemoryActivity";

    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

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
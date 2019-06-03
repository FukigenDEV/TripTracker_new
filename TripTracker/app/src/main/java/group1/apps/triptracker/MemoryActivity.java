package group1.apps.triptracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MemoryActivity extends Activity {

    private static final String TAG = "MemoryActivity";

    private ImageView navMap;
    private ImageView navMemory;
    private ImageView navCamera;
    private ImageView navProfile;

    private Button mscAddMemory;
    private ImageView mscShare;

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

        mscAddMemory = (Button) findViewById(R.id.button_new_memory);
        mscAddMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMemory();
            }
        });

        mscShare = (ImageView) findViewById(R.id.memory_share_1);
        mscShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShare();
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

    public void openAddMemory() {
        Intent intent = new Intent(this, AddMemoryActivity.class);
        startActivity(intent);
    }

    public void doShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
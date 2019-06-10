package group1.apps.triptracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private ImageView pf_settings;
    private TextView pf_name;
    private TextView pf_biography;
    private ImageView mscAddMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mscAddMemory = findViewById(R.id.pf_memory_add);
        pf_settings = findViewById(R.id.pf_settings);
        pf_name = findViewById(R.id.pf_name);
        pf_biography = findViewById(R.id.pf_biography);

        mscAddMemory.setOnClickListener(getOnClickListener());
        pf_settings.setOnClickListener(getOnClickListener());

        updateProfileInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateProfileInfo();
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

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == mscAddMemory) {
                    openAddMemory();
                } else if (view == pf_settings) {
                    openChangeProfile();
                }
            }
        };
    }
}

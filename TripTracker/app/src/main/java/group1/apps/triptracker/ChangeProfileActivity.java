package group1.apps.triptracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangeProfileActivity extends AppCompatActivity {

    private ImageView imvPhoto;
    private EditText etName;
    private EditText etBiography;
    private ImageView imvLocation;
    private Button btnChangeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        imvPhoto = findViewById(R.id.imvPhoto);
        etName = findViewById(R.id.etName);
        etBiography = findViewById(R.id.etBiography);
        imvLocation = findViewById(R.id.imvLocation);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);

        btnChangeProfile.setOnClickListener(getOnClickListener());

        setEditTextValues();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setEditTextValues();
    }

    private void saveProfile() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.profile_name), etName.getText().toString());
        editor.putString(getString(R.string.profile_biography), etBiography.getText().toString());
        editor.apply();
    }

    public void setEditTextValues() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String name = sharedPreferences.getString(getString(R.string.profile_name), "Name");
        String biography = sharedPreferences.getString(getString(R.string.profile_biography), "Biography");

        etName.setText(name);
        etBiography.setText(biography);
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnChangeProfile) {
                    saveProfile();
                    finish();
                }
            }
        };
    }
}

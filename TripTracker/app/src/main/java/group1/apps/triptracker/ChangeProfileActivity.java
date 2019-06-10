package group1.apps.triptracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeProfileActivity extends FragmentActivity {

    private EditText etName;
    private EditText etBiography;
    private Button btnChangeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        etName = findViewById(R.id.etName);
        etBiography = findViewById(R.id.etBiography);
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
    }
}

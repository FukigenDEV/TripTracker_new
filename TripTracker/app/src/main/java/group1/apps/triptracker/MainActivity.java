package group1.apps.triptracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends FragmentActivity {

    private MemoryDbHelper dbHelper = new MemoryDbHelper(this);

    private ImageView pf_settings;
    private TextView pf_name;
    private TextView pf_biography;
    private ImageView mscAddMemory;
    private ImageView pf_memory_more;

    private ImageView pf_memory_content_1_1;
    private ImageView pf_memory_content_1_2;
    private ImageView pf_memory_content_1_3;
    private ImageView pf_memory_content_1_4;
    private ImageView pf_memory_content_2_1;
    private ImageView pf_memory_content_2_2;
    private ImageView pf_memory_content_2_3;
    private ImageView pf_memory_content_2_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mscAddMemory = findViewById(R.id.pf_memory_add);
        pf_settings = findViewById(R.id.pf_settings);
        pf_name = findViewById(R.id.pf_name);
        pf_biography = findViewById(R.id.pf_biography);
        pf_memory_more = findViewById(R.id.pf_memory_more);

        pf_memory_content_1_1 = findViewById(R.id.pf_memory_content_1_1);
        pf_memory_content_1_2 = findViewById(R.id.pf_memory_content_1_2);
        pf_memory_content_1_3 = findViewById(R.id.pf_memory_content_1_3);
        pf_memory_content_1_4 = findViewById(R.id.pf_memory_content_1_4);
        pf_memory_content_2_1 = findViewById(R.id.pf_memory_content_2_1);
        pf_memory_content_2_2 = findViewById(R.id.pf_memory_content_2_2);
        pf_memory_content_2_3 = findViewById(R.id.pf_memory_content_2_3);
        pf_memory_content_2_4 = findViewById(R.id.pf_memory_content_2_4);

        mscAddMemory.setOnClickListener(getOnClickListener());
        pf_settings.setOnClickListener(getOnClickListener());
        pf_memory_more.setOnClickListener(getOnClickListener());

        updateProfileInfo();

        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        addMemoryThumbnails();
                    }
                });
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

    public void openMoreMemories() {
        Intent intent = new Intent(this, MemoryActivity.class);
        startActivity(intent);
    }

    public void updateProfileInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        String name = sharedPreferences.getString(getString(R.string.profile_name), "Name");
        String biography = sharedPreferences.getString(getString(R.string.profile_biography), "Biography");

        pf_name.setText(name);
        pf_biography.setText(biography);
    }

    private void addMemoryThumbnails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(MemoryContract.MemoryEntry.TABLE_NAME, null, null, null, null, null, null);

        int counter = 1;

        while (cursor.moveToNext()) {
            if (counter > 8) {
                break;
            }

            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE));
            Bitmap image = byteArrayToBitmap(imageByteArray);

            switch (counter) {
                case 1:
                    pf_memory_content_1_1.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_1_1.getWidth(), pf_memory_content_1_1.getHeight(), false));
                    break;
                case 2:
                    pf_memory_content_1_2.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_1_2.getWidth(), pf_memory_content_1_2.getHeight(), false));
                    break;
                case 3:
                    pf_memory_content_1_3.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_1_3.getWidth(), pf_memory_content_1_3.getHeight(), false));
                    break;
                case 4:
                    pf_memory_content_1_4.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_1_4.getWidth(), pf_memory_content_1_4.getHeight(), false));
                    break;
                case 5:
                    pf_memory_content_2_1.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_2_1.getWidth(), pf_memory_content_2_1.getHeight(), false));
                    break;
                case 6:
                    pf_memory_content_2_2.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_2_2.getWidth(), pf_memory_content_2_2.getHeight(), false));
                    break;
                case 7:
                    pf_memory_content_2_3.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_2_3.getWidth(), pf_memory_content_2_3.getHeight(), false));
                    break;
                case 8:
                    pf_memory_content_2_4.setImageBitmap(Bitmap.createScaledBitmap(image, pf_memory_content_2_4.getWidth(), pf_memory_content_2_4.getHeight(), false));
                    break;
            }

            counter++;
        }

        cursor.close();
    }

    private Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == mscAddMemory) {
                    openAddMemory();
                } else if (view == pf_settings) {
                    openChangeProfile();
                } else if (view == pf_memory_more) {
                    openMoreMemories();
                }
            }
        };
    }
}

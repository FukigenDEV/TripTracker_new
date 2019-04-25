package group1.apps.triptracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView btnAddMemory;
    private MemoryDbHelper mMemoryDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddMemory = findViewById(R.id.btnAddMemory);

        mMemoryDbHelper = new MemoryDbHelper(this);

        btnAddMemory.setOnClickListener(getOnClickListener());

        insertTestValues();
        retrieveTestValues();
    }

    @Override
    protected void onDestroy() {
        mMemoryDbHelper.close();

        super.onDestroy();
    }

    private void insertTestValues() {
        SQLiteDatabase db = mMemoryDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE, "Memory title");
        values.put(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION, "Memory description");

        db.insert(MemoryContract.MemoryEntry.TABLE_NAME, null, values);
    }

    private List<String> retrieveTestValues() {
        SQLiteDatabase db = mMemoryDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MemoryContract.MemoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        List<Long> itemIds = new ArrayList<>();
        List<String> itemTitles = new ArrayList<>();

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MemoryContract.MemoryEntry._ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE));

            itemIds.add(itemId);
            itemTitles.add(itemTitle);
        }

        cursor.close();

        return itemTitles;
    }

    private void deleteTableData() {
        SQLiteDatabase db = mMemoryDbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + MemoryContract.MemoryEntry.TABLE_NAME);
    }

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnAddMemory) {
                    startActivity(new Intent(MainActivity.this, MemoryActivity.class));
                }
            }
        };
    }
}

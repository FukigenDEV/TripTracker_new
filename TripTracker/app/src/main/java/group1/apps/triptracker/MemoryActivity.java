package group1.apps.triptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemoryActivity extends FragmentActivity {

    private static final String TAG = "MemoryActivity";

    private MemoryDbHelper dbHelper = new MemoryDbHelper(this);

    private LinearLayout llScrollHolder;

    private Button mscAddMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        mscAddMemory = (Button) findViewById(R.id.button_new_memory);
        mscAddMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMemory();
            }
        });

        llScrollHolder = (LinearLayout) findViewById(R.id.ll_scroll_holder);

        displayMemories();
    }

    public void openAddMemory() {
        Intent intent = new Intent(this, AddMemoryActivity.class);
        startActivity(intent);
    }

    private void c() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL(MemoryContract.SQL_CREATE_MEMORIES);
    }

    private void d() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL(MemoryContract.SQL_DELETE_MEMORIES);
    }

    private void displayMemories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MemoryContract.MemoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION));
            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE));
            Bitmap image = byteArrayToBitmap(imageByteArray);

            ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.memory_item, null, false);

            ImageView imageView = (ImageView) view.getChildAt(1);
            imageView.setImageBitmap(image);

            ConstraintLayout constraintLayoutChild = (ConstraintLayout) view.getChildAt(2);

            TextView txvTitle = (TextView) constraintLayoutChild.getChildAt(0);
            txvTitle.setText(title);

            NestedScrollView nestedScrollView = (NestedScrollView) constraintLayoutChild.getChildAt(1);

            TextView txvDescription = (TextView) nestedScrollView.getChildAt(0);
            txvDescription.setText(description);

            llScrollHolder.addView(view);
        }

        cursor.close();
    }

    private Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
package group1.apps.triptracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MemoryActivity extends FragmentActivity {

    private MemoryDbHelper dbHelper = new MemoryDbHelper(this);

    private LinearLayout llScrollHolder;
    private Button mscAddMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        mscAddMemory = findViewById(R.id.button_new_memory);
        llScrollHolder = findViewById(R.id.ll_scroll_holder);

        mscAddMemory.setOnClickListener(getOnClickListener());

        displayMemories();
    }

    public void openAddMemory() {
        Intent intent = new Intent(this, AddMemoryActivity.class);
        startActivity(intent);
    }

    private void createTable() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL(MemoryContract.SQL_CREATE_MEMORIES);
    }

    private void deleteTable() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL(MemoryContract.SQL_DELETE_MEMORIES);
    }

    private void displayMemories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(MemoryContract.MemoryEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DATE));

            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE));
            Bitmap image = byteArrayToBitmap(imageByteArray);

            ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.memory_item, null, false);

            ImageView imageView = (ImageView) view.getChildAt(1);
            imageView.setImageBitmap(image);

            ConstraintLayout constraintLayoutChild = (ConstraintLayout) view.getChildAt(2);

            TextView txvTitle = (TextView) constraintLayoutChild.getChildAt(0);
            txvTitle.setText(title);

            ScrollView scrollView = (ScrollView) constraintLayoutChild.getChildAt(1);

            LinearLayout linearLayout = (LinearLayout) scrollView.getChildAt(0);

            TextView txvDescription = (TextView) linearLayout.getChildAt(0);
            txvDescription.setText(description);

            TextView txvDate = (TextView) linearLayout.getChildAt(1);
            txvDate.setText(date);

            llScrollHolder.addView(view);
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
                }
            }
        };
    }
}
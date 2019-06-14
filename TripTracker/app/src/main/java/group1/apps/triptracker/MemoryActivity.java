package group1.apps.triptracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class MemoryActivity extends FragmentActivity {
    private static final int REQUEST_WRITE = 1;

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

        if (ContextCompat.checkSelfPermission(MemoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // ask camera permission
            ActivityCompat.requestPermissions(MemoryActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
            // if the permission is granted
        } else if (ContextCompat.checkSelfPermission(MemoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // open camera
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
            String id = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DATE));

            byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE));
            Bitmap image = byteArrayToBitmap(imageByteArray);

            ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.memory_item, null, false);

            ImageView imageView = (ImageView) view.getChildAt(1);
            imageView.setImageBitmap(image);

            ConstraintLayout constraintLayoutChild = (ConstraintLayout) view.getChildAt(2);

            NestedScrollView nestedScrollView = (NestedScrollView) constraintLayoutChild.getChildAt(0);

            LinearLayout linearLayout = (LinearLayout) nestedScrollView.getChildAt(0);

            TextView txvTitle = (TextView) linearLayout.getChildAt(0);
            txvTitle.setText(title);

            TextView txvDescription = (TextView) linearLayout.getChildAt(1);
            txvDescription.setText(description);

            TextView txvDate = (TextView) linearLayout.getChildAt(2);
            txvDate.setText(date);

            TextView txvShare = (TextView) linearLayout.getChildAt(3);
            txvShare.setOnClickListener(getShareButtonOnClickListener());
            txvShare.setTag(id);

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

    private View.OnClickListener getShareButtonOnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView clickedTextView = (TextView) view;
                String tag = clickedTextView.getTag().toString();

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(MemoryContract.MemoryEntry.TABLE_NAME, null, null, null, null, null, null);

                // Go through every row
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry._ID));

                    // If the id matches the TextView's tag, we found the right memory
                    if (id.equals(tag)) {
                        String title = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_TITLE));
                        String description = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DESCRIPTION));
                        String date = cursor.getString(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_DATE));

                        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex(MemoryContract.MemoryEntry.COLUMN_NAME_IMAGE));
                        Bitmap image = byteArrayToBitmap(imageByteArray);

                        // You can do something with the title, description, date and image here...
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, description);
                        sendIntent.setType("*/*");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(MemoryActivity.this, image));
                        startActivity(sendIntent);

                        // Memory has been found, so we break the loop
                        break;
                    }
                }

                cursor.close();
            }
        };
    }
}
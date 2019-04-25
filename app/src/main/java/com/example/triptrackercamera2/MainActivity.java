package com.example.triptrackercamera2;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import static android.os.Environment.DIRECTORY_PICTURES;

public class MainActivity extends AppCompatActivity {

    private Button btnTakePic;
    private ImageView imgPhoto;

    private static final int Image_Capture_Code = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTakePic =(Button)findViewById(R.id.btnPicture);
        imgPhoto = (ImageView) findViewById(R.id.capturedImage);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Intent.putExtra(een URI?);//ensure that the image is of high quality
                startActivityForResult(Intent,Image_Capture_Code);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgPhoto.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
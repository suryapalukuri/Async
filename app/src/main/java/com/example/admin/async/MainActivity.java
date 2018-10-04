package com.example.admin.async;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ImageView imageView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            imageView = findViewById(R.id.image1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


        if (checkPermission())
        {
            Intent i=new Intent( Intent.ACTION_SEND );
            i.setData(Uri.parse("mailto:p.suryateja129@gmail.com"));
            i.setType("image/jpg");
            String imagePath = Environment.getExternalStorageDirectory()
                    + "/name.jpg";
            File imageFileToShare = new File(imagePath);
            Uri uri = Uri.fromFile(imageFileToShare);
            i.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(i,"share Image") );
        }

        else{

            requestPermissions();

        }
                }
            });
            String url = "https://www.cinematografo.it/wp-content/uploads/2016/03/Kung-Fu-Panda-3.jpg";
            new DownloadImage().execute(url);
            //Picasso.get().load(url).into(imageView);
        }


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

    protected void onPreExecute(){
            super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Downloading");
                progressDialog.setMessage("Loading");
                progressDialog.setIndeterminate(false);
                progressDialog.show();
                }
        @Override
        protected Bitmap doInBackground(String... url) {
            String imageUrl= url[0];
            Bitmap bitmap=null;
            try {
                InputStream inputStream = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                File storagePath = Environment.getExternalStorageDirectory();
                OutputStream outputStream = new FileOutputStream(new File(storagePath, "name.jpg"));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
       protected void onPostExecute(Bitmap result) {
           imageView.setImageBitmap(result);
           progressDialog.dismiss();
       }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkPermission() {
        int storage= ContextCompat.checkSelfPermission(MainActivity.this,WRITE_EXTERNAL_STORAGE);
        if (storage== PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;

        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case 100:

                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED

                        )

                    Log.e( "value","Permission Granted,Now you can use local Drive" );

                else

                    Log.e( "value","Permissions Denied,You cannot use local Drive" );

                break;

        }

    }

}

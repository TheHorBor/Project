package com.example.viber;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songList = new ArrayList<>();

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        TextView username = findViewById(R.id.editTextTextEmailAddress);
        TextView password = findViewById(R.id.editTextTextPassword);

        Button loginbtn =  findViewById(R.id.loginbtn);

        //מעבר בין המסכים בעזרת כפתור הlogin

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
       // button = (Button)findViewById(R.id.loginbtn);
       // button.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View view) {
                ;
          //  }
      //  });













        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(MainActivity.this,"LOGIN SUCCEFUL",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(MainActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        noMusicTextView = findViewById(R.id.no_playlist);

        if(checkPermission() == false){
            requestPermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection , selection,null , null);
        while(cursor.moveToNext()){
            AudioModel songData = new AudioModel(cursor.getString(1) ,cursor.getString(0),cursor.getString(2));
            if(new File(songData.getPath()).exists());
            songList.add(songData);
        }

        if(songList.size() == 0){
            noMusicTextView.setVisibility(View.VISIBLE);
        }else
        {
            //recyclerview
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songList,getApplicationContext()));
        }
    }
    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else return false;
    }
    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this,"Read Permission Is Required, Please Allow It From Settings",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }
}
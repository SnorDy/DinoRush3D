package com.example.dinorush3d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.jme3.math.ColorRGBA;

public class LaunchActivity extends AppCompatActivity {
    private Button btn_start,btn_quit,sound_btn;


    private boolean sound_on=true;
    private MediaPlayer menu_music;

    @Override
    protected void onStart() {
        super.onStart();
        menu_music.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        menu_music.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu_music = MediaPlayer.create(getApplicationContext(),R.raw.menu_music);
        setContentView(R.layout.launch_activity);
        getSupportActionBar().hide();
        menu_music.setLooping(true);
        menu_music.start();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#323232"));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        btn_start=findViewById(R.id.start_btn);
        btn_quit= findViewById(R.id.quit_btn);
        sound_btn=findViewById(R.id.sound_btn);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this,StartActivity.class);
                startActivity(intent);
                menu_music.stop();
                finishAffinity();


            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_music.stop();
                finishAffinity();
            }
        });
        sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound_on){
                    menu_music.pause();
                    sound_on = false;
                    sound_btn.setBackground(getResources().getDrawable(R.drawable.no_sound_icon));
                }
                else{
                    menu_music.start();
                    sound_on = true;
                    sound_btn.setBackground(getResources().getDrawable(R.drawable.sound_on_icon));}
            }
        });


    }
}
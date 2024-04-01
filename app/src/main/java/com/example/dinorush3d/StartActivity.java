package com.example.dinorush3d;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jme3.math.ColorRGBA;
import com.jme3.view.surfaceview.JmeSurfaceView;

import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {
    private TextView scoreView,max_scoreView;
    private ImageButton restart_btn,home_btn;
    private  Thread thread;
    private int screenHeight,screenWidth;
    private JmeSurfaceView jsv;
    private  Game game;
    private boolean game_over_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Log.d("LAUNCH","WORKING");

        Typeface tf = ResourcesCompat.getFont(this, R.font.energydimension);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#323232"));
        }

        jsv=new JmeSurfaceView(this);
        game =new Game();
        jsv.setLegacyApplication(game);
        setContentView(jsv);
        jsv.startRenderer(0);
        screenHeight = jsv.getHeight();
        screenWidth=jsv.getWidth();

        Log.d("SCREEN",screenHeight+ " "+ screenWidth);

        scoreView = new TextView(jsv.getContext());
        scoreView.setText("0");
        scoreView.setTextColor(Color.parseColor("#00adb5"));
        scoreView.setHeight(150);
        scoreView.setWidth(500);
        scoreView.setPadding(40,10,0,0);
        scoreView.setTypeface(tf);
        scoreView.setTextSize(24);


        max_scoreView = new TextView(jsv.getContext());
        max_scoreView.setText(game.getMaxScore()+"");
        max_scoreView.setTextColor(Color.parseColor("#00adb5"));
        max_scoreView.setHeight(150);
        max_scoreView.setWidth(500);
        max_scoreView.setPadding(40,80,0,0);
        max_scoreView.setTypeface(tf);
        max_scoreView.setTextSize(24);

        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                scoreView.setText(game.getScore()+" ");

                                if (max_scoreView.getText().equals("0")||game.IsGameOver()&&!game_over_flag){
                                max_scoreView.setText("max: "+ game.getMaxScore()+"");
                                    screenHeight = jsv.getHeight();
                                    screenWidth=jsv.getWidth();}
                                if (game.IsGameOver()&&!game_over_flag){

                                    try {
                                        create_buttons();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    game_over_flag=true;
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();


//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(new FirstFragment(),"FirstFragment");
//        ft.commit();
        addContentView(scoreView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addContentView(max_scoreView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));




    }
    public  void create_buttons() throws InterruptedException {
        restart_btn = new ImageButton(jsv.getContext());


        restart_btn.setImageResource(R.drawable.restart_icon);
        restart_btn.setBackgroundColor(Color.TRANSPARENT);
        restart_btn.setScaleX(0.8f);
        restart_btn.setScaleY(0.8f);
        restart_btn.setY(screenHeight/2-200);
        restart_btn.setX(screenWidth/2-300);

        home_btn = new ImageButton(jsv.getContext());


        home_btn.setImageResource(R.drawable.home_icon);
        home_btn.setBackgroundColor(Color.TRANSPARENT);
        home_btn.setScaleX(0.8f);
        home_btn.setScaleY(0.8f);
        home_btn.setY(screenHeight/2-200);
        home_btn.setX(screenWidth/2-100);

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_over_flag=false;
                scoreView.setText("0");
                restart_btn.setVisibility(View.GONE);
                home_btn.setVisibility(View.GONE);
                game.setNew_game_flag(true);
            }
        });
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsv.destroy();

                Intent intent = new Intent(StartActivity.this,LaunchActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


        addContentView(restart_btn,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addContentView(home_btn,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

}
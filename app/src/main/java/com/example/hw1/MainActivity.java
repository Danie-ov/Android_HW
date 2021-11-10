package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY = 1000;
    private static final int NUM_OF_OBSTACLES = 4;

    private int clock = 10;
    private Timer timer;

    private ImageButton main_BTN_right;
    private ImageButton main_BTN_left;

    private ImageView main_IMG_car_Right;
    private ImageView main_IMG_car_Left;
    private ImageView main_IMG_car_Center;

    private ImageView[][] main_IMGVW_obstacles;

    private ImageView[] heart_IMVW;

    private int position = 1;
    private int num_of_lives = 3;
    private int num_of_barrier_col = 0;
    private int rand_road = (int)(Math.random()*3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initButtons();

        main_IMGVW_obstacles[rand_road][num_of_barrier_col].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    private void start() {
        startTicker();
    }

    private void createRandomBarriers() {

        main_IMGVW_obstacles[rand_road][num_of_barrier_col].setVisibility(View.INVISIBLE);
        num_of_barrier_col = (num_of_barrier_col + 1) % NUM_OF_OBSTACLES;
        if(num_of_barrier_col == 0)
            rand_road = (int)(Math.random()*3);

        main_IMGVW_obstacles[rand_road][num_of_barrier_col].setVisibility(View.VISIBLE);

        if(main_IMGVW_obstacles[position][NUM_OF_OBSTACLES-1].getVisibility() == View.VISIBLE){
            num_of_lives--;
            heart_IMVW[num_of_lives].setVisibility(View.INVISIBLE);
            if(num_of_lives != 0){
                toast("Oops");
                vibrate();
            }
            else{
                gameOver();
            }
        }
    }

    private void gameOver(){
        stopTicker();
        toast("You lose!");
        vibrate();
        Intent myIntent = getIntent();
        finish();
        startActivity(myIntent);

    }

    private void stop() {
        stopTicker();
    }

    private void initButtons() {

        main_BTN_right.setOnClickListener(v -> moveCarRight());
        main_BTN_left.setOnClickListener(v -> moveCarLeft());
    }

    private void findViews() {

        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);

        main_IMG_car_Left = findViewById(R.id.main_IMG_Car_Left);
        main_IMG_car_Center = findViewById(R.id.main_IMG_Car_Center);
        main_IMG_car_Right = findViewById(R.id.main_IMG_Car_Right);

        main_IMGVW_obstacles = new ImageView[][]{
                {findViewById(R.id.main_VW_Left_obstacle1),
                        findViewById(R.id.main_VW_Left_obstacle2),
                        findViewById(R.id.main_VW_Left_obstacle3),
                        findViewById(R.id.main_VW_Left_obstacle4)},
                {findViewById(R.id.main_VW_Center_obstacle1),
                        findViewById(R.id.main_VW_Center_obstacle2),
                        findViewById(R.id.main_VW_Center_obstacle3),
                        findViewById(R.id.main_VW_Center_obstacle4)},
                {findViewById(R.id.main_VW_Right_obstacle1),
                        findViewById(R.id.main_VW_Right_obstacle2),
                        findViewById(R.id.main_VW_Right_obstacle3),
                        findViewById(R.id.main_VW_Right_obstacle4)}
        };

        heart_IMVW = new ImageView[]{findViewById(R.id.heart1_IMVW),
                findViewById(R.id.heart2_IMVW),
                findViewById(R.id.heart3_IMVW)
        };
    }

    public void moveCarRight(){
        switch (position) {
            case 1:
                main_IMG_car_Center.setVisibility(View.INVISIBLE);
                main_IMG_car_Right.setVisibility(View.VISIBLE);
                position++;
                break;

            case 0:
                main_IMG_car_Center.setVisibility(View.VISIBLE);
                main_IMG_car_Left.setVisibility(View.INVISIBLE);
                position++;
                break;
        }
    }

    public void moveCarLeft(){
        switch (position) {
            case 1:
                main_IMG_car_Center.setVisibility(View.INVISIBLE);
                main_IMG_car_Left.setVisibility(View.VISIBLE);
                position--;
                break;

            case 2:
                main_IMG_car_Center.setVisibility(View.VISIBLE);
                main_IMG_car_Right.setVisibility(View.INVISIBLE);
                position--;
                break;
        }
    }

    public void startTicker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    createRandomBarriers();
                });
            }
        }, 1000, DELAY);
    }

    public void stopTicker() {
        timer.cancel();
    }

    public void vibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
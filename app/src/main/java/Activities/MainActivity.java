package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Models.Record;
import Models.TopRecords;
import Utiles.Constants;
import Utiles.SharedPrefs;

import com.example.hw1.R;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Constants {

    private Timer timer;
    private int speed = REGULAR_DELAY;

    private double lat = 0.0;
    private double lon = 0.0;

    public String playerName = "";

    private Boolean isSensorOn = false;

    private TextView main_TXV_dollars;
    private int score = 0;

    private TopRecords topRecords;

    private ImageButton main_BTN_right;
    private ImageButton main_BTN_left;
    private Button main_BTN_newGame;

    private ImageView[] main_IMG_car;

    private ImageView[][] main_IMGVW_obstacles;
    private int[][] randNumbers = {{-1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1},
                                    {-1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1},
                                    {-1, -1, -1, -1, -1}, {-1, -1, -1, -1, -1}};

    private ImageView[] heart_IMVW;

    private int position, lastPosition = 0;
    private int num_of_lives = 3;

    private SensorManager sensorManager;
    private Sensor sensor;
    private final double MOVE_RIGHT_LIMIT = 1.5;
    private final double MOVE_LEFT_LIMIT = -1.5;
    private double x;
    private double y;
    private double z;
    private Handler handler;
    private Runnable runnable;
    private Boolean isHandlerStop = false;
    private Boolean lock = true;

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            x = (int) event.values[0];
            y = (int) event.values[1];
            z = (int) event.values[2];
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        playerName = bundle.getString(PLAYER_NAME);
        isSensorOn = bundle.getBoolean(SENSOR_TYPE);
        lat = bundle.getDouble(LATITUDE);
        lon = bundle.getDouble(LONGITUDE);

        findViews();
        initSensor();
        initHandler();

        if (isSensorOn) {
            main_BTN_right.setVisibility(View.INVISIBLE);
            main_BTN_left.setVisibility(View.INVISIBLE);
            sensorActivate(MOVE_RIGHT);
        }else{
            initButtons();
        }
    }

    private void initHandler() {
        if (isSensorOn) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    double currentX = x;
                    if (currentX < MOVE_RIGHT_LIMIT)
                        sensorActivate(MOVE_RIGHT);
                    if (currentX > MOVE_LEFT_LIMIT)
                        sensorActivate(MOVE_LEFT);
                    double currentY = y;
                    if (currentY > 1)
                        speed = FAST_DELAY;
                    if (currentY < -1)
                        speed = EASY_DELAY;

                    handler.postDelayed(this, 400);
                    if (isHandlerStop)
                        handler.removeCallbacks(runnable);
                }
            };
            handler.post(runnable);
        }
    }

    private void sensorActivate(int direction) {

        int nextPosition = 0;

        if (direction == 1) {
            nextPosition = position + 1;
            if (nextPosition == 4)
                nextPosition = 3;

            main_IMG_car[position].setVisibility(View.INVISIBLE);
            main_IMG_car[nextPosition].setVisibility(View.VISIBLE);

        } else {
            nextPosition = position - 1;
            if (nextPosition == -1)
                nextPosition = 0;

            main_IMG_car[position].setVisibility(View.INVISIBLE);
            main_IMG_car[nextPosition].setVisibility(View.VISIBLE);
        }
        lastPosition = position;
        position = nextPosition;
    }

    private void initSensor() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void findViews() {

        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        main_BTN_newGame = findViewById(R.id.main_BTN_newGame);

        main_IMG_car = new ImageView[]{findViewById(R.id.main_IMG_Car_mostLeft),
                findViewById(R.id.main_IMG_Car_Left),
                findViewById(R.id.main_IMG_Car_Center),
                findViewById(R.id.main_IMG_Car_Right),
                findViewById(R.id.main_IMG_Car_mostRight)
        };

        main_IMGVW_obstacles = new ImageView[][]{
                {findViewById(R.id.main_VW_mostLeft_obstacle1),
                        findViewById(R.id.main_VW_Left_obstacle1),
                        findViewById(R.id.main_VW_Center_obstacle1),
                        findViewById(R.id.main_VW_Right_obstacle1),
                        findViewById(R.id.main_VW_mostRight_obstacle1)},
                {findViewById(R.id.main_VW_mostLeft_obstacle2),
                        findViewById(R.id.main_VW_Left_obstacle2),
                        findViewById(R.id.main_VW_Center_obstacle2),
                        findViewById(R.id.main_VW_Right_obstacle2),
                        findViewById(R.id.main_VW_mostRight_obstacle2)},
                {findViewById(R.id.main_VW_mostLeft_obstacle3),
                        findViewById(R.id.main_VW_Left_obstacle3),
                        findViewById(R.id.main_VW_Center_obstacle3),
                        findViewById(R.id.main_VW_Right_obstacle3),
                        findViewById(R.id.main_VW_mostRight_obstacle3)},
                {findViewById(R.id.main_VW_mostLeft_obstacle4),
                        findViewById(R.id.main_VW_Left_obstacle4),
                        findViewById(R.id.main_VW_Center_obstacle4),
                        findViewById(R.id.main_VW_Right_obstacle4),
                        findViewById(R.id.main_VW_mostRight_obstacle4)},
                {findViewById(R.id.main_VW_mostLeft_obstacle5),
                        findViewById(R.id.main_VW_Left_obstacle5),
                        findViewById(R.id.main_VW_Center_obstacle5),
                        findViewById(R.id.main_VW_Right_obstacle5),
                        findViewById(R.id.main_VW_mostRight_obstacle5)},
                {findViewById(R.id.main_VW_mostLeft_obstacle6),
                        findViewById(R.id.main_VW_Left_obstacle6),
                        findViewById(R.id.main_VW_Center_obstacle6),
                        findViewById(R.id.main_VW_Right_obstacle6),
                        findViewById(R.id.main_VW_mostRight_obstacle6)}
        };

        main_TXV_dollars = findViewById(R.id.main_TXV_dollars);

        heart_IMVW = new ImageView[]{findViewById(R.id.heart1_IMVW),
                findViewById(R.id.heart2_IMVW),
                findViewById(R.id.heart3_IMVW)
        };

        for(int i=0; i < main_IMG_car.length; i++)
            main_IMG_car[i].setVisibility(View.INVISIBLE);

        main_IMG_car[2].setVisibility(View.VISIBLE);
        position = 2;

        for (int i = 0; i < main_IMGVW_obstacles.length; i++) {
            for (int j = 0; j < main_IMGVW_obstacles[i].length; j++) {
                main_IMGVW_obstacles[i][j].setVisibility(View.INVISIBLE);
            }
        }
        main_IMGVW_obstacles[0][0].setVisibility(View.VISIBLE);

        main_BTN_newGame.setOnClickListener(v -> newGame());

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accSensorEventListener);
    }

    private void initButtons() {

        main_BTN_right.setOnClickListener(v -> moveCarRight());
        main_BTN_left.setOnClickListener(v -> moveCarLeft());
    }

    public void moveCarRight(){
        switch (position) {
            case 0:
                main_IMG_car[0].setVisibility(View.INVISIBLE);
                main_IMG_car[1].setVisibility(View.VISIBLE);
                position++;
                break;

            case 1:
                main_IMG_car[1].setVisibility(View.INVISIBLE);
                main_IMG_car[2].setVisibility(View.VISIBLE);
                position++;
                break;

            case 2:
                main_IMG_car[2].setVisibility(View.INVISIBLE);
                main_IMG_car[3].setVisibility(View.VISIBLE);
                position++;
                break;

            case 3:
                main_IMG_car[3].setVisibility(View.INVISIBLE);
                main_IMG_car[4].setVisibility(View.VISIBLE);
                position++;
                break;
        }
    }

    public void moveCarLeft(){
        switch (position) {
            case 1:
                main_IMG_car[1].setVisibility(View.INVISIBLE);
                main_IMG_car[0].setVisibility(View.VISIBLE);
                position--;
                break;

            case 2:
                main_IMG_car[1].setVisibility(View.VISIBLE);
                main_IMG_car[2].setVisibility(View.INVISIBLE);
                position--;
                break;

            case 3:
                main_IMG_car[2].setVisibility(View.VISIBLE);
                main_IMG_car[3].setVisibility(View.INVISIBLE);
                position--;
                break;

            case 4:
                main_IMG_car[3].setVisibility(View.VISIBLE);
                main_IMG_car[4].setVisibility(View.INVISIBLE);
                position--;
                break;
        }
    }

    private void newGame() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
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
        gameAction();
    }

    private void startGame() {

        checkCrash();

        for (int i = main_IMGVW_obstacles.length -2 ; i >= 0 ; i--){
            for (int j = 0 ; j < main_IMGVW_obstacles[i].length ; j++){
                if(randNumbers[i][j] == BARRIER){
                    main_IMGVW_obstacles[i][j].setVisibility(View.INVISIBLE);
                    randNumbers[i][j] = EMPTY;
                    main_IMGVW_obstacles[i+1][j].setImageResource(R.drawable.barrier);
                    main_IMGVW_obstacles[i+1][j].setVisibility(View.VISIBLE);
                    randNumbers[i+1][j] = BARRIER;
                }
                else if(randNumbers[i][j] == COIN){
                    main_IMGVW_obstacles[i][j].setVisibility(View.INVISIBLE);
                    randNumbers[i][j] = EMPTY;
                    main_IMGVW_obstacles[i+1][j].setImageResource(R.drawable.dollar);
                    main_IMGVW_obstacles[i+1][j].setVisibility(View.VISIBLE);
                    randNumbers[i+1][j] = COIN;
                }
                else{
                    main_IMGVW_obstacles[i][j].setVisibility(View.INVISIBLE);
                    randNumbers[i+1][j] = EMPTY;
                }
            }
        }
        int rand = (int)(Math.random()*10);
        int index = (int)(Math.random()*5);
        locateBarriers(rand, index);
   }

    private void locateBarriers(int rand, int index) {

        if(rand <= 6)//barrier
        {
            main_IMGVW_obstacles[0][index].setImageResource(R.drawable.barrier);
            main_IMGVW_obstacles[0][index].setVisibility(View.VISIBLE);
            randNumbers[0][index] = BARRIER;
        }
        else{
            main_IMGVW_obstacles[0][index].setImageResource(R.drawable.dollar);
            main_IMGVW_obstacles[0][index].setVisibility(View.VISIBLE);
            randNumbers[0][index] = COIN;
        }
    }

   void checkCrash(){

       for(int j = 0 ; j <= COL -1  ; j++) {
           if (main_IMGVW_obstacles[ROW - 1][j].getVisibility() == View.VISIBLE
                   && main_IMG_car[j].getVisibility() == View.VISIBLE) {
               if (randNumbers[ROW - 1][j] == BARRIER) {
                   toast("Ops");
                   heart_IMVW[num_of_lives - 1].setVisibility(View.INVISIBLE);
                   vibrate();
                   soundCrash();
                   num_of_lives--;
                   if (num_of_lives == 0) {
                       toast("You lose!");
                       gameOver();
                   }
               } else {
                   score++;
                   main_TXV_dollars.setText("" + score);
               }
           }
           main_IMGVW_obstacles[ROW - 1][j].setVisibility(View.INVISIBLE);
           main_IMGVW_obstacles[COL][j].setVisibility(View.INVISIBLE);
       }
    }

    private void gameOver(){
        if (isSensorOn)
            isHandlerStop = true;
        saveRecordToSP();
        main_BTN_newGame.setVisibility(View.VISIBLE);
        stop();
    }

    private void stop() {
        stopTicker();
    }

    public void stopTicker() {
        timer.cancel();
    }

    public void gameAction() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {startGame();});
            }
        }, 0, speed);
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

    private void soundCrash() {
        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(this , R.raw.crash);
        mediaPlayer.start();
    }

    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void saveRecordToSP() {

        String fromSP = SharedPrefs.getInstance(this).getStrSP("MY_DB", "");
        topRecords = new Gson().fromJson(fromSP, TopRecords.class);

        topRecords.addRecord(new Record()
                .setName(playerName)
                .setScore(score)
                .setLat(lat)
                .setLon(lon));

        String jsonRecords = new Gson().toJson(topRecords);
        SharedPrefs.getInstance(this).putStringSP("MY_DB", jsonRecords);
    }
}
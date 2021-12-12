package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hw1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class HomePageActivity extends AppCompatActivity {

    private MaterialButton main_BTN_acc;
    private MaterialButton main_BTN_light;
    private MaterialButton main_BTN_recordTable;
    private TextInputEditText main_LBL_playerName;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double lat;
    private double lon;
    private boolean isSensorOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        findViews();
        locationPermission();
        initViews();

    }

    private void findViews() {

        main_BTN_acc = findViewById(R.id.main_BTN_acc);
        main_BTN_light = findViewById(R.id.main_BTN_light);
        main_LBL_playerName = findViewById(R.id.main_LBL_playerName);
        main_BTN_recordTable = findViewById(R.id.main_BTN_recordTable);

    }

    private void initViews() {

        main_BTN_acc.setOnClickListener(v -> gameAction("acc"));
        main_BTN_light.setOnClickListener(v -> gameAction("light"));
        main_BTN_recordTable.setOnClickListener(v -> openRecordTable());
    }

    private void gameAction(String str) {
        if(str.equals("acc"))
            isSensorOn = true;

        if (!isEmpty(main_LBL_playerName)){
            Intent intent = new Intent(this , MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(MainActivity.SENSOR_TYPE ,isSensorOn);
            bundle.putDouble(MainActivity.LATITUDE , lat);
            bundle.putDouble(MainActivity.LONGITUDE , lon);
            bundle.putString(MainActivity.PLAYER_NAME , main_LBL_playerName.getText().toString());
            intent.putExtra("bundle" , bundle);
            startActivity(intent);
            finish();
        }else{
            toast("You must fill PlayerName");
        }
    }

    private boolean isEmpty(TextInputEditText text) {
        if (text.getText().toString().trim().length() > 0)
            return false;
        return true;
    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    private void locationPermission() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, (android.location.LocationListener) locationListener);
        }
    }

    private void openRecordTable() {

        Intent intent = new Intent(this , RecordTableActivity.class);
        Bundle bundle = new Bundle();
        startActivity(intent);
        finish();
    }
}
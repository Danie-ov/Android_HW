package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hw1.R;
import com.google.gson.Gson;

import Fragments.ListFragment;
import Fragments.MapFragment;
import Interfaces.CallBack_List;
import Models.TopRecords;
import Utiles.SharedPrefs;

public class RecordTableActivity extends AppCompatActivity {

    private TopRecords topRecords;
    private String fromJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_table);

        fromJson = SharedPrefs.getInstance(this).getStrSP("MY_DB", "");
        topRecords = new Gson().fromJson(fromJson, TopRecords.class);

        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mapFrame, mapFragment).commit();

        ListFragment listFragment = new ListFragment();
        listFragment.setActivity(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.recordFrame, listFragment).commit();

        CallBack_List callBack_list = new CallBack_List() {
            @Override
            public void zoomToMarker(double lat, double lon) {
                mapFragment.changeMap(lat, lon);
            }
        };

        listFragment.setActivity(this);
        listFragment.setCallbackList(callBack_list);
    }
}
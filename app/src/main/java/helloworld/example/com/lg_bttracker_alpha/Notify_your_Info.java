package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Notify_your_Info extends Activity {

    private String device_address;
    private double Longtitude;
    private double Latitude;

    private TextView BTtracker_address;
    private TextView BTtracker_Longtitude;
    private TextView BTtracker_Latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notify_your__info);

        BTtracker_address = (TextView) findViewById(R.id.BTtracker_address);
        BTtracker_Longtitude = (TextView) findViewById(R.id.BTtracker_Longtitude);
        BTtracker_Latitude = (TextView) findViewById(R.id.BTtracker_Latitude);

        final Intent intent = getIntent();
        device_address = intent.getStringExtra("deviceAddress");
        Longtitude = intent.getDoubleExtra("Longtitude", 0.0);
        Latitude = intent.getDoubleExtra("Latitude", 0.0);

        BTtracker_address.setText(device_address);
        BTtracker_Longtitude.setText(Longtitude+" ");
        BTtracker_Latitude.setText(Latitude+" ");
    }
}

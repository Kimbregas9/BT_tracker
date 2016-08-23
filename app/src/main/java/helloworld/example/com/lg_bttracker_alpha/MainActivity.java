package helloworld.example.com.lg_bttracker_alpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.bluetooth.*;
import android.widget.ImageView;
import android.widget.Toast;

public class
        MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;
    private ConnectivityManager manager;

    private ImageView network_state;
    private ImageView gps_state;

    private boolean bt_able = false;
    private boolean network_able = false;
    private boolean wifi_able = false;
    private boolean gps_able = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        network_state = (ImageView) findViewById(R.id.network_state);
        gps_state = (ImageView) findViewById(R.id.gps_state);

        manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        //gps 읽기.
        LocationManager gps_manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (gps_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps_state.setImageResource(R.drawable.ok_state);
            gps_able = true;
        } else {
            gps_state.setImageResource(R.drawable.no_state);
            gps_able = false;
        }

        if (wifi.isConnected() || mobile.isConnected()) {
            network_state.setImageResource(R.drawable.ok_state);
        } else {
            network_state.setImageResource(R.drawable.no_state);
        }

        // get the bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // check if the device has bluetooth capabilities
        // if not, display a toast message and close the app
        if (mBluetoothAdapter == null) {

            Toast.makeText(this, "This app requires a bluetooth capable phone", Toast.LENGTH_SHORT).show();
            finish();
        }

        // set the GUI on the actual state
        updateGUI(mBluetoothAdapter.getState());

        // Create a broadcast receiver for receiving notifications
        // when bluetooth changes status
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                    int actualState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    updateGUI(actualState);

                } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null) {
                        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected())
                            wifi_able = true;
                        else
                            wifi_able = false;
                    }
                    if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
                        if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected())
                            network_able = true;
                        else
                            network_able = false;
                    }

                    if (wifi_able || network_able)
                        network_state.setImageResource(R.drawable.ok_state);
                    else
                        network_state.setImageResource(R.drawable.no_state);

                } else if (action.matches("android.location.PROVIDERS_CHANGED")) {
                    LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gps_state.setImageResource(R.drawable.ok_state);
                        gps_able = true;
                    } else {
                        gps_state.setImageResource(R.drawable.no_state);
                        gps_able = false;
                    }
                }

            }
        };

        // Register the broadcast receiver for the ACTION_STATE_CHANGE event
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(mReceiver, intentFilter);
    }

    private void updateGUI(int actualState) {

        // get the GUI objects
        ImageView bt_state = (ImageView) findViewById(R.id.bt_state);

        switch (actualState) {

            case BluetoothAdapter.STATE_ON:
                bt_state.setImageResource(R.drawable.ok_state);
                bt_able = true;
                break;

            case BluetoothAdapter.STATE_OFF:
                bt_state.setImageResource(R.drawable.no_state);
                bt_able = false;
                break;

            case BluetoothAdapter.STATE_TURNING_ON:
                bt_state.setImageResource(R.drawable.ok_state);
                bt_able = true;
                break;

            case BluetoothAdapter.STATE_TURNING_OFF:
                bt_state.setImageResource(R.drawable.no_state);
                bt_able = false;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister the broadcast receiver
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    public void next(View v) {

        if (bt_able && (network_able || wifi_able) && gps_able) {
            startActivity(new Intent(getApplication(), BT_List.class));
        } else {
            Toast.makeText(getApplication(), "Please Check your state...", Toast.LENGTH_SHORT).show();
        }
    }
}

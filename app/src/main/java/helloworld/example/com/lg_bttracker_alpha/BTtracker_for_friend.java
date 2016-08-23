package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BTtracker_for_friend extends Activity implements OnMapReadyCallback {

    private TextView deviceName;
    private TextView lastDate;
    private TextView rssiRecord;
    private TextView lastLocation;
    private ImageView connectState;

    private String rssi_value;

    private final static String TAG = "dsfsdfdsfdsf";

    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;

    private double Long;
    private double Lat;

    private Context now;

    static final LatLng SEOUL = new LatLng(37.56, -126.97);
    private GoogleMap map;
    private Calendar calendar;
    private LatLng latest_location = new LatLng(37.56, -126.97);

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        Long = location.getLongitude();
                        Lat = location.getLatitude();
                        latest_location = new LatLng(Lat, Long);
                        drawMarker(location);
                    }
                };

                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(getApplicationContext(), locationResult);

                updateConnectionState(R.string.connected);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        Long = location.getLongitude();
                        Lat = location.getLatitude();
                        latest_location = new LatLng(Lat, Long);
                        drawMarker(location);
                    }
                };

                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(getApplicationContext(), locationResult);

                updateConnectionState(R.string.disconnected);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                startReadRssi();
                // Show all the supported services and characteristics on the user interface.
                // displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_GATT_RSSI.equals(action)) {
                Log.d(TAG, "BroadCast + RSSI");
                rssi_value = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                rssi_value += "dB";
                Log.d(TAG, "rssi_value = " + rssi_value);
                updateRSSI(rssi_value);
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);
        List<Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);
                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nowAddress;
    }

    private void startReadRssi() {
        new Thread() {
            public void run() {

                while (mBluetoothLeService != null) {
                    try {
                        mBluetoothLeService.readRssi();
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    private void updateConnectionState(final int resourceId) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resourceId == R.string.connected) {
                    connectState.setBackgroundResource(R.drawable.bt_on_icon);

                    lastDate.setText(calendar.getTime().toString());
                    lastLocation.setText(getAddress(now, Lat, Long));

                } else if (resourceId == R.string.disconnected) {
                    connectState.setBackgroundResource(R.drawable.bt_off_icon);

                    lastDate.setText(calendar.getTime().toString());
                    lastLocation.setText(getAddress(now, Lat, Long));
                    rssiRecord.setText("Disconnected");
                    startActivity(new Intent(getApplication(), NotifyDisconnected.class));
                }
            }
        });
    }

    private void updateRSSI(String value) {
        // TODO Auto-generated method stub
        if (value != null) {
            rssiRecord.setText(value);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bttracker_for_friend);

        now = getApplication();
        calendar = Calendar.getInstance();

        deviceName = (TextView) findViewById(R.id.deviceName1);
        lastDate = (TextView) findViewById(R.id.lastDate1);
        rssiRecord = (TextView) findViewById(R.id.rssiRecord1);
        lastLocation = (TextView) findViewById(R.id.lastLocation1);
        connectState = (ImageView) findViewById(R.id.connectState1);

        final Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra("deviceAddress");
        Long = intent.getDoubleExtra("Longtitude", 0.0);
        Lat = intent.getDoubleExtra("Latitude", 0.0);

        latest_location = new LatLng(Lat, Long);

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googlemap) {

        map = googlemap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 21));//초기 위치...수정필요

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                drawMarker(location);
            }
        };

        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);
    }

    private void drawMarker(Location location) {

        //기존 마커 지우기
        map.clear();
        LatLng crrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(crrentLocation, 21));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        //마커 추가
        map.addMarker(new MarkerOptions()
                .position(latest_location)
                .snippet("Lat:" + latest_location.getLatitude() + "Lng:" + latest_location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(mDeviceName));
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_RSSI);
        return intentFilter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }
}
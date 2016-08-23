package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class Register_another_BT_info extends Activity {

    private EditText MAC_address;
    private EditText Longtitude;
    private EditText Latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_another__bt_info);

        MAC_address = (EditText) findViewById(R.id.MAC_address);
        Longtitude = (EditText) findViewById(R.id.Longtitude);
        Latitude = (EditText) findViewById(R.id.Latitude);
    }

    public void submit(View v){
        Intent aa = new Intent(getApplication(), BTtracker_for_friend.class);
        aa.putExtra("deviceAddress", MAC_address.getText());
        aa.putExtra("Longtitude", Longtitude.getText());
        aa.putExtra("Longtitude", Latitude.getText());

        startActivity(aa);
    }
}

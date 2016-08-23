package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class NotifyDisconnected extends Activity {

    private Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notify_disconnectd);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(new long[]{100, 300, 100, 500}, 0);
    }

    public void checkOK(View v) {
        vib.cancel();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        vib.cancel();
    }
}

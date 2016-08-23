package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Notify_delete extends Activity {

    private String device_address;
    private String device_name;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    final static String dbName = "alpha2.db"; //내가 지정한 database 이름.
    final static int dbVersion = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notify_delete);

        final Intent intent = getIntent();
        device_address = intent.getStringExtra("deviceAddress");
        device_name = intent.getStringExtra("deviceName");
    }

    public void deleteOk(View v){

        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db = dbHelper.getWritableDatabase();

        String table1 = "DELETE FROM BT_Information WHERE deviceName='" + device_name+ "';";
        db.execSQL(table1);

        String table2 = "DELETE FROM BTtrackers WHERE device_address='" + device_address+ "';";
        db.execSQL(table2);

        startActivity(new Intent(this, BT_List.class));
        this.finish();

        BT_List aa = new BT_List();
        aa.now.finish();
    }

    public void deleteCancel(View v){
        startActivity(new Intent(this, BT_List.class));
        this.finish();

        BT_List aa = new BT_List();
        aa.now.finish();
    }
}

package helloworld.example.com.lg_bttracker_alpha;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class BT_List extends AppCompatActivity {

    /*
    여기서는 이미 페어링된(저장된?) 목록들을 보여주는 화면
    registerBT method는 BT tracker를 찾기위한 것.
     */
    private ListView list;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String sql;
    private Cursor cursor;
    private String table_name = "BTtrackers";

    final static String dbName = "alpha2.db"; //내가 지정한 database 이름.
    final static int dbVersion = 1;

    public Activity now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register__bt);

        now = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        list = (ListView) findViewById(R.id.list);
        dbHelper = new DBHelper(this, dbName, null, dbVersion);
        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM " + table_name + ";";

        cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            startManagingCursor(cursor);
            cursor.moveToFirst();
            AdapterForBT aa = new AdapterForBT(this, cursor); // 오직 문제 하나만 뿌려주는 리스트뷰랑 연결.
            list.setAdapter(aa);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView Name = (TextView) view.findViewById(R.id.btName);
                TextView MAC = (TextView) view.findViewById(R.id.btAddress);
                String device_name = Name.getText().toString();
                String device_address = MAC.getText().toString();

                Intent aa = new Intent(getApplication(), BTtracker_information.class);
                aa.putExtra("deviceName", device_name);
                aa.putExtra("deviceAddress", device_address);

                startActivity(aa);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView Name = (TextView) view.findViewById(R.id.btName);
                TextView MAC = (TextView) view.findViewById(R.id.btAddress);
                String device_name = Name.getText().toString();
                String device_address = MAC.getText().toString();

                Intent aa = new Intent(getApplication(), Notify_delete.class);
                aa.putExtra("deviceName", device_name);
                aa.putExtra("deviceAddress", device_address);

                startActivity(aa);

                return false;
            }
        });
    }

    public void registerBT(View v) {
        startActivity(new Intent(getApplication(), Register_BT.class));
    }

    public void help_another(View v){
        startActivity(new Intent(getApplication(), Register_another_BT_info.class));
    }
}

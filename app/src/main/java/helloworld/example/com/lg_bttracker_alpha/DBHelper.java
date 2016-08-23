package helloworld.example.com.lg_bttracker_alpha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private String sql;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //DB에 default로 문제 다 만들어야함.
    @Override
    public void onCreate(SQLiteDatabase db) {

        // BTtracker를 기록함.
        sql =  "create table BTtrackers (_id integer primary key autoincrement," +
                "device_name text not null, device_address text not null);";

        db.execSQL(sql);

        // 각각의 BTtracker의 정보를 기록함.
        sql =  "create table BT_Information (_id integer primary key autoincrement," +
                "deviceName text not null, Date text not null, Long text not null, Lat text not null);";

        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
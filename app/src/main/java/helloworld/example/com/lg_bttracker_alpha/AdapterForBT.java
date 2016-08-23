package helloworld.example.com.lg_bttracker_alpha;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AdapterForBT extends CursorAdapter {
    @SuppressWarnings("deprecation")
    public AdapterForBT(Context context, Cursor c) {
        super(context, c);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView Name = (TextView)view.findViewById(R.id.btName);
        final TextView MAC = (TextView)view.findViewById(R.id.btAddress);
        Name.setText(cursor.getString(1));
        MAC.setText(cursor.getString(2));
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.each_bt, parent, false);
        return v;
    }
}
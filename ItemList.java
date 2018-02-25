package com.microsoft.CognitiveServicesExample;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ItemList extends Activity {
    private DatabaseOpenHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;

    TextView loctv,ordtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_layout);

        mDbHelper = new DatabaseOpenHelper(this);

        String loc = "";
        String ord = "";

        Cursor c = readItems();

        if(c!=null)
        {
            try
            {
                while (c.moveToNext()) {
                    ord += c.getString(c.getColumnIndex("food_order")) + "\n";
                    loc += c.getString(c.getColumnIndex("location")) + "\n";
                }
            }
            finally {
                c.close();
            }
        }
        Log.i("onCreate","ord: "+ord);
        Log.i("onCreate","loc: "+loc);
        loctv = (TextView) findViewById(R.id._id);
        ordtv = (TextView) findViewById(R.id.name);

        loctv.setText(loc);
        ordtv.setText(ord);

        //setListAdapter(mAdapter);
    }

    private Cursor readItems() {
        return mDbHelper.getWritableDatabase().query(DatabaseOpenHelper.TABLE_NAME,
                DatabaseOpenHelper.columns, null, new String[] {}, null, null, null); // 7 values in the cursor
    }

    @Override
    protected void onDestroy() {

        mDbHelper.getWritableDatabase().close();
        mDbHelper.deleteDatabase();
        Log.d("destroy","Database deleted");
        super.onDestroy();

    }
}

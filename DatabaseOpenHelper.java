package com.microsoft.CognitiveServicesExample;

/**
 * Created by DELL on 23-02-2018.
 */
/**
 * Created by chinm on 24-02-2018.
 */

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    final static String TABLE_NAME = "orders_table";
    final static String LOCATION = "location";
    final static String _ID = "_id";
    final static String FOOD_ORDER = "food_order";
    final static String[] columns = { LOCATION, FOOD_ORDER };

    final private static String CREATE_CMD =

            //"DROP table if exists orders_table;"+
    " CREATE TABLE if not exists orders_table (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FOOD_ORDER + " TEXT NOT NULL, "
                    + LOCATION + " TEXT NOT NULL)";

    final private static String NAME = "karanDB";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public DatabaseOpenHelper(Context context) {
        // Always call superclass's constructor
        super(context, NAME, null, VERSION);

        // Save the context that created DB in order to make calls on that context,
        // e.g., deleteDatabase() below.
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }

    // Calls ContextWrapper.deleteDatabase() by way of inheritance
    void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }



    String getOrderByLocation(String location)
    {
        SQLiteDatabase db = getWritableDatabase();
        String foodOrder = "";
        String[] cols = {FOOD_ORDER};
        Cursor c = db.query(TABLE_NAME,cols, null, new String[] {LOCATION}, null, null, null);

        if(c!=null)
        {
            try
            {
                c.moveToFirst();
                foodOrder = c.getString(c.getColumnIndex(FOOD_ORDER));
            }
            finally {
                c.close();
            }
        }
        return foodOrder;
    }

    /*boolean updateOrderDetails(String location, ArrayList<String> foodOrder)
    {
        try
        {
        }
        catch (Exception e)
        {
            Log.d("update",e.getLocalizedMessage());
            return false;
        }
        return true;
    }*/

}
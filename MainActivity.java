package com.microsoft.CognitiveServicesExample;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    private DatabaseOpenHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;
    static ArrayList<String> a = new ArrayList<String>();
    ListView lv;
    String locationName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                //execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDbHelper = new DatabaseOpenHelper(this);

        Button button_2 = (Button)findViewById(R.id.button_2);
        Button button_3 = (Button)findViewById(R.id.button_3);
        Button button_4 = (Button)findViewById(R.id.button_4);
        Button button_5 = (Button)findViewById(R.id.button_5);


        lv = (ListView) findViewById(R.id.lview);


       Intent in= getIntent();
        Bundle b = in.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("order");
            a.add(j);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    a);

            lv.setAdapter(arrayAdapter);
        }



        button_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText tview = (EditText) findViewById(R.id.editText);
                String content = tview.getText().toString(); //gets you the contents of edit text
                a.add(content);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        a);

                lv.setAdapter(arrayAdapter);
            }
        });

        button_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , Activity2.class);
                i.putExtra("list", a);
                startActivity(i);
            }
        });


        button_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();//
                    double longitude = gps.getLongitude();//
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if(null!=listAddresses&&listAddresses.size()>0)
                        {
                            locationName = listAddresses.get(0).getFeatureName();
                            if (locationName.replaceAll(" ","").matches("[0-9]+"))
                                locationName = listAddresses.get(0).getAddressLine(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // \n is for new line
                    if(locationName.trim().isEmpty())
                        locationName = "nowhere";
                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Your are at " + locationName,Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                String fooditems = "";
                for(int i = 0;i<a.size();i++) {
                    fooditems = fooditems + a.get(i)+", ";
                }
                fooditems = fooditems.substring(0,fooditems.length()-2);
                    ContentValues values = new ContentValues();
                    values.put(DatabaseOpenHelper.LOCATION, locationName);
                    values.put(DatabaseOpenHelper.FOOD_ORDER,fooditems);
                    mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);
                    values.clear();
            }
        });

        button_5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , ItemList.class);
                startActivity(i);
            }
        });


        /*button_6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });*/
    }


}

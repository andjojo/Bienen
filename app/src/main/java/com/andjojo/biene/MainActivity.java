package com.andjojo.biene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andjojo.biene.WebAPI.DownloadFilesTask;
import com.andjojo.biene.WebAPI.HandlePHPResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MapView map = null;
    List<Task> taskStack;
    View tile;
    TextView taskTitle, taskDescription;
    LocationManager mLocationManager;
    String urlString = "http://18.194.159.113:5001/api/new_job/?lat=48.8552&lon=9.29231&radius=0.005";
    ProgressDialog dialog;
    Boolean firstGps = true;
    ImageButton dismiss,accept;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(this, "",
                "Blümchen werden erschnuppert...", true);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    30, mLocationListener);
        }

        dismiss = (ImageButton) findViewById(R.id.imageButton2);
        accept = (ImageButton) findViewById(R.id.imageButton);
        dismiss.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    v.animate()
                            .scaleXBy(-0.04f)
                            .scaleYBy(-0.04f)
                            .setDuration(200)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    v.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(200);
                    onNo(v);
                    return true;
                }
                return false;
            }
        });
        accept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    v.animate()
                            .scaleXBy(-0.04f)
                            .scaleYBy(-0.04f)
                            .setDuration(200)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    v.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(200);
                    onYes(v);
                    return true;
                }
                return false;
            }
        });
        tile = findViewById(R.id.tile);
        imageView = (ImageView) findViewById(R.id.imageView2);
        taskTitle = (TextView) findViewById(R.id.textView);
        taskDescription = (TextView) findViewById(R.id.textView2);
        taskStack = new ArrayList<Task>();

        /*if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
               // Log.v(TAG, "Permission is granted");
            } else {
                //Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        setContentView(R.layout.activity_main);
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(53.551085, 9.993682);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }else{
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                            30, mLocationListener);
                }
                return;
            }

        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if (firstGps) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                URL url = null;
                try {
                    url = new URL(urlString );//+ "?lat="+latitude+"&lon="+longitude+"&radius=0.005");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                new DownloadFilesTask(url, handlePHPResult).execute("");
                firstGps = false;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void onYes(View v){
        tile.animate()
                .translationX(1.3f*tile.getWidth())
                .rotation(30)
                .alpha(0f)
                .setDuration(500)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, JobActivity.class);
                intent.putExtra("Task", taskStack.get(0));
                startActivity(intent);
                showNextTask();
                tile.animate().translationX(0).translationY(0).rotation(0).setDuration(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        tile.animate().alpha(1f).setDuration(200);
                    }
                });

            }
        });

    }

    public void onNo(View v){
        tile.animate()
                .translationX(-1.3f*tile.getWidth())
                .alpha(0f)
                .rotation(-30)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        taskStack.add(taskStack.get(0));
                        taskStack.remove(0);
                        showNextTask();
                        tile.animate().translationX(0).translationY(0).rotation(0).setDuration(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                tile.animate().alpha(1f).setDuration(200);
                            }
                        });
                    }
                });
    }

    public void showNextTask(){
        taskTitle.setText(taskStack.get(0).getTaskHeader());
        taskDescription.setText(taskStack.get(0).getTaskDescription());
        if (taskStack.get(0).getCat().equals("Soziales"))
            imageView.setImageResource(R.drawable.social);
        else if (taskStack.get(0).getCat().equals("Einkaufen"))
            imageView.setImageResource(R.drawable.shopping);
    }

    public HandlePHPResult handlePHPResult=(s, url)->{
        dialog.dismiss();
        JSONArray jsonTasks = new JSONArray(s);
        for (int i=0;i<jsonTasks.length();i++){
            JSONObject task = jsonTasks.getJSONObject(i);
            taskStack.add(new Task(task));
        }
        showNextTask();
        /*for (int i=0;i<10;i++){
            taskStack.add(new Task("task "+i));
        }*/
    };

}

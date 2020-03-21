package com.andjojo.biene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

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
    TextView taskTitle;
    String urlString = "http://18.194.159.113:5001/api/new_job/?lat=48.8552&lon=9.29231&radius=0.005";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL url = null;
        try {
            url = new URL(urlString+"");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFilesTask(url, handlePHPResult).execute("");
        dialog = ProgressDialog.show(this, "",
                "Route wird geladen...", true);
        tile = findViewById(R.id.tile);
        taskTitle = (TextView) findViewById(R.id.textView);
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
        taskTitle.setText(taskStack.get(0).getTaskName());
    }

    public HandlePHPResult handlePHPResult=(s, url)->{
        dialog.dismiss();
        JSONArray jsonTasks = new JSONArray(s);
        for (int i=0;i<jsonTasks.length();i++){
            JSONObject task = jsonTasks.getJSONObject(i);
            taskStack.add(new Task(task));
        }
        /*for (int i=0;i<10;i++){
            taskStack.add(new Task("task "+i));
        }*/
    };

}

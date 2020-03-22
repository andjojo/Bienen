package com.andjojo.biene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andjojo.biene.WebAPI.DownloadFilesTask;
import com.andjojo.biene.WebAPI.HandlePHPResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

public class JobActivity extends AppCompatActivity {


    String urlString = "http://18.194.159.113:5001/api/job_accept/?req_user_id=";
    ProgressDialog dialog;
    Task task;
    TextView Header,Beschreibung,infotext;
    String phonenumber = "0176 57884499";
    Boolean cangoBack = false;
    LinearLayout linearLayout;
    SharedPreferences sp;
    int counter = 0;
    ImageView imageView;
    ImageButton telephone,finishtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_job);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayoutjob);
        linearLayout.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(0);

        sp = getApplicationContext().getSharedPreferences(
                "Biene", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("Task");
        if (task==null){
            task = new Task("Du musst noch was erledigen");
            task.setUserID(sp.getString("userID",""));
        }else{
            sp.edit().putString("userID",task.getUserID()).apply();
        }
        URL url = null;
        try {
            url = new URL(urlString+task.getUserID()+"&hero_phone=17215215214&hero_user_id=SUperHero&hero_vorname=Jochen&hero_nachname=Super&hero_adresse=HAuptstraße7&est_time=60");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFilesTask(url, handlePHPResult).execute("");
        dialog = ProgressDialog.show(this, "",
                "Bzzz...Bzzzz...Bzzzz...", true);
        Header = (TextView) findViewById(R.id.textView4);
        Beschreibung = (TextView) findViewById(R.id.textView5);
        infotext = (TextView) findViewById(R.id.textView3);
        telephone = (ImageButton) findViewById(R.id.imageButton3);
        finishtask = (ImageButton) findViewById(R.id.imageButton4);
        imageView = (ImageView) findViewById(R.id.imageView);
        Header.setText(task.getUserName()+", "+task.getTaskHeader());
        Beschreibung.setText(task.getTaskDescription());
        if(sp.getBoolean("calledAlready",false)){
            finishtask.setVisibility(View.VISIBLE);
        }

        if (task.getCat().equals("Soziales"))
            imageView.setImageResource(R.drawable.social);
        else if (task.getCat().equals("Einkaufen"))
            imageView.setImageResource(R.drawable.shopping);
        else
            imageView.setImageResource(R.drawable.others);


        telephone.setOnTouchListener(new View.OnTouchListener() {
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
                    onCall(v);
                    return true;
                }
                return false;
            }
        });

        finishtask.setOnTouchListener(new View.OnTouchListener() {
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
                    onFinishTask(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public void onEscape(View v){
        new AlertDialog.Builder(JobActivity.this)
                .setTitle("Aufgabe abgeben?")
                .setMessage("Willst du die Aufgabe doch einem anderen Bienchen überlassen?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        URL url = null;
                        try {
                            url = new URL("http://18.194.159.113:5001/api/abort_job/?req_user_id="+task.getUserID()+"&task=Einkaufen");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        new DownloadFilesTask(url, handlePHPResult).execute("");
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Abbrechen", null)
                .show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (cangoBack){
            if (counter>0) { Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        infotext.setText("Wenn du mit deinem Job fertig bist, drücke auf den Honigtopf :)");
                        finishtask.setVisibility(View.VISIBLE);
                    }
                }, 500);
                /**/
            }else{
                counter++;
            }
        }
    }

    public void onFinishTask(View v){
        new AlertDialog.Builder(JobActivity.this)
                .setTitle("Aufgabe erledigt?")
                .setMessage("Hast du die Aufgabe zufriedenstellend beendet und kann sie als erledigt markiert werden?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        URL url = null;
                        try {
                            url = new URL("http://18.194.159.113:5001/api/job_finished/?req_user_id="+task.getUserID());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        new DownloadFilesTask(url, handlePHPResult).execute("");
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    public void onCall(View v) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phonenumber));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(JobActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }else{
            startActivity(callIntent);
            cangoBack = true;
            sp.edit().putBoolean("calledAlready",true).apply();
        }

    }

    public HandlePHPResult handlePHPResult=(s, url)->{

        dialog.dismiss();
        //JSONObject jsonTask = new JSONObject(s);
        //phonenumber = jsonTask.getString("req_phone_number");
        if (url.toString().contains("job_finished")){
            JobActivity.super.onBackPressed();
            sp.edit().putBoolean("calledAlready",false).apply();
            sp.edit().putBoolean("taskActive",false).apply();
            sp.edit().putInt("level",sp.getInt("level",0)+1).apply();
        }

        else if (url.toString().contains("job_accept")){
            JSONArray jsonTaskArray = new JSONArray(s);
            JSONObject jsonTask = jsonTaskArray.getJSONObject(0);
            phonenumber = jsonTask.getString("req_phone_nummber");
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(200);
            task = new Task(jsonTask);
            Header.setText(task.getUserName()+", "+task.getTaskHeader());
            Beschreibung.setText(task.getTaskDescription());
            sp.edit().putString("userID",task.getUserID()).apply();
        }
        else if (url.toString().contains("abort_job")){
            super.onBackPressed();
            sp.edit().putBoolean("calledAlready",false).apply();
            sp.edit().putBoolean("taskActive",false).apply();
        }
    };
}
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
import android.view.View;
import android.widget.ImageButton;
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


    String urlString = "http://18.194.159.113:5001/api/job_accept/?req_user_id=MaxLuithardtLindenäckerstraße&hero_phone=17215215214&hero_user_id=SUperHero&hero_vorname=Jochen&hero_nachname=Super&hero_adresse=HAuptstraße7&est_time=60";
    ProgressDialog dialog;
    Task task;
    TextView Header,Beschreibung;
    String phonenumber = "0176 57884499";
    Boolean cangoBack = false;
    LinearLayout linearLayout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_job);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayoutjob);
        linearLayout.animate()
                .scaleX(0)
                .scaleY(0)
                .setDuration(0);

        sp = this.getSharedPreferences(
                "Biene", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putBoolean("", cangoBack);
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("Task");
        URL url = null;
        try {
            url = new URL(urlString + "");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFilesTask(url, handlePHPResult).execute("");
        dialog = ProgressDialog.show(this, "",
                "Bzzz...Bzzzz...Bzzzz...", true);
        Header = (TextView) findViewById(R.id.textView4);
        Beschreibung = (TextView) findViewById(R.id.textView5);
        Header.setText(task.getUserName()+", "+task.getTaskHeader());
        Beschreibung.setText(task.getTaskDescription());
    }

    @Override
    public void onBackPressed() {
        if (cangoBack)super.onBackPressed();
    }

    public void onEscape(View v){
        new AlertDialog.Builder(JobActivity.this)
                .setTitle("Aufgabe abgeben?")
                .setMessage("Willst du die Aufgabe doch einem anderen Bienchen überlassen?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        JobActivity.super.onBackPressed();
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
                        new AlertDialog.Builder(JobActivity.this)
                                .setTitle("Fleißiges Bienchen?")
                                .setMessage("Konntest du weiterhelfen und kann die Anzeige offline gehen?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("Ja!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        URL url = null;
                                        try {
                                            url = new URL("http://18.194.159.113:5001/api/job_finished/?req_user_id="+task.getUserID()+"&task=Einkaufen");
                                        } catch (MalformedURLException e) {
                                            e.printStackTrace();
                                        }
                                        new DownloadFilesTask(url, handlePHPResult).execute("");
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton("noch Nicht", null)
                                .show();
                    }
                }, 500);
                /**/
            }else{
                counter++;
            }
        }
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
            editor.putBoolean("", cangoBack);
        }

    }

    public HandlePHPResult handlePHPResult=(s, url)->{

        dialog.dismiss();
        //JSONObject jsonTask = new JSONObject(s);
        //phonenumber = jsonTask.getString("req_phone_number");
        if (url.toString().contains("job_finished")){

            JobActivity.super.onBackPressed();
        }
        if (url.toString().contains("job_accept")){
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.animate()
                    .scaleX(1)
                    .scaleY(1)
                    .setDuration(200);
        }
    };
}

package com.andjojo.biene;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.andjojo.biene.WebAPI.DownloadFilesTask;
import com.andjojo.biene.WebAPI.HandlePHPResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class JobActivity extends AppCompatActivity {


    String urlString = "http://18.194.159.113:5001/api/job_accept/?req_user_id=MaxLuithardtLindenäckerstraße&hero_phone=17215215214&hero_user_id=SUperHero&hero_vorname=Jochen&hero_nachname=Super&hero_adresse=HAuptstraße7&est_time=60";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        URL url = null;
        try {
            url = new URL(urlString+"");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        new DownloadFilesTask(url, handlePHPResult).execute("");
        dialog = ProgressDialog.show(this, "",
                "Route wird geladen...", true);
    }

    public HandlePHPResult handlePHPResult=(s, url)->{
        dialog.dismiss();
    };
}

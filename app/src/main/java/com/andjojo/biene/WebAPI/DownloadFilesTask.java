package com.andjojo.biene.WebAPI;

import android.os.AsyncTask;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFilesTask extends AsyncTask<String, Void, String> {
    URL url;
    String s;
    HandlePHPResult handlePHPResult;
    public  DownloadFilesTask(URL url, HandlePHPResult handlePHPResult) {
        this.url=url;
        this.handlePHPResult=handlePHPResult;
    }

    protected String doInBackground(String... params) {
        String responseString = null;
            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                .url(url.toString())
                .build();
        try {
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


       return responseString;
    }


    @Override
    protected void onPostExecute(String result) {
        if (result!=null) {
            try {
                handlePHPResult.handle(result,url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
        }
    }
}

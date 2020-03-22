package com.andjojo.biene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    TextView level;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getApplicationContext().getSharedPreferences(
                "Biene", Context.MODE_PRIVATE);
        level = (TextView)findViewById(R.id.textView7);
        level.setText(sp.getInt("level",0)+"");
    }

    public void onYay(View v){
        super.onBackPressed();
    }
}

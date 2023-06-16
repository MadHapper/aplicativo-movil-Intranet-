package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;


public class Deudas_usuario extends AppCompatActivity {

    private Button _linkpagar;
    String _url = "https://mpago.la/2KRXUUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas_usuario);

        _linkpagar = findViewById(R.id.linkpagar);

        _linkpagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri _link= Uri.parse(_url);
                Intent i =new Intent(Intent.ACTION_VIEW,_link);
                startActivity(i);

              //  Log.d("click","ok");
            }
        });



    }




}
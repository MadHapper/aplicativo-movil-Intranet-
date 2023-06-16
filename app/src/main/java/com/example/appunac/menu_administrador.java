package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class menu_administrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_administrador);
    }

    public void abrirListacursos (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }


    public void abrirListausuarios (View view) {
        startActivity(new Intent(getApplicationContext(),Admin_listausuario.class));
        finish();
    }

    public void abrirListamatriculas (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }

    public void abrirListapagosydeudas (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }

    public void abrirHorarioacademico (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }

}
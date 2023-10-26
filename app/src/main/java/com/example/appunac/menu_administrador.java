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

    public void abrirListapagosydeudas (View view) {
        Intent intent = new Intent(menu_administrador.this, admin_pagos.class);
        startActivity(intent);
    }

    public void abrirregistroacedemico (View view) {
        Intent intent = new Intent(menu_administrador.this, admin_registroacademico.class);
        startActivity(intent);
    }

    public void abrirusuarios (View view) {
        Intent intent = new Intent(menu_administrador.this, Admin_listausuario.class);
        startActivity(intent);
    }

    public void abrirtramites (View view) {
        Intent intent = new Intent(menu_administrador.this, admin_tramites.class);
        startActivity(intent);
    }

    public void abrirListacursos (View view) {
        Intent intent = new Intent(menu_administrador.this, admin_listacursos.class);
        startActivity(intent);
    }
}

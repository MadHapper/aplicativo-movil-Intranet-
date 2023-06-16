package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menuUsuario extends AppCompatActivity {

    TextView textViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        textViewUsuario = findViewById(R.id.textView9);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);
    }

    //abrir deudas y pagos
    public void abrirPagos (View view) {
        startActivity(new Intent(getApplicationContext(),Deudas_usuario.class));
        finish();
    }

    //abre mis cursos
    public void abrirMiscursos (View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menuUsuario.this, cursos_usuario.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }

    //abre matricula
    public void abrirMatricula (View view) {
        startActivity(new Intent(getApplicationContext(),Matricula_usuario.class));
        finish();
    }

    //abre horario academico
    public void abrirhorarioacademico (View view) {
        startActivity(new Intent(getApplicationContext(),horarioacademico_usuario.class));
        finish();
    }

    //abre registro academico
    public void abrirregistroacedemico (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }

}
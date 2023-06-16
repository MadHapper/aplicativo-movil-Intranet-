package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu_docente extends AppCompatActivity {

    TextView textViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_docente);

        textViewUsuario = findViewById(R.id.textView111);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);
    }

    public void abrirMiscursos (View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menu_docente.this, docente_curso.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }


    public void abrirHorarioacademico (View view) {
        startActivity(new Intent(getApplicationContext(),horarioacademico_usuario.class));
        finish();
    }
}
package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class docente_ingles extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_ingles);

        textViewUsuario = findViewById(R.id.textView14);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.textView9);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        textViewCursoc = findViewById(R.id.textView47);
        String cursoc = getIntent().getStringExtra("cursoc");
        textViewCursoc.setText(cursoc);
    }

    public void abrirNotas (View view) {
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(docente_ingles.this, docente_registronotas.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

    public void abrirLinks (View view) {
        startActivity(new Intent(getApplicationContext(),CursoLinks.class));
        finish();
    }

    public void abrirDocumentos (View view){

        String url = "https://drive.google.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void abrirMeet (View view){

        String url = "https://meet.google.com/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    public void onBackPressed() {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(docente_ingles.this, menu_docente.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}
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
    }

    public void abrirAsistencia (View view) {
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(docente_ingles.this, docente_asistencia.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

    public void abrirDocumentos (View view){

        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(docente_ingles.this, docente_biblioteca.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

    public void abrirMeet (View view){
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(docente_ingles.this, Docente_Meet.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

    public void abrirTareas (View view){
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(docente_ingles.this, docente_tareas.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

}
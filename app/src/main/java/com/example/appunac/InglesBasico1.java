package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InglesBasico1 extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc, meetTextView, documentosTextView, tareasTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingles_basico1);

        textViewUsuario = findViewById(R.id.textView14);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.textView9);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        textViewCursoc = findViewById(R.id.textView47);
        String cursoc = getIntent().getStringExtra("cursoc");
        textViewCursoc.setText(cursoc);

        meetTextView = findViewById(R.id.meet);
        documentosTextView = findViewById(R.id.documentos);
        tareasTextView = findViewById(R.id.tareas);

        // Actualizar los TextView automáticamente al ingresar a la actividad
        actualizarTextViews();
    }

    // funciones de ingreso a botones
    public void abrirNotas(View view) {
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(InglesBasico1.this, registronotas.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

    public void abrirAsistencia(View view) {
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        Intent intent = new Intent(InglesBasico1.this, usuario_asistensia.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
    }

    public void abrirDocumentos (View view){

        String url = documentosTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void abrirMeet (View view){
        String url = meetTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void abrirTareas(View view) {
        String url = tareasTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void obtenerDatosCurso(final String url, final String textView) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL apiUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        reader.close();

                        // Procesar la respuesta JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        final String valor = jsonResponse.getString(textView);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("documentos".equals(textView)) {
                                    // Actualizar el TextView de documentos
                                    documentosTextView.setText(valor);
                                } else if ("meet".equals(textView)) {
                                    // Actualizar el TextView de meet
                                    meetTextView.setText(valor);
                                } else if ("tareas".equals(textView)) {
                                    // Actualizar el TextView de tareas
                                    tareasTextView.setText(valor);
                                }
                            }
                        });
                    } else {
                        // Manejar el error de conexión
                    }

                    conn.disconnect();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void actualizarTextViews() {
        String url = "https://www.productosjr.com/aplicativos/appunac/obtener_cursos.php?cursoc=" + textViewCursoc.getText().toString();
        obtenerDatosCurso(url, "meet");
        obtenerDatosCurso(url, "documentos");
        obtenerDatosCurso(url, "tareas");
    }
}

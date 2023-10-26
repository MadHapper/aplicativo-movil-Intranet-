package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class usuario_asistensia extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_asistensia);

        textViewUsuario = findViewById(R.id.alumnon);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.nombrec);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        textViewCursoc = findViewById(R.id.codigoc);
        String cursoc = getIntent().getStringExtra("cursoc");
        textViewCursoc.setText(cursoc);

        String codigo = getIntent().getStringExtra("cursoc");



        obtenerEstadosAsistencia(codigo, usuario);
    }

    private void obtenerEstadosAsistencia(String cursoc, String usuario) {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/asistenciausuario.php?codigo=" +cursoc + "&codigo_alumno=" + usuario);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if (conn.getResponseCode() == 200) {
                    InputStream responseBody = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Analizar la respuesta JSON y crear las filas de asistencia
                    runOnUiThread(() -> {
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            LinearLayout asistenciaList = findViewById(R.id.AsitenciaList);

                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0); // Obtener el primer objeto JSON

                                for (int i = 1; i <= 20; i++) {
                                    // Crear una nueva fila (TableRow) y establecer los valores
                                    TableRow newRow = (TableRow) getLayoutInflater().inflate(R.layout.seccion_asistensia, null);

                                    // Obtener las vistas dentro de la fila
                                    TextView numeroSeccion = newRow.findViewById(R.id.NumeroSeccion);
                                    TextView estadoTextView = newRow.findViewById(R.id.EstadoAsistencia);

                                    // Configurar el número de sección
                                    numeroSeccion.setText("Sesión " + i);

                                    // Obtener el estado de asistencia de la columna "seccionX" correspondiente
                                    String columnaSeccion = "Sesión" + i;
                                    String estadoAsistencia = jsonObject.getString(columnaSeccion);
                                    estadoTextView.setText(estadoAsistencia);
                                    CardView cardView = newRow.findViewById(R.id.cardviewcolor);

                                    if (estadoAsistencia.equals("Presente")) {
                                        cardView.setCardBackgroundColor(Color.parseColor("#1ed013")); // Verde
                                    } else if (estadoAsistencia.equals("Falta")) {
                                        cardView.setCardBackgroundColor(Color.parseColor("#f34336")); // Rojo
                                    } else {
                                        // Puedes configurar un color por defecto o manejar otros estados si es necesario
                                        cardView.setCardBackgroundColor(Color.parseColor("#A6A6A6"));
                                    }

                                    // Agregar la fila al LinearLayout
                                    asistenciaList.addView(newRow);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}

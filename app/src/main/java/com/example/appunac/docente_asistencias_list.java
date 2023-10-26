package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

public class docente_asistencias_list extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc, textviewNombreco, textcod;
    TextView numeroSeccion;
    TextView estadoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_asistencias_list);

        textViewUsuario = findViewById(R.id.alumnon);
        textViewCurso = findViewById(R.id.nombrec);
        textViewCursoc = findViewById(R.id.codigoc);
        textviewNombreco = findViewById(R.id.nombreComplese);
        textcod = findViewById(R.id.textcod);


        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");
        String nombrecompleto = getIntent().getStringExtra("nombreCompleto");
        String cod = getIntent().getStringExtra("cod");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);
        textviewNombreco.setText(nombrecompleto);
        textcod.setText(cod);

        obtenerEstadosAsistencia(cursoc,cod);
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

                                    // Agregar un clic al CardView para iniciar la actividad de modificación
                                    cardView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View View) {
                                            String usuario = textViewUsuario.getText().toString();
                                            String curso = textViewCurso.getText().toString();
                                            String cursoc = textViewCursoc.getText().toString();
                                            String Nombreco = textviewNombreco.getText().toString();
                                            String cod = textcod.getText().toString();
                                            String seccion= numeroSeccion.getText().toString();

                                            // Crea un Intent y agrega los datos utilizando putExtra()
                                            Intent intent = new Intent(docente_asistencias_list.this, docente_asistenciamodificar.class);
                                            intent.putExtra("usuario", usuario);
                                            intent.putExtra("curso", curso);
                                            intent.putExtra("cursoc", cursoc);
                                            intent.putExtra("cod", cod);
                                            intent.putExtra("nombreCompleto", Nombreco);
                                            intent.putExtra("numeroSeccion", seccion);
                                            startActivity(intent);

                                        }
                                    });

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

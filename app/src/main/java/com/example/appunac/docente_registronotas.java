package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class docente_registronotas extends AppCompatActivity {

    private TextView textViewUsuario, textViewCurso, textViewCursoc;
    TableLayout tableLayoutNotas;
    SearchView txtbuscar; // Agregamos la referencia al SearchView

    // Constantes de colores
    private static final int COLOR_ROJO = Color.RED;
    private static final int COLOR_AZUL = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_registronotas);

        textViewUsuario = findViewById(R.id.textView48);
        textViewCurso = findViewById(R.id.textView51);
        textViewCursoc = findViewById(R.id.textView52);
        tableLayoutNotas = findViewById(R.id.tableLayout3);
        txtbuscar = findViewById(R.id.txtbuscar); // Asignamos el SearchView

        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);

        String textView52 = textViewCursoc.getText().toString();
        String url = "https://www.productosjr.com/aplicativos/appunac/docenteregistrarnotascursos.php?docente_registronotas=" + cursoc;
        new ObtenerDatosAsyncTask().execute(url);

        // Configuramos el listener para el SearchView
        txtbuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Llama a la función para filtrar los resultados
                filtrarResultados(newText);
                return true;
            }
        });
    }

    private void mostrarCursos(JSONArray cursos) {
        try {
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject curso = cursos.getJSONObject(i);
                String alumnoId = curso.getString("codigo_alumno");
                String alumnoNom = curso.getString("nombre_alumno");
                String alumnoApeP = curso.getString("apellidoP_alumno");
                String alumnoApeM = curso.getString("apellidoM_alumno");
                String alumnoP1 = curso.getString("practica1");
                String alumnoPar = curso.getString("parcial");
                String alumnoP2 = curso.getString("practica2");
                String alumnoFin = curso.getString("final");
                String alumnoPro = curso.getString("promedio");

                alumnoP1 = alumnoP1.equals("null") ? "" : alumnoP1;
                alumnoPar = alumnoPar.equals("null") ? "" : alumnoPar;
                alumnoP2 = alumnoP2.equals("null") ? "" : alumnoP2;
                alumnoFin = alumnoFin.equals("null") ? "" : alumnoFin;
                alumnoPro = alumnoPro.equals("null") ? "" : alumnoPro;

                // Duplicar TableRow
                TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row_usuariosnotas, null);
                TextView textViewalumnoId = tableRow.findViewById(R.id.textcod);
                TextView textViewalumnoNom = tableRow.findViewById(R.id.textNombre);
                TextView textViewalumnoApeP = tableRow.findViewById(R.id.textApellidoP);
                TextView textViewalumnoApeM = tableRow.findViewById(R.id.textApellidoM);
                TextView textViewalumnoP1 = tableRow.findViewById(R.id.textView58);
                TextView textViewalumnoPar = tableRow.findViewById(R.id.textView59);
                TextView textViewalumnoP2 = tableRow.findViewById(R.id.textView60);
                TextView textViewalumnoFin = tableRow.findViewById(R.id.textView61);
                TextView textViewalumnoPro = tableRow.findViewById(R.id.textView62);

                textViewalumnoId.setText(alumnoId);
                textViewalumnoNom.setText(alumnoNom);
                textViewalumnoApeP.setText(alumnoApeP);
                textViewalumnoApeM.setText(alumnoApeM);
                textViewalumnoP1.setText(alumnoP1);
                textViewalumnoPar.setText(alumnoPar);
                textViewalumnoP2.setText(alumnoP2);
                textViewalumnoFin.setText(alumnoFin);
                textViewalumnoPro.setText(alumnoPro);

                // Establecer color para textViewalumnoP1
                if (!alumnoP1.isEmpty()) {
                    int notaP1 = Integer.parseInt(alumnoP1);
                    textViewalumnoP1.setTextColor(notaP1 < 50 ? COLOR_ROJO : COLOR_AZUL);
                }

                // Establecer color para textViewalumnoPar
                if (!alumnoPar.isEmpty()) {
                    int notaPar = Integer.parseInt(alumnoPar);
                    textViewalumnoPar.setTextColor(notaPar < 50 ? COLOR_ROJO : COLOR_AZUL);
                }

                // Establecer color para textViewalumnoP2
                if (!alumnoP2.isEmpty()) {
                    int notaP2 = Integer.parseInt(alumnoP2);
                    textViewalumnoP2.setTextColor(notaP2 < 50 ? COLOR_ROJO : COLOR_AZUL);
                }

                // Establecer color para textViewalumnoFin
                if (!alumnoFin.isEmpty()) {
                    int notaFin = Integer.parseInt(alumnoFin);
                    textViewalumnoFin.setTextColor(notaFin < 50 ? COLOR_ROJO : COLOR_AZUL);
                }

                // Establecer color para textViewalumnoPro
                if (!alumnoPro.isEmpty()) {
                    int notaPro = Integer.parseInt(alumnoPro);
                    textViewalumnoPro.setTextColor(notaPro < 50 ? COLOR_ROJO : COLOR_AZUL);
                }

                // Agregar OnClickListener a TableRow
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener los valores
                        String docente = textViewUsuario.getText().toString();
                        String cursoc = textViewCursoc.getText().toString();
                        String curso = textViewCurso.getText().toString();
                        String alumnoID = textViewalumnoId.getText().toString();
                        String alumnoNom = textViewalumnoNom.getText().toString();
                        String alumnoApeP = textViewalumnoApeP.getText().toString();
                        String alumnoApeM = textViewalumnoApeM.getText().toString();
                        String alumnoP1 = textViewalumnoP1.getText().toString();
                        String alumnoPar = textViewalumnoPar.getText().toString();
                        String alumnoP2 = textViewalumnoP2.getText().toString();
                        String alumnoFin = textViewalumnoFin.getText().toString();
                        String alumnoPro = textViewalumnoPro.getText().toString();




                        // Crear Intent para iniciar la actividad
                        Intent intent = new Intent(docente_registronotas.this, docente_modificarnota.class);
                        intent.putExtra("usuario", docente);
                        intent.putExtra("curso", curso);
                        intent.putExtra("cursoc", cursoc);
                        intent.putExtra("alumnoid", alumnoID);
                        intent.putExtra("alumnoNom", alumnoNom);
                        intent.putExtra("alumnoApeP", alumnoApeP);
                        intent.putExtra("alumnoApeM", alumnoApeM);
                        intent.putExtra("alumnoP1", alumnoP1);
                        intent.putExtra("alumnoPar", alumnoPar);
                        intent.putExtra("alumnoP2", alumnoP2);
                        intent.putExtra("alumnoFin", alumnoFin);
                        intent.putExtra("alumnoPro", alumnoPro);
                        startActivity(intent);
                        finish();
                    }
                });

                tableLayoutNotas.addView(tableRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class ObtenerDatosAsyncTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            JSONArray cursosJsonArray = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    return null;
                }

                String cursosJsonString = builder.toString();
                cursosJsonArray = new JSONArray(cursosJsonString);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return cursosJsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray cursosJsonArray) {
            super.onPostExecute(cursosJsonArray);
            if (cursosJsonArray != null) {
                mostrarCursos(cursosJsonArray);
            }
        }
    }

    // Función para filtrar los resultados
    private void filtrarResultados(String query) {
        for (int i = 1; i < tableLayoutNotas.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayoutNotas.getChildAt(i);
            TextView textNombre = row.findViewById(R.id.textNombre);
            TextView textApellidoP = row.findViewById(R.id.textApellidoP);
            TextView textApellidoM = row.findViewById(R.id.textApellidoM);

            // Comprueba si el texto de nombre, apellido paterno o apellido materno contiene la búsqueda
            String nombre = textNombre.getText().toString();
            String apellidoP = textApellidoP.getText().toString();
            String apellidoM = textApellidoM.getText().toString();

            if (nombre.toLowerCase().contains(query.toLowerCase()) ||
                    apellidoP.toLowerCase().contains(query.toLowerCase()) ||
                    apellidoM.toLowerCase().contains(query.toLowerCase())) {
                row.setVisibility(View.VISIBLE); // Muestra la fila si cumple con el criterio
            } else {
                row.setVisibility(View.GONE); // Oculta la fila si no cumple con el criterio
            }
        }
    }

}
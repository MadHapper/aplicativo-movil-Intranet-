package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class docente_curso extends AppCompatActivity {

    TextView textViewUsuario;
    TableLayout tableLayoutCursos;
    SearchView txtbuscar; // Agregamos la referencia al SearchView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_curso);

        textViewUsuario = findViewById(R.id.textView28);
        tableLayoutCursos = findViewById(R.id.tableLayout3);
        txtbuscar = findViewById(R.id.txtbuscar); // Asignamos el SearchView

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        String url = "https://www.productosjr.com/aplicativos/appunac/docentecursos.php?curso_docente=" + usuario;
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
                filtrarCursos(newText);
                return true;
            }
        });
    }

    private void mostrarCursos(JSONArray cursos) {
        try {
            for (int i = 0; i < cursos.length(); i++) {
                JSONObject curso = cursos.getJSONObject(i);
                String cursoId = curso.getString("curso_id");
                String cursoNombre = curso.getString("curso_nombre");
                String Fecha = curso.getString("curso_horario");

                // Duplicar TableRow
                TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row_curso, null);
                TextView textViewCursoc = tableRow.findViewById(R.id.codigocurso);
                TextView textViewCurso = tableRow.findViewById(R.id.nombrecurso);
                TextView textViewFecha = tableRow.findViewById(R.id.Fecha);

                textViewCursoc.setText(cursoId);
                textViewCurso.setText(cursoNombre);
                textViewFecha.setText(Fecha);

                // Agregar OnClickListener a TableRow
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener los valores de codigocurso y nombrecurso
                        String cursoc = textViewCursoc.getText().toString();
                        String curso = textViewCurso.getText().toString();
                        String usuario = textViewUsuario.getText().toString();

                        // Crear Intent para iniciar la actividad InglesBasico1.java
                        Intent intent = new Intent(docente_curso.this, docente_ingles.class);
                        intent.putExtra("usuario", usuario);
                        intent.putExtra("curso", curso);
                        intent.putExtra("cursoc", cursoc);
                        startActivity(intent);
                    }
                });

                tableLayoutCursos.addView(tableRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void filtrarCursos(String query) {
        for (int i = 1; i < tableLayoutCursos.getChildCount(); i++) {
            TableRow row = (TableRow) tableLayoutCursos.getChildAt(i);
            TextView textNombreCurso = row.findViewById(R.id.nombrecurso);

            // Comprueba si el texto de nombrecurso contiene la búsqueda
            String nombreCurso = textNombreCurso.getText().toString();

            if (nombreCurso.toLowerCase().contains(query.toLowerCase())) {
                row.setVisibility(View.VISIBLE); // Muestra la fila si cumple con el criterio
            } else {
                row.setVisibility(View.GONE); // Oculta la fila si no cumple con el criterio
            }
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
}
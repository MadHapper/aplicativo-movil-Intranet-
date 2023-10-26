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

public class admin_listacursos extends AppCompatActivity {

    TextView textViewUsuario;
    TableLayout tableLayoutCursos;
    SearchView txtBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listacursos);

        textViewUsuario = findViewById(R.id.nombread);
        tableLayoutCursos = findViewById(R.id.tableLayout4);
        txtBuscar = findViewById(R.id.txtbuscar);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        String url = "https://www.productosjr.com/aplicativos/appunac/admincursos.php";
        new ObtenerDatosAsyncTask().execute(url);

        txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
                        Intent intent = new Intent(admin_listacursos.this, admin_ingles.class);
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

    private void filtrarCursos(String filtro) {
        for (int i = 0; i < tableLayoutCursos.getChildCount(); i++) {
            View view = tableLayoutCursos.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                TextView textViewCurso = row.findViewById(R.id.nombrecurso);
                String nombreCurso = textViewCurso.getText().toString();
                if (nombreCurso.toLowerCase().contains(filtro.toLowerCase())) {
                    row.setVisibility(View.VISIBLE);
                } else {
                    row.setVisibility(View.GONE);
                }
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

    public void crearcurso(View view) {
        startActivity(new Intent(getApplicationContext(), admin_cursonuevo.class));
    }

}

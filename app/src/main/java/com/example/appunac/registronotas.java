package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class registronotas extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc, textView29, textView31, textView36, textView38, textView39;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registronotas);

        textViewUsuario = findViewById(R.id.textView42);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.textView13);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        textViewCursoc = findViewById(R.id.textView43);
        String cursoc = getIntent().getStringExtra("cursoc");
        textViewCursoc.setText(cursoc);

        textView29 = findViewById(R.id.textView29);
        textView31 = findViewById(R.id.textView31);
        textView36 = findViewById(R.id.textView36);
        textView38 = findViewById(R.id.textView38);
        textView39 = findViewById(R.id.textView39);

        // Obtener el nombre de la tabla
        String tabla = "tabla_" + cursoc;

        // Realizar la conexión a la base de datos mediante el archivo PHP
        String url = "https://www.productosjr.com/aplicativos/appunac/registronotas_curso.php?tabla=" + tabla;
        ObtenerDatosAsyncTask obtenerDatosAsyncTask = new ObtenerDatosAsyncTask();
        obtenerDatosAsyncTask.execute(url);

    }
    private class ObtenerDatosAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            String url = urls[0];

            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mostrarDatos(result);
        }
    }

    private void mostrarDatos(String result) {
        try {
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String practica1 = jsonObject.getString("practica1");
                String parcial = jsonObject.getString("parcial");
                String practica2 = jsonObject.getString("practica2");
                String finalScore = jsonObject.getString("final");
                String promedio = jsonObject.getString("promedio");

                // Actualizar los TextViews con los valores obtenidos
                textView29.setText(practica1);
                textView31.setText(parcial);
                textView36.setText(practica2);
                textView38.setText(finalScore);
                textView39.setText(promedio);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(registronotas.this, InglesBasico1.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

}
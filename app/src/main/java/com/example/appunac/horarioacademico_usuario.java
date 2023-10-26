package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class horarioacademico_usuario extends AppCompatActivity {

    TextView textViewUsuario, textCursoNombre1, textHorario1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarioacademico_usuario);

        textViewUsuario = findViewById(R.id.usu_correo);
        textCursoNombre1 = findViewById(R.id.textCursoNombre1);
        textHorario1 = findViewById(R.id.textHorario1);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        // Llama a la función para cargar los datos desde la base de datos
        cargarDatosDesdeBD(usuario);
    }

// ...

    // Función para cargar los datos desde la base de datos
    private void cargarDatosDesdeBD(String usuario) {
        // Realiza una solicitud HTTP para obtener datos desde tu servidor PHP
        // y actualiza los TextView con los datos recuperados.
        // Puedes usar AsyncTask o Volley para realizar la solicitud.

        String url = "https://www.productosjr.com/aplicativos/appunac/usuariohorario.php?usu_correo=" + usuario;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        if (jsonArray.length() > 0) {
                            JSONObject data = jsonArray.getJSONObject(0);
                            String cursoNombre = data.getString("curso_nombre");
                            String cursoHorario = data.getString("curso_horario");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textCursoNombre1.setText(cursoNombre);
                                    textHorario1.setText(cursoHorario);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(url);
    }

    // ...

}

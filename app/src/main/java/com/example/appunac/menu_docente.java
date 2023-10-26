package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class menu_docente extends AppCompatActivity {

    TextView textViewUsuario;
    TextView textNombre, textApellidoP, textApellidoM, textView59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_docente);

        textViewUsuario = findViewById(R.id.textView9);
        textNombre = findViewById(R.id.textNombre);
        textApellidoP = findViewById(R.id.textApellidoP);
        textApellidoM = findViewById(R.id.textApellidoM);
        textView59 = findViewById(R.id.textView59);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        // Realizar la conexión y obtener los datos del usuario
        obtenerDatosUsuario(usuario);
    }

    private void obtenerDatosUsuario(String correo) {
        String url = "https://www.productosjr.com/aplicativos/appunac/menuuruario.php?correo=" + correo;

        // Realizar la solicitud HTTP en segundo plano utilizando AsyncTask
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder result = new StringBuilder();

                try {
                    URL apiUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();
                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return result.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);

                    // Verificar si se obtuvieron resultados
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        // Obtener los valores del usuario
                        String nombre = jsonObject.getString("usu_nombre");
                        String apellidoP = jsonObject.getString("usu_apellidoP");
                        String apellidoM = jsonObject.getString("usu_apellidoM");
                        String codigo = jsonObject.getString("usu_codigo");

                        // Actualizar los TextView con los valores obtenidos
                        textNombre.setText(nombre);
                        textApellidoP.setText(apellidoP);
                        textApellidoM.setText(apellidoM);
                        textView59.setText(codigo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void abrirMiscursos (View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menu_docente.this, docente_curso.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

}
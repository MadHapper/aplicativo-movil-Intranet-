package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class admin_cursonuevo extends AppCompatActivity {

    private EditText editTextNombreCurso;
    private EditText editTextFechaHora;
    private Button buttonCrearCurso;
    private LinearLayout listdocentes;

    private List<CheckBox> checkBoxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cursonuevo);

        editTextNombreCurso = findViewById(R.id.editTextnombrecurso);
        editTextFechaHora = findViewById(R.id.editTextfechayhora);
        buttonCrearCurso = findViewById(R.id.crearcurso);
        listdocentes = findViewById(R.id.listdocentes);

        checkBoxList = new ArrayList<>();

        buttonCrearCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevoCurso();
            }
        });

        new FetchUsuariosTask().execute();
    }

    private void crearNuevoCurso() {
        String nombreCurso = editTextNombreCurso.getText().toString().trim();
        String fechaHora = editTextFechaHora.getText().toString().trim();
        String docenteSeleccionado = null;

        for (CheckBox checkBox : checkBoxList) {
            if (checkBox.isChecked()) {
                docenteSeleccionado = checkBox.getTag().toString();
                break;
            }
        }

        if (nombreCurso.isEmpty() || fechaHora.isEmpty() || docenteSeleccionado == null) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new CrearCursoTask().execute(nombreCurso, docenteSeleccionado, fechaHora);
    }

    private class FetchUsuariosTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // URL del script PHP que consulta la base de datos
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/docentelist.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Leer la respuesta del servidor
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // Retornar la respuesta como una cadena de texto
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Parsear la respuesta JSON
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String correo = jsonObject.getString("usu_correo");
                        String nombre = jsonObject.getString("usu_nombre");
                        String apellidoP = jsonObject.getString("usu_apellidoP");
                        String apellidoM = jsonObject.getString("usu_apellidoM");

                        // Crear CheckBox y asignar texto y correo
                        CheckBox checkBox = new CheckBox(admin_cursonuevo.this);
                        checkBox.setText(nombre + " " + apellidoP + " " + apellidoM);
                        checkBox.setTag(correo);
                        checkBox.setTextColor(Color.BLACK);

                        // Agregar CheckBox al LinearLayout y a la lista
                        listdocentes.addView(checkBox);
                        checkBoxList.add(checkBox);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class CrearCursoTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String nombreCurso = params[0];
            String docenteCorreo = params[1];
            String fechaHora = params[2];

            try {
                // URL del script PHP que crea un nuevo curso
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/crearcursod.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Parámetros de la solicitud POST
                String postData = "curso_nombre=" + nombreCurso
                        + "&curso_docente=" + docenteCorreo
                        + "&curso_horario=" + fechaHora;

                // Enviar los parámetros al servidor
                connection.getOutputStream().write(postData.getBytes());

                // Obtener la respuesta del servidor
                int responseCode = connection.getResponseCode();

                // Retornar true si se creó correctamente el curso (código de respuesta 200)
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(admin_cursonuevo.this, "El curso fue creado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(admin_cursonuevo.this, "Error al crear el curso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancelar(View view) {
        finish();
    }
}

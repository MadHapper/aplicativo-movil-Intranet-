package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class admin_modificarcurso extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso;
    EditText editTextnombrecurso, editTextfechayhora;
    LinearLayout listdocentes;
    List<CheckBox> checkBoxList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modificarcurso);

        textViewUsuario = findViewById(R.id.textView2);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.nombread);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        editTextnombrecurso = findViewById(R.id.editTextnombrecurso);
        editTextfechayhora = findViewById(R.id.editTextfechayhora);
        listdocentes = findViewById(R.id.listdocentes);

        checkBoxList = new ArrayList<>();

        // Utilizar FetchDocentesTask para obtener el listado de docentes
        new FetchDocentesTask().execute();

        String cursoc = getIntent().getStringExtra("cursoc");

        // Realizar la solicitud HTTP para obtener los datos del curso
        new GetCursoDataTask().execute(cursoc);

        Button buttonCrearCurso = findViewById(R.id.crearcurso);
        buttonCrearCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarCurso();
            }
        });
    }

    private void modificarCurso() {
        String nombreCurso = editTextnombrecurso.getText().toString().trim();
        String fechaHora = editTextfechayhora.getText().toString().trim();
        String docenteSeleccionado = null;

        for (CheckBox checkBox : checkBoxList) {
            if (checkBox.isChecked()) {
                docenteSeleccionado = checkBox.getTag().toString();
                break;
            }
        }

        String cursoId = textViewCurso.getText().toString().trim();

        if (nombreCurso.isEmpty() || fechaHora.isEmpty() || docenteSeleccionado == null) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        new ModificarCursoTask().execute(cursoId, nombreCurso, docenteSeleccionado, fechaHora);
    }

    private class FetchDocentesTask extends AsyncTask<Void, Void, String> {

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
                        CheckBox checkBox = new CheckBox(admin_modificarcurso.this);
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

    private class GetCursoDataTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String cursoId = params[0];
            String urlStr = "https://www.productosjr.com/aplicativos/appunac/cursomodificar.php";

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(("curso_id=" + cursoId).getBytes());
                os.flush();
                os.close();

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();

                return new JSONObject(result.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    int success = result.getInt("success");
                    if (success == 1) {
                        String cursoNombre = result.getString("curso_nombre");
                        String cursoHorario = result.getString("curso_horario");
                        editTextnombrecurso.setText(cursoNombre);
                        editTextfechayhora.setText(cursoHorario);
                    } else {
                        editTextnombrecurso.setText("");
                        editTextfechayhora.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ModificarCursoTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String cursoId = params[0];
            String nombreCurso = params[1];
            String docenteCorreo = params[2];
            String fechaHora = params[3];

            try {
                // URL del script PHP que modifica el curso
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/modificarcursod.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Parámetros de la solicitud POST
                String postData = "curso_id=" + cursoId
                        + "&curso_nombre=" + nombreCurso
                        + "&curso_docente=" + docenteCorreo
                        + "&curso_horario=" + fechaHora;

                // Enviar los parámetros al servidor
                connection.getOutputStream().write(postData.getBytes());

                // Obtener la respuesta del servidor
                int responseCode = connection.getResponseCode();

                // Retornar true si se modificó correctamente el curso (código de respuesta 200)
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(admin_modificarcurso.this, "El curso fue modificado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(admin_modificarcurso.this, "Error al modificar el curso", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void cancelar(View view) {
        finish();
    }

}

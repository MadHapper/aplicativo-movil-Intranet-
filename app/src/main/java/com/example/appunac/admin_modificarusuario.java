package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class admin_modificarusuario extends AppCompatActivity {
    // Declaraciones de vistas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modificarusuario);


        String correo = getIntent().getStringExtra("correo");
        obtenerDatosUsuario(correo);


        Button btnModificar = findViewById(R.id.modificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarUsuario();
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void obtenerDatosUsuario(String correo) {
        // Realizar una solicitud HTTP para obtener los datos del usuario
        String url = "https://www.productosjr.com/aplicativos/appunac/adminmostrarusuario.php?correo=" + correo;

        // Puedes usar una biblioteca como Volley o Retrofit para realizar la solicitud HTTP.
        // Aquí se muestra un ejemplo usando AsyncTask:

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject userData = new JSONObject(result);

                        // Actualizar las vistas con los datos del usuario
                        EditText usuCodigo = findViewById(R.id.usu_codigo);
                        usuCodigo.setText(userData.getString("usu_codigo"));

                        EditText usuCorreo = findViewById(R.id.usu_correo);
                        usuCorreo.setText(userData.getString("usu_correo"));

                        EditText usuPassword = findViewById(R.id.usu_password);
                        usuPassword.setText(userData.getString("usu_password"));

                        EditText usuNombre = findViewById(R.id.usu_nombre);
                        usuNombre.setText(userData.getString("usu_nombre"));

                        EditText usuApellidoP = findViewById(R.id.usu_apellidoP);
                        usuApellidoP.setText(userData.getString("usu_apellidoP"));

                        EditText usuApellidoM = findViewById(R.id.usu_apellidoM);
                        usuApellidoM.setText(userData.getString("usu_apellidoM"));

                        RadioGroup usuGrado = findViewById(R.id.usu_grado);
                        String grado = userData.getString("usu_grado");
                        if (grado.equals("Alumno Pre-Grado")) {
                            usuGrado.check(R.id.AlumnoPre);
                        } else if (grado.equals("Alumno Post-Grado")) {
                            usuGrado.check(R.id.AlumnoPost);
                        } else if (grado.equals("Alumno General")) {
                            usuGrado.check(R.id.AlumnoGeneral);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute(url);
    }
    private void modificarUsuario() {
        // Obtener los datos de los EditText y el RadioGroup
        String usuCodigo = ((EditText) findViewById(R.id.usu_codigo)).getText().toString();
        String usuCorreo = ((EditText) findViewById(R.id.usu_correo)).getText().toString();
        String usuPassword = ((EditText) findViewById(R.id.usu_password)).getText().toString();
        String usuNombre = ((EditText) findViewById(R.id.usu_nombre)).getText().toString();
        String usuApellidoP = ((EditText) findViewById(R.id.usu_apellidoP)).getText().toString();
        String usuApellidoM = ((EditText) findViewById(R.id.usu_apellidoM)).getText().toString();

        RadioGroup usuGrado = findViewById(R.id.usu_grado);
        String grado = "";
        int selectedId = usuGrado.getCheckedRadioButtonId();
        if (selectedId == R.id.AlumnoPre) {
            grado = "Alumno Pre-Grado";
        } else if (selectedId == R.id.AlumnoPost) {
            grado = "Alumno Post-Grado";
        } else if (selectedId == R.id.AlumnoGeneral) {
            grado = "Alumno General";
        }

        // Realizar una solicitud HTTP para modificar los datos del usuario
        String url = "https://www.productosjr.com/aplicativos/appunac/adminmodificarusuario.php";
        String finalGrado = grado;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Construir los parámetros POST
                    String postData = "usu_codigo=" + usuCodigo + "&usu_correo=" + usuCorreo + "&usu_password=" + usuPassword
                            + "&usu_nombre=" + usuNombre + "&usu_apellidoP=" + usuApellidoP + "&usu_apellidoM=" + usuApellidoM
                            + "&usu_grado=" + finalGrado;

                    DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                    os.writeBytes(postData);
                    os.flush();
                    os.close();

                    // Leer la respuesta del servicio web
                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    // Procesar la respuesta
                    if (result.contains("modificado")) {
                        // Mostrar un mensaje de éxito
                        mostrarMensaje("Los datos fueron modificados");
                    } else {
                        // Mostrar un mensaje de error si no se completó la modificación
                        mostrarMensaje("Error al modificar los datos del usuario");
                    }
                }
            }
        }.execute(url);

    }

    public void Cancelar(View view) {
        finish();
    }
}

package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class admin_registroacademico extends AppCompatActivity {

    EditText editTextURLPDF;
    Button buttonModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registroacademico);

        editTextURLPDF = findViewById(R.id.editTextURLPDF);
        buttonModificar = findViewById(R.id.button5);

        // Obtener y mostrar la URL actual desde la base de datos al iniciar la actividad
        obtenerURLRepositorio();

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el nuevo valor del EditText
                String nuevaURL = editTextURLPDF.getText().toString();

                // Llamar a la función para actualizar la URL en la base de datos
                actualizarURLRepositorio(nuevaURL);
            }
        });
    }

    private void obtenerURLRepositorio() {
        String url = "https://www.productosjr.com/aplicativos/appunac/adminregistropdf.php";

        // Realizar una solicitud GET para obtener la URL actual
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener la URL actual desde la respuesta JSON
                            String urlActual = response.getString("url_repositorio");

                            // Mostrar la URL actual en el EditText
                            editTextURLPDF.setText(urlActual);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void actualizarURLRepositorio(final String nuevaURL) {
        // Crear una solicitud POST para enviar la nueva URL al servidor
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://www.productosjr.com/aplicativos/appunac/adminregistropdfnew.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor, por ejemplo, mostrar un mensaje de éxito
                        Toast.makeText(getApplicationContext(), "URL actualizada con éxito", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud, por ejemplo, mostrar un mensaje de error
                        Toast.makeText(getApplicationContext(), "Error al actualizar la URL", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parámetros POST que se enviarán al servidor (nombre de la columna y nueva URL)
                Map<String, String> params = new HashMap<>();
                params.put("columna_url", nuevaURL);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest);
    }
}

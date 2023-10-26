package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class menuUsuario extends AppCompatActivity {

    TextView textViewUsuario;
    TextView textNombre, textApellidoP, textApellidoM, textView59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

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

        // Realizar la solicitud HTTP utilizando Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(stringRequest);
    }

    //abrir deudas y pagos
    public void abrirPagos(View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String codigo = textView59.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menuUsuario.this, Deudas_usuario.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("codigo", codigo);
        startActivity(intent);
    }

    //abre mis cursos
    public void abrirMiscursos(View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menuUsuario.this, cursos_usuario.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    //abre matricula
    public void abrirMatricula(View view) {
        String usuario = textViewUsuario.getText().toString();

        Intent intent = new Intent(menuUsuario.this, Matricula_usuario.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    //abre horario academico
    public void abrirhorarioacademico(View view) {
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menuUsuario.this, horarioacademico_usuario.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    //abre registro academico
    public void abrirRegistroAcademico(View view) {
        // Obtén la URL del repositorio desde la base de datos
        obtenerUrlRepositorio();
    }

    public void abrirtramites(View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String codigo = textView59.getText().toString();
        String nombre = textNombre.getText().toString();
        String apellidoP = textApellidoP.getText().toString();
        String apellidoM = textApellidoM.getText().toString();


        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(menuUsuario.this, usuario_tramites.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("codigo", codigo);
        intent.putExtra("nombre", nombre);
        intent.putExtra("apellidoP", apellidoP);
        intent.putExtra("apellidoM", apellidoM);
        startActivity(intent);
    }

    private void obtenerUrlRepositorio() {
        // La URL del archivo PHP que obtiene la URL del repositorio
        String url = "https://www.productosjr.com/aplicativos/appunac/url.php";

        // Realizar la solicitud HTTP utilizando Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Parsear la respuesta JSON para obtener la URL
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Obtener la URL del repositorio
                            String urlRepositorio = jsonObject.getString("url_repositorio");

                            // Verificar que la URL no esté vacía
                            if (!urlRepositorio.isEmpty()) {
                                // Abrir la URL en un navegador
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlRepositorio));
                                startActivity(intent);
                            } else {
                                // La URL está vacía, maneja el caso de error aquí
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        // Maneja el error de la solicitud HTTP aquí
                    }
                });

        requestQueue.add(stringRequest);
    }

}

package com.example.appunac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Matricula_usuario extends AppCompatActivity {

    TextView textViewUsuario;
    TextView textNombre, textApellidoP, textApellidoM, textView59, textViewusu_grado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matricula_usuario);

        textViewUsuario = findViewById(R.id.textView9);
        textNombre = findViewById(R.id.textNombre);
        textApellidoP = findViewById(R.id.textApellidoP);
        textApellidoM = findViewById(R.id.textApellidoM);
        textView59 = findViewById(R.id.textView59);
        textViewusu_grado = findViewById(R.id.usu_grado);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        // Realizar la conexión y obtener los datos del usuario
        obtenerDatosUsuario(usuario);
    }

    private void obtenerDatosUsuario(String correo) {
        String url = "https://www.productosjr.com/aplicativos/appunac/menuuruariomatricula.php?correo=" + correo;

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
                                String grado = jsonObject.getString("usu_grado");

                                // Actualizar los TextView con los valores obtenidos
                                textNombre.setText(nombre+" "+apellidoP+" "+apellidoM);
                                textView59.setText(codigo);
                                textViewusu_grado.setText(grado);
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

    public void abrirMatriculaPago(View view) {
        // Obtén el valor del TextView
        String codigo = textView59.getText().toString();
        String nombre = textNombre.getText().toString();
        String apellidoP = textApellidoP.getText().toString();
        String apellidoM = textApellidoM.getText().toString();
        String usuario = textViewUsuario.getText().toString();
        String grado = textViewusu_grado.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(Matricula_usuario.this, usuario_pagar.class);
        intent.putExtra("codigo", codigo);
        intent.putExtra("nombre", nombre);
        intent.putExtra("apellidoP", apellidoP);
        intent.putExtra("apellidoM", apellidoM);
        intent.putExtra("usuario", usuario);
        intent.putExtra("grado", grado);
        startActivity(intent);
    }

    public void abrirMatriculaCurso (View view) {
        // Obtén el valor del TextView
        String codigo = textView59.getText().toString();
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(Matricula_usuario.this, MatriculaCurso_usuario.class);
        intent.putExtra("codigo", codigo);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }


}
package com.example.appunac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edtUsuario, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtUsuario = findViewById(R.id.edtUsuario);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario("https://www.productosjr.com/aplicativos/appunac/validar_usuario.php");
            }
        });
    }

    private void validarUsuario(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int usuGrado = jsonObject.getInt("usu_cargo");
                        Intent intent;

                        switch (usuGrado) {
                            case 1: // Usuario
                                intent = new Intent(getApplicationContext(), menuUsuario.class);
                                intent.putExtra("usuario", edtUsuario.getText().toString());
                                startActivity(intent);
                                break;
                            case 2: // Docente
                                intent = new Intent(getApplicationContext(), menu_docente.class);
                                intent.putExtra("usuario", edtUsuario.getText().toString());
                                startActivity(intent);
                                break;
                            case 3: // Administrador
                                intent = new Intent(getApplicationContext(), menu_administrador.class);
                                intent.putExtra("usuario", edtUsuario.getText().toString());
                                startActivity(intent);
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Grado de usuario desconocido", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("usuario", edtUsuario.getText().toString());
                parametros.put("password", edtPassword.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void abrirregistro(View view) {
        startActivity(new Intent(getApplicationContext(), registro.class));
        finish();
    }
}

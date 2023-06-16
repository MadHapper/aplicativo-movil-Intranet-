package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class admin_registrarnuevo extends AppCompatActivity {

    private static final String URL_REGISTRO = "https://www.productosjr.com/aplicativos/appunac/conexion_adminregistro.php";

    private EditText edtNombre;
    private EditText edtApellidoPaterno;
    private EditText edtApellidoMaterno;
    private EditText edtCodigo;
    private EditText edtCorreo;
    private EditText edtContraseña;
    private EditText edtCargo;
    private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registrarnuevo);

        edtNombre = findViewById(R.id.edtNombreNew);
        edtApellidoPaterno = findViewById(R.id.edtApellidoPaternoNew);
        edtApellidoMaterno = findViewById(R.id.edtApellidoMaternoNew);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtCorreo = findViewById(R.id.edtCorreoNew);
        edtContraseña = findViewById(R.id.edtContraseñaNew);
        edtCargo = findViewById(R.id.edtCargo);
        btnRegistro = findViewById(R.id.btnregistro);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = edtNombre.getText().toString().trim();
        String apellidoPaterno = edtApellidoPaterno.getText().toString().trim();
        String apellidoMaterno = edtApellidoMaterno.getText().toString().trim();
        String codigo = edtCodigo.getText().toString().trim();
        String correo = edtCorreo.getText().toString().trim();
        String contraseña = edtContraseña.getText().toString().trim();
        String cargo = edtCargo.getText().toString().trim();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() ||
                codigo.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || cargo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(admin_registrarnuevo.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(admin_registrarnuevo.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(admin_registrarnuevo.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("usu_nombre", nombre);
                params.put("usu_apellidoP", apellidoPaterno);
                params.put("usu_apellidoM", apellidoMaterno);
                params.put("usu_codigo", codigo);
                params.put("usu_correo", correo);
                params.put("usu_password", contraseña);
                params.put("usu_cargo", cargo); // Nuevo campo "Cargo"

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

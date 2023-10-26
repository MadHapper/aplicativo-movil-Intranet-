package com.example.appunac;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class registro extends AppCompatActivity {

    private static final String URL_REGISTRO = "https://www.productosjr.com/aplicativos/appunac/conexion_registro.php";

    private EditText edtNombre;
    private EditText edtApellidoPaterno;
    private EditText edtApellidoMaterno;
    private EditText edtCodigo;
    private EditText edtCorreo;
    private EditText edtContraseña;
    private Button btnRegistro;

    private RadioGroup radioGroupCursos;
    private TextView idgrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre = findViewById(R.id.edtNombreNew);
        edtApellidoPaterno = findViewById(R.id.edtApellidoPaternoNew);
        edtApellidoMaterno = findViewById(R.id.edtApellidoMaternoNew);
        edtCodigo = findViewById(R.id.edtCodigo);
        edtCorreo = findViewById(R.id.edtCorreoNew);
        edtContraseña = findViewById(R.id.edtContraseñaNew);
        btnRegistro = findViewById(R.id.btnregistro);

        radioGroupCursos = findViewById(R.id.radioGroupCursos);
        idgrado = findViewById(R.id.idgrado);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        radioGroupCursos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Verifica cuál RadioButton está seleccionado y actualiza el TextView
                switch (checkedId) {
                    case R.id.AlumnoPre:
                        idgrado.setText("Alumno Pre-Grado");
                        break;
                    case R.id.AlumnoPost:
                        idgrado.setText("Alumno Post-Grado");
                        break;
                    case R.id.AlumnoGeneral:
                        idgrado.setText("Alumno General");
                        break;
                }
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
        String Idgardo = idgrado.getText().toString();

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() ||
                codigo.isEmpty() || correo.isEmpty() || contraseña.isEmpty() || Idgardo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(registro.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(registro.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(registro.this, "Error de conexión", Toast.LENGTH_SHORT).show();
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
                params.put("usu_cargo", "1");
                params.put("usu_grado", Idgardo);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void abrirmainactivity (View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

}
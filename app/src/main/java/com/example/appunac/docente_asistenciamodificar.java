package com.example.appunac;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class docente_asistenciamodificar extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc, textviewNombreco, textcod;
    TextView textnumeroSeccion;
    TextView textnumeroSecciontabla;
    Button btnModificar;
    RadioGroup radioGroupCursos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_asistenciamodificar);

        textViewUsuario = findViewById(R.id.alumnon);
        textViewCurso = findViewById(R.id.nombrec);
        textViewCursoc = findViewById(R.id.codigoc);
        textviewNombreco = findViewById(R.id.nombreComplese);
        textcod = findViewById(R.id.textcod);
        textnumeroSeccion = findViewById(R.id.numeroSeccion);
        textnumeroSecciontabla = findViewById(R.id.numeroSecciontabla);
        btnModificar = findViewById(R.id.modificar);
        radioGroupCursos = findViewById(R.id.radioGroupCursos);

        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");
        String nombrecompleto = getIntent().getStringExtra("nombreCompleto");
        String cod = getIntent().getStringExtra("cod");
        String numeroSeccion = getIntent().getStringExtra("numeroSeccion");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);
        textviewNombreco.setText(nombrecompleto);
        textcod.setText(cod);
        textnumeroSeccion.setText(numeroSeccion);

        String numeroSeccionSinEspacios = numeroSeccion.replace(" ", "");
        textnumeroSecciontabla.setText(numeroSeccionSinEspacios);

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroupCursos.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                String valorSeleccionado = selectedRadioButton.getText().toString();

                String url = "https://www.productosjr.com/aplicativos/appunac/docentemodificarasistencia.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Verificar si la modificación se realizó con éxito
                                if (response.trim().equals("Modificación exitosa")) {
                                    // Mostrar un mensaje de éxito
                                    Toast.makeText(getApplicationContext(), "Se ha modificado la asistencia correctamente", Toast.LENGTH_LONG).show();
                                } else {
                                    // Mostrar un mensaje de error
                                    Toast.makeText(getApplicationContext(), "Error al modificar asistencia", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejar errores de la solicitud HTTP
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("cursoc", textViewCursoc.getText().toString());
                        params.put("textcod", textcod.getText().toString());
                        params.put("numeroSecciontabla", textnumeroSecciontabla.getText().toString());
                        params.put("valorSeleccionado", valorSeleccionado);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        });
    }
}

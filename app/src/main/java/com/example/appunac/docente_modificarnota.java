package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

public class docente_modificarnota extends AppCompatActivity {

    private TextView textViewUsuario, textViewCurso, textViewCursoc, textViewAlumnoId, textViewalumnoNom, textViewalumnoApeP, textViewalumnoApeM, textViewalumnoP1, textViewalumnoPar, textViewalumnoP2, textViewalumnoFin, textViewalumnoPro;
    private EditText pc1EditText, parcialEditText, pc2EditText, efinalEditText;
    private TextView pfinalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_modificarnota);

        textViewUsuario = findViewById(R.id.usuario);
        textViewCurso = findViewById(R.id.curso);
        textViewCursoc = findViewById(R.id.cursoc);
        textViewAlumnoId = findViewById(R.id.codigo);
        textViewalumnoNom = findViewById(R.id.nombre);
        textViewalumnoApeP = findViewById(R.id.apellidoP);
        textViewalumnoApeM = findViewById(R.id.apellidoM);
        textViewalumnoP1 = findViewById(R.id.pc1);
        textViewalumnoPar = findViewById(R.id.parcial);
        textViewalumnoP2 = findViewById(R.id.pc2);
        textViewalumnoFin = findViewById(R.id.efinal);
        textViewalumnoPro = findViewById(R.id.Pfinal);

        pc1EditText = findViewById(R.id.pc1);
        parcialEditText = findViewById(R.id.parcial);
        pc2EditText = findViewById(R.id.pc2);
        efinalEditText = findViewById(R.id.efinal);
        pfinalTextView = findViewById(R.id.Pfinal);

        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");
        String alumnoId = getIntent().getStringExtra("alumnoid");
        String alumnoNom = getIntent().getStringExtra("alumnoNom");
        String alumnoApeP = getIntent().getStringExtra("alumnoApeP");
        String alumnoApeM = getIntent().getStringExtra("alumnoApeM");
        String alumnoP1 = getIntent().getStringExtra("alumnoP1");
        String alumnoPar = getIntent().getStringExtra("alumnoPar");
        String alumnoP2 = getIntent().getStringExtra("alumnoP2");
        String alumnoFin = getIntent().getStringExtra("alumnoFin");
        String alumnoPro = getIntent().getStringExtra("alumnoPro");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);
        textViewAlumnoId.setText(alumnoId);
        textViewalumnoNom.setText(alumnoNom);
        textViewalumnoApeP.setText(alumnoApeP);
        textViewalumnoApeM.setText(alumnoApeM);
        textViewalumnoP1.setText(alumnoP1);
        textViewalumnoPar.setText(alumnoPar);
        textViewalumnoP2.setText(alumnoP2);
        textViewalumnoFin.setText(alumnoFin);
        textViewalumnoPro.setText(alumnoPro);

        pc1EditText.addTextChangedListener(textWatcher);
        parcialEditText.addTextChangedListener(textWatcher);
        pc2EditText.addTextChangedListener(textWatcher);
        efinalEditText.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calcularPromedioFinal();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void calcularPromedioFinal() {
        String pc1Text = pc1EditText.getText().toString().trim();
        String parcialText = parcialEditText.getText().toString().trim();
        String pc2Text = pc2EditText.getText().toString().trim();
        String efinalText = efinalEditText.getText().toString().trim();

        if (!pc1Text.isEmpty() && !parcialText.isEmpty() && !pc2Text.isEmpty() && !efinalText.isEmpty()) {
            double pc1Value = Double.parseDouble(pc1Text);
            double parcialValue = Double.parseDouble(parcialText);
            double pc2Value = Double.parseDouble(pc2Text);
            double efinalValue = Double.parseDouble(efinalText);

            double promedioFinal = (pc1Value + parcialValue + pc2Value + efinalValue) / 4;
            pfinalTextView.setText(String.valueOf(promedioFinal));
        }
    }

    public void atras(View view) {
        finish();
    }

    public void modificar(View view) {
        String cursoc = textViewCursoc.getText().toString();
        String alumnoId = textViewAlumnoId.getText().toString();
        String pc1Value = pc1EditText.getText().toString().trim();
        String parcialValue = parcialEditText.getText().toString().trim();
        String pc2Value = pc2EditText.getText().toString().trim();
        String efinalValue = efinalEditText.getText().toString().trim();

        // Verificar si alguno de los valores está vacío
        if (TextUtils.isEmpty(pc1Value) || TextUtils.isEmpty(parcialValue) || TextUtils.isEmpty(pc2Value) || TextUtils.isEmpty(efinalValue)) {
            // Valores faltantes, mostrar mensaje de error
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double pc1Double = Double.parseDouble(pc1Value);
        double parcialDouble = Double.parseDouble(parcialValue);
        double pc2Double = Double.parseDouble(pc2Value);
        double efinalDouble = Double.parseDouble(efinalValue);

        if (pc1Double < 0 || pc1Double > 100 ||
                parcialDouble < 0 || parcialDouble > 100 ||
                pc2Double < 0 || pc2Double > 100 ||
                efinalDouble < 0 || efinalDouble > 100) {
            // Valores fuera del rango, mostrar mensaje de error
            Toast.makeText(this, "Los valores deben estar en el rango de 0 a 100.", Toast.LENGTH_SHORT).show();
            return;
        }

        calcularPromedioFinal();

        // Construir la URL de conexión
        String url = "https://www.productosjr.com/aplicativos/appunac/modificarnotas.php";
        String tabla = "tabla_" + cursoc;

        // Construir los parámetros de la solicitud HTTP
        HashMap<String, String> params = new HashMap<>();
        params.put("tabla", tabla);
        params.put("codigo_alumno", alumnoId);
        params.put("practica1", pc1Value);
        params.put("parcial", parcialValue);
        params.put("practica2", pc2Value);
        params.put("final", efinalValue);
        params.put("promedio", pfinalTextView.getText().toString());

        // Realizar la solicitud HTTP utilizando Volley o alguna otra biblioteca
        // Aquí se muestra un ejemplo utilizando Volley:
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // La solicitud se ha completado exitosamente
                        Toast.makeText(docente_modificarnota.this, "Modificación de notas realizada", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error en la solicitud HTTP
                        Toast.makeText(docente_modificarnota.this, "Error al modificar las notas", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley (o ejecutarla de alguna otra manera)
        Volley.newRequestQueue(this).add(request);
    }

    public void onBackPressed() {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso.getText().toString();
        String cursoc = textViewCursoc.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(docente_modificarnota.this, docente_registronotas.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("curso", curso);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

}


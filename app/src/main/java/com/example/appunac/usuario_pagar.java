package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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

public class usuario_pagar extends AppCompatActivity {

    TextView textNombre, textCodigo, textViewUsuario, textViewusu_grado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_pagar);

        textNombre = findViewById(R.id.NombreCompleto);
        textCodigo = findViewById(R.id.Usercode);
        textViewUsuario = findViewById(R.id.UsuarioView);
        textViewusu_grado = findViewById(R.id.usu_grados);
        CardView cardViewAlumnos = findViewById(R.id.cardViewAlumnos);

        cardViewAlumnos.setVisibility(View.GONE);

        String codigo = getIntent().getStringExtra("codigo");
        String nombre = getIntent().getStringExtra("nombre");
        String apellidoP = getIntent().getStringExtra("apellidoP");
        String apellidoM = getIntent().getStringExtra("apellidoM");
        String usuario = getIntent().getStringExtra("usuario");
        String grado = getIntent().getStringExtra("grado");

        textNombre.setText(nombre+" "+apellidoP+" "+apellidoM);
        textCodigo.setText(codigo);
        textViewUsuario.setText(usuario);
        textViewusu_grado.setText(grado);

        setAlumnoCheckBoxBasedOnGrados(grado);

        // Obtener referencias a los CheckBoxes
        CheckBox idiomaInglesCheckBox = findViewById(R.id.Ingles);
        CheckBox idiomaPortugesCheckBox = findViewById(R.id.Portuges);
        CheckBox idiomaFrancesCheckBox = findViewById(R.id.Frances);

        CheckBox cicloRegularCheckBox = findViewById(R.id.Regular);
        CheckBox cicloIntensivoCheckBox = findViewById(R.id.Intensivo);

        CheckBox alumnoPregradoCheckBox = findViewById(R.id.Pregardo);
        CheckBox alumnoPosgradoCheckBox = findViewById(R.id.Posgrado);
        CheckBox alumnoGeneralCheckBox = findViewById(R.id.General);

        CheckBox basicoCheckBox = findViewById(R.id.Basico);
        CheckBox intermedioCheckBox = findViewById(R.id.Intermedio);
        CheckBox avanzadoCheckBox = findViewById(R.id.Avanzado);

        // Asignar el Listener a cada CheckBox
        idiomaInglesCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        idiomaPortugesCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        idiomaFrancesCheckBox.setOnCheckedChangeListener(checkBoxClickListener);

        cicloRegularCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        cicloIntensivoCheckBox.setOnCheckedChangeListener(checkBoxClickListener);

        alumnoPregradoCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        alumnoPosgradoCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        alumnoGeneralCheckBox.setOnCheckedChangeListener(checkBoxClickListener);

        basicoCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        intermedioCheckBox.setOnCheckedChangeListener(checkBoxClickListener);
        avanzadoCheckBox.setOnCheckedChangeListener(checkBoxClickListener);

        // Actualizar el monto inicialmente, en caso de que algunos CheckBoxes estén marcados por defecto.
        updateMonto();

        Button btnAceptar = findViewById(R.id.Acceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link = ((TextView) findViewById(R.id.Link)).getText().toString();
                if (!link.isEmpty()) {
                    abrirEnlace(link);
                    realizarPago();
                }
            }
        });
    }

    private void abrirEnlace(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void realizarPago() {
        // Obtener referencias a los TextViews para obtener los valores de los datos a enviar
        TextView userCodeTextView = findViewById(R.id.Usercode);
        TextView nombreCompletoTextView = findViewById(R.id.NombreCompleto);
        TextView descripcionTextView = findViewById(R.id.Descripcion);
        TextView montoTextView = findViewById(R.id.monto);

        // Obtener los valores de los datos a enviar
        String codigoUsuario = userCodeTextView.getText().toString();
        String nombreCompleto = nombreCompletoTextView.getText().toString();
        String descripcion = descripcionTextView.getText().toString();
        String monto = montoTextView.getText().toString();

        // Realizar la solicitud POST al servidor para enviar los datos del pago
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.productosjr.com/aplicativos/appunac/pagarmatricula.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor, si es necesario
                        Toast.makeText(usuario_pagar.this, "Procesando Pago.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores en la solicitud
                        Toast.makeText(usuario_pagar.this, "Error al realizar el pago.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigouser_pago", codigoUsuario);
                params.put("nombreuser_pago", nombreCompleto);
                params.put("concepto_pago", "Matricula");
                params.put("descripcion_pago", descripcion);
                params.put("monto_pago", monto);
                return params;
            }
        };

        queue.add(postRequest);
    }


    private final CheckBox.OnCheckedChangeListener checkBoxClickListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // Si un CheckBox se marca, desmarca los otros CheckBoxes dentro del mismo LinearLayout
            if (isChecked) {
                uncheckOtherCheckBoxes((LinearLayout) buttonView.getParent(), buttonView.getId());
                // Actualizar el monto al marcar un CheckBox
                updateMonto();
            }
        }
    };

    private void uncheckOtherCheckBoxes(LinearLayout linearLayout, int selectedCheckBoxId) {
        // Recorrer los CheckBoxes dentro del LinearLayout
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View view = linearLayout.getChildAt(i);
            if (view instanceof CheckBox && view.getId() != selectedCheckBoxId) {
                CheckBox checkBox = (CheckBox) view;
                checkBox.setChecked(false);
            }
        }
    }

    private void updateMonto()
    {
        // Obtener referencias a los CheckBoxes que determinan el monto
        CheckBox cicloRegularCheckBox = findViewById(R.id.Regular);
        CheckBox cicloIntensivoCheckBox= findViewById(R.id.Intensivo);

        CheckBox alumnoPregradoCheckBox = findViewById(R.id.Pregardo);
        CheckBox alumnoPosgradoCheckBox = findViewById(R.id.Posgrado);
        CheckBox alumnoGeneralCheckBox = findViewById(R.id.General);

        CheckBox basicoCheckBox = findViewById(R.id.Basico);
        CheckBox intermedioCheckBox = findViewById(R.id.Intermedio);
        CheckBox avanzadoCheckBox = findViewById(R.id.Avanzado);

        // Obtener referencia al TextView para mostrar el monto
        TextView montoTextView = findViewById(R.id.monto);
        TextView linkTextView = findViewById(R.id.Link);
        TextView DescripcionTextView = findViewById(R.id.Descripcion);

        // Comprobar si los CheckBoxes están marcados y establecer el monto en el TextView

        //ciclo regular
        if (cicloRegularCheckBox.isChecked()  && alumnoPregradoCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/82,00"); linkTextView.setText("http://mpago.la/1Nf5bMd"); DescripcionTextView.setText("1"); }
        if (cicloRegularCheckBox.isChecked() && alumnoPregradoCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/92,00");linkTextView.setText("http://mpago.la/2YELfwM");DescripcionTextView.setText("2"); }
        if (cicloRegularCheckBox.isChecked() && alumnoPregradoCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/92,00");linkTextView.setText("http://mpago.la/1ZQBkKL");DescripcionTextView.setText("3"); }

        if (cicloRegularCheckBox.isChecked() && alumnoPosgradoCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/110,00");linkTextView.setText("http://mpago.la/1QEtEi3");DescripcionTextView.setText("4"); }
        if (cicloRegularCheckBox.isChecked() && alumnoPosgradoCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/120,00");linkTextView.setText("http://mpago.la/1CiTBqu");DescripcionTextView.setText("5"); }
        if (cicloRegularCheckBox.isChecked()  && alumnoPosgradoCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/120,00");linkTextView.setText("http://mpago.la/2ZnECzV");DescripcionTextView.setText("6"); }

        if (cicloRegularCheckBox.isChecked()  && alumnoGeneralCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/120,00");linkTextView.setText("http://mpago.la/19KqaPa");DescripcionTextView.setText("7"); }
        if (cicloRegularCheckBox.isChecked()  && alumnoGeneralCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/130,00");linkTextView.setText("http://mpago.la/2YFmHx5");DescripcionTextView.setText("8"); }
        if (cicloRegularCheckBox.isChecked()  && alumnoGeneralCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/130,00");linkTextView.setText("http://mpago.la/2JAQbxN");DescripcionTextView.setText("9"); }


        /// ciclo intensivo


        if (cicloIntensivoCheckBox.isChecked()  && alumnoPregradoCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/260,00");linkTextView.setText("http://mpago.la/2nf7QuK");DescripcionTextView.setText("28"); }
        if (cicloIntensivoCheckBox.isChecked()  && alumnoPregradoCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/280,00");linkTextView.setText("http://mpago.la/2tdt3qM");DescripcionTextView.setText("29"); }
        if (cicloIntensivoCheckBox.isChecked()  && alumnoPregradoCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/280,00");linkTextView.setText("http://mpago.la/1SfXBP9");DescripcionTextView.setText("30"); }

        if (cicloIntensivoCheckBox.isChecked() && alumnoPosgradoCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/220,00");linkTextView.setText("http://mpago.la/2Ljhu4L");DescripcionTextView.setText("31"); }
        if (cicloIntensivoCheckBox.isChecked() && alumnoPosgradoCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/240,00");linkTextView.setText("http://mpago.la/1GMEt3J");DescripcionTextView.setText("32"); }
        if (cicloIntensivoCheckBox.isChecked() && alumnoPosgradoCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/240,00");linkTextView.setText("http://mpago.la/2gDgf7y");DescripcionTextView.setText("33"); }

        if (cicloIntensivoCheckBox.isChecked() && alumnoGeneralCheckBox.isChecked() && basicoCheckBox.isChecked())
        {montoTextView.setText("S/280,00");linkTextView.setText("http://mpago.la/2e4Zsdp");DescripcionTextView.setText("34"); }
        if (cicloIntensivoCheckBox.isChecked() && alumnoGeneralCheckBox.isChecked() && intermedioCheckBox.isChecked())
        {montoTextView.setText("S/300,00");linkTextView.setText("http://mpago.la/1QegqYW");DescripcionTextView.setText("35"); }
        if (cicloIntensivoCheckBox.isChecked() && alumnoGeneralCheckBox.isChecked() && avanzadoCheckBox.isChecked())
        {montoTextView.setText("S/300,00");linkTextView.setText("http://mpago.la/2S1qQAC");DescripcionTextView.setText("36"); }
    }

    private void setAlumnoCheckBoxBasedOnGrados(String grados) {
        CheckBox pregradoCheckBox = findViewById(R.id.Pregardo);
        CheckBox posgradoCheckBox = findViewById(R.id.Posgrado);
        CheckBox generalCheckBox = findViewById(R.id.General);

        if (grados.equals("Alumno Pre-Grado")) {
            pregradoCheckBox.setChecked(true);
        } else if (grados.equals("Alumno Post-Grado")) {
            posgradoCheckBox.setChecked(true);
        } else if (grados.equals("Alumno General")) {
            generalCheckBox.setChecked(true);
        }
    }


    public void CancelarPago(View view) {
        finish();
    }

}

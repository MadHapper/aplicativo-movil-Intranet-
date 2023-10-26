package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class usuario_tramites extends AppCompatActivity {

    TextView textViewUsuario;
    TextView textViewContenido;
    TextView textNombre, textCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_tramites);

        textViewUsuario = findViewById(R.id.textView9);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);


        textNombre = findViewById(R.id.NombreCompleto);
        textCodigo = findViewById(R.id.Usercode);


        String codigo = getIntent().getStringExtra("codigo");
        String nombre = getIntent().getStringExtra("nombre");
        String apellidoP = getIntent().getStringExtra("apellidoP");
        String apellidoM = getIntent().getStringExtra("apellidoM");

        textNombre.setText(nombre+" "+apellidoP+" "+apellidoM);
        textCodigo.setText(codigo);


        textViewContenido = findViewById(R.id.contenido);
        String contenido = obtenerTexto();
        textViewContenido.setText(contenido);

        final String montoTramite1 = "S/ 50.00";
        final String montoTramite2 = "S/ 50.00";
        final String montoTramite3 = "S/ 30.00";
        final String montoTramite4 = "S/ 20.00";

        final String Tramite1 = "Certificado";
        final String Tramite2 = "Duplicado de certificado";
        final String Tramite3 = "Constancia de notas";
        final String Tramite4 = "Constancia de matricula";

        final String TramiteLink1 = "http://mpago.la/1tcwyWx";
        final String TramiteLink2 = "http://mpago.la/2KMqKSW";
        final String TramiteLink3 = "http://mpago.la/2q5C1vo";
        final String TramiteLink4 = "http://mpago.la/1wxkM1w";

        CardView cardViewTramite1 = findViewById(R.id.cardView1);
        CardView cardViewTramite2 = findViewById(R.id.cardView2);
        CardView cardViewTramite3 = findViewById(R.id.cardView3);
        CardView cardViewTramite4 = findViewById(R.id.cardVieww4);

        cardViewTramite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewMonto = findViewById(R.id.monto);
                TextView textViewtramite = findViewById(R.id.tramite);
                TextView textViewlink = findViewById(R.id.link);
                textViewMonto.setText(montoTramite1);
                textViewtramite.setText(Tramite1);
                textViewlink.setText(TramiteLink1);
            }
        });

        cardViewTramite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewMonto = findViewById(R.id.monto);
                TextView textViewtramite = findViewById(R.id.tramite);
                TextView textViewlink = findViewById(R.id.link);
                textViewMonto.setText(montoTramite2);
                textViewtramite.setText(Tramite2);
                textViewlink.setText(TramiteLink2);
            }
        });

        cardViewTramite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewMonto = findViewById(R.id.monto);
                TextView textViewtramite = findViewById(R.id.tramite);
                TextView textViewlink = findViewById(R.id.link);
                textViewMonto.setText(montoTramite3);
                textViewtramite.setText(Tramite3);
                textViewlink.setText(TramiteLink3);
            }
        });

        cardViewTramite4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textViewMonto = findViewById(R.id.monto);
                TextView textViewtramite = findViewById(R.id.tramite);
                TextView textViewlink = findViewById(R.id.link);
                textViewMonto.setText(montoTramite4);
                textViewtramite.setText(Tramite4);
                textViewlink.setText(TramiteLink4);
            }
        });

        Button btnPagar = findViewById(R.id.button3);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textViewLink = findViewById(R.id.link);
                String link = textViewLink.getText().toString();
                if (!link.isEmpty()) {
                    abrirEnlace(link);
                    enviarDatosPago();
                }
            }
        });
    }

    public String obtenerTexto() {
        String texto = "Para realizar en trámite es importante adjuntar las siguientes documentos:\n\n"
                + "1.- Recibo ORIGINAL de SCOTIABANK y UNA COPIA. En caso de pagar en ventanilla SERÁ AL SERVICIO 112, "
                + "y si será por aplicativo PONDRA EMPRESA: UNIV DEL CALLAO / SERVICIO:CENTRO DE IDIOMAS.\n\n"
                + "2.- EN CASO QUE EL RECIBO SEA VIRTUAL PRESENTARÁ 2 IMPRESIONES.\n\n"
                + "3.- SI SOLO REQUIERO CERTIFICADO SE REALIZARÁ EL PAGO DEL PRIMER MONTO.\n"
                + "    - Certificado S/. 50.00 N de Recibo\n"
                + "    - Duplicado de certificado S/. 50.00 N de Recibo\n"
                + "    - Constancia de notas S/. 30.00 N de Recibo\n"
                + "    - Constancia de matrícula S/. 20.00 N de Recibo\n\n"
                + "4.- Trabajadores de la UNAC CAS Y NOMBRADOS A pagan el 20% presentando Constancia del Trabajador (original) "
                + "otorgado por la Oficina de Personal. !!PRESENTAR TODA LA DOCUMENTACIÓN EN FOLDER MANILA SIN FASTENERS!!\n\n"
                + "5.- Fotocopia del documento de identidad y 01 fotocopia de esta solicitud completo (CARGO)";

        return texto;
    }

    private void abrirEnlace(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void enviarDatosPago() {
        // Obtener referencias a los TextViews para obtener los valores de los datos a enviar
        TextView userCodeTextView = findViewById(R.id.Usercode);
        TextView nombreCompletoTextView = findViewById(R.id.NombreCompleto);
        TextView descripcionTextView = findViewById(R.id.tramite);
        TextView montoTextView = findViewById(R.id.monto);

        // Obtener los valores de los datos a enviar
        String codigo = userCodeTextView.getText().toString();
        String nombre = nombreCompletoTextView.getText().toString();
        String tramite = descripcionTextView.getText().toString();
        String monto = montoTextView.getText().toString();

        // Configurar la URL del script PHP en tu servidor
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.productosjr.com/aplicativos/appunac/pagartramite.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor, si es necesario
                        Toast.makeText(usuario_tramites.this, "Procesando Pago.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores en la solicitud
                        Toast.makeText(usuario_tramites.this, "Error al realizar el pago.", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("codigouser_pago", codigo);
                params.put("nombreuser_pago", nombre);
                params.put("concepto_pago", "Tramite");
                params.put("descripcion_pago", tramite);
                params.put("monto_pago", monto);
                return params;
            }
        };

        queue.add(postRequest);
    }


    public void cancelar(View view) {
        finish();
    }


}
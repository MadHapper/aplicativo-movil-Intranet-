package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;  // Agregar esta importación para abrir enlaces
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class usuario_historialpagos extends AppCompatActivity {

    TextView textnombre, textCodigo;
    LinearLayout listPagos;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_historialpagos);

        textnombre = findViewById(R.id.nombrepago);
        String usuario = getIntent().getStringExtra("usuario");
        textnombre.setText(usuario);

        textCodigo = findViewById(R.id.codigopago);
        String codigo = getIntent().getStringExtra("codigo");
        textCodigo.setText(codigo);

        listPagos = findViewById(R.id.listPagos);
        imageView3 = findViewById(R.id.imageView3);

        imageView3.setVisibility(View.GONE);

        // Realizar la solicitud para obtener los datos de pago
        obtenerDatosDePago(codigo);
    }

    private void obtenerDatosDePago(String codigoUsuario) {
        // URL del archivo PHP en tu servidor que obtiene los datos de pago
        String url = "https://www.productosjr.com/aplicativos/appunac/usuariodeudasypagos.php?codigo_usuario=" + codigoUsuario;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Obtener los datos de pago de la respuesta JSON
                                String conceptoPago = jsonObject.getString("concepto_pago");
                                String descripcionPago = jsonObject.getString("descripcion_pago");
                                String montoPago = jsonObject.getString("monto_pago");
                                String estadoPago = jsonObject.getString("estado_pago");

                                // Cambiar la imagen en imageView3 si el estado es "pendiente"
                                if (estadoPago.equals("pendiente")) {
                                    imageView3.setImageResource(R.drawable.deuda);
                                }

                                // Inflar el diseño "historial_pago" por cada fila de pago
                                LayoutInflater inflater = LayoutInflater.from(usuario_historialpagos.this);
                                View historialPagoView = inflater.inflate(R.layout.historial_pago, null);

                                // Asignar valores a los TextView dentro del diseño "historial_pago"
                                TextView conceptoTextView = historialPagoView.findViewById(R.id.concepto_pago);
                                TextView descripcionTextView = historialPagoView.findViewById(R.id.descripcion_pago);
                                TextView montoTextView = historialPagoView.findViewById(R.id.monto_pago);
                                TextView estadoTextView = historialPagoView.findViewById(R.id.estado_pago);

                                conceptoTextView.setText(conceptoPago);
                                descripcionTextView.setText(descripcionPago);
                                montoTextView.setText(montoPago);
                                estadoTextView.setText(estadoPago);

                                // Agregar un OnClickListener para abrir enlaces
                                historialPagoView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // Redirigir a enlace correspondiente
                                        if (descripcionPago.equals("1")) {
                                            abrirEnlace("http://mpago.la/1Nf5bMd");
                                        } else if (descripcionPago.equals("2")) {
                                            abrirEnlace("http://mpago.la/2YELfwM");
                                        } else if (descripcionPago.equals("3")) {
                                            abrirEnlace("http://mpago.la/1ZQBkKL");
                                        } else if (descripcionPago.equals("4")) {
                                            abrirEnlace("http://mpago.la/1QEtEi3");
                                        } else if (descripcionPago.equals("5")) {
                                            abrirEnlace("http://mpago.la/1CiTBqu");
                                        } else if (descripcionPago.equals("6")) {
                                            abrirEnlace("http://mpago.la/2ZnECzV");
                                        } else if (descripcionPago.equals("7")) {
                                            abrirEnlace("http://mpago.la/19KqaPa");
                                        } else if (descripcionPago.equals("8")) {
                                            abrirEnlace("http://mpago.la/2YFmHx5");
                                        } else if (descripcionPago.equals("9")) {
                                            abrirEnlace("http://mpago.la/2JAQbxN");
                                        } else if (descripcionPago.equals("28")) {
                                            abrirEnlace("http://mpago.la/2nf7QuK");
                                        } else if (descripcionPago.equals("29")) {
                                            abrirEnlace("http://mpago.la/2tdt3qM");
                                        } else if (descripcionPago.equals("30")) {
                                            abrirEnlace("http://mpago.la/1SfXBP9");
                                        } else if (descripcionPago.equals("31")) {
                                            abrirEnlace("http://mpago.la/2Ljhu4L");
                                        } else if (descripcionPago.equals("32")) {
                                            abrirEnlace("http://mpago.la/1GMEt3J");
                                        } else if (descripcionPago.equals("33")) {
                                            abrirEnlace("http://mpago.la/2gDgf7y");
                                        } else if (descripcionPago.equals("34")) {
                                            abrirEnlace("http://mpago.la/2e4Zsdp");
                                        } else if (descripcionPago.equals("35")) {
                                            abrirEnlace("http://mpago.la/1QegqYW");
                                        } else if (descripcionPago.equals("36")) {
                                            abrirEnlace("http://mpago.la/2S1qQAC");

                                        } else if (descripcionPago.equals("Certificado")) {
                                            abrirEnlace("http://mpago.la/1tcwyWx");
                                        } else if (descripcionPago.equals("Duplicado de certificado")) {
                                            abrirEnlace("http://mpago.la/2KMqKSW");
                                        } else if (descripcionPago.equals("Constancia de notas")) {
                                            abrirEnlace("http://mpago.la/2q5C1vo");
                                        } else if (descripcionPago.equals("Constancia de matricula")) {
                                            abrirEnlace("http://mpago.la/1wxkM1w");
                                        }
                                    }
                                });

                                // Agregar el diseño "historial_pago" al LinearLayout
                                listPagos.addView(historialPagoView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        error.printStackTrace();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void abrirEnlace(String url) {
        // Abre un enlace web en el navegador
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}

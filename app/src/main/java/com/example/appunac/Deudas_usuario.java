package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Deudas_usuario extends AppCompatActivity {

    TextView textnombre, textCodigo;
    LinearLayout listPagos;
    ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas_usuario);

        textnombre = findViewById(R.id.nombrepago);
        String usuario = getIntent().getStringExtra("usuario");
        textnombre.setText(usuario);

        textCodigo = findViewById(R.id.codigopago);
        String codigo = getIntent().getStringExtra("codigo");
        textCodigo.setText(codigo);

        listPagos = findViewById(R.id.listPagos);
        imageView3 = findViewById(R.id.imageView3);

        listPagos.setVisibility(View.GONE);

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
                                LayoutInflater inflater = LayoutInflater.from(Deudas_usuario.this);
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

    public void abrirPagosHistorial(View view) {
        // Obtén el valor del TextView
        String usuario = textnombre.getText().toString();
        String codigo = textCodigo.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(Deudas_usuario.this, usuario_historialpagos.class);
        intent.putExtra("usuario", usuario);
        intent.putExtra("codigo", codigo);
        startActivity(intent);
        finish();
    }
}

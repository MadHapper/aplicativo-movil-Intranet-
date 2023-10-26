package com.example.appunac;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class admin_pagos extends AppCompatActivity {

    private LinearLayout listaPagosLayout;
    private List<JSONObject> pagosList;
    private List<JSONObject> filteredPagosList;
    private TableRow[] rows;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pagos);

        listaPagosLayout = findViewById(R.id.ListaPagos);
        searchView = findViewById(R.id.txtbuscar);
        pagosList = new ArrayList<>();
        filteredPagosList = new ArrayList<>();

        // Realiza la solicitud HTTP para obtener datos desde el servidor PHP
        new Thread(new Runnable() {
            @Override
            public void run() {
                String apiUrl = "https://www.productosjr.com/aplicativos/appunac/admindeudasypagos.php";
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Lee la respuesta del servidor
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    connection.disconnect();

                    // Procesa la respuesta JSON
                    final JSONArray jsonArray = new JSONArray(response.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Itera sobre los datos JSON y crea vistas dinámicas
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    pagosList.add(jsonObject);
                                    filteredPagosList.add(jsonObject);

                                    // Crear una vista "list_pago" por cada fila de datos
                                    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.list_pago, null);
                                    TextView nombreuserPago = row.findViewById(R.id.nombreuser_pago);
                                    TextView idPago = row.findViewById(R.id.id_pago);
                                    TextView conceptoPago = row.findViewById(R.id.concepto_pago);
                                    TextView descripcionPago = row.findViewById(R.id.descripcion_pago);
                                    TextView montoPago = row.findViewById(R.id.monto_pago);
                                    TextView estadoPago = row.findViewById(R.id.estado_pago);

                                    // Establecer los datos en las vistas
                                    nombreuserPago.setText(jsonObject.getString("nombreuser_pago"));
                                    idPago.setText(jsonObject.getString("id_pago"));
                                    conceptoPago.setText(jsonObject.getString("concepto_pago"));
                                    descripcionPago.setText(jsonObject.getString("descripcion_pago"));
                                    montoPago.setText(jsonObject.getString("monto_pago"));
                                    estadoPago.setText(jsonObject.getString("estado_pago"));

                                    listaPagosLayout.addView(row);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Agregar TextWatcher para el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No se usa en esta implementación
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return true;
            }
        });
    }

    private void filterResults(String query) {
        filteredPagosList.clear();

        for (int i = 0; i < pagosList.size(); i++) {
            JSONObject jsonObject = pagosList.get(i);
            try {
                String nombreuserPago = jsonObject.getString("nombreuser_pago").toLowerCase();
                if (nombreuserPago.contains(query.toLowerCase())) {
                    filteredPagosList.add(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Actualiza la vista con los resultados filtrados
        updateView();
    }

    private void updateView() {
        listaPagosLayout.removeAllViews();

        for (int i = 0; i < filteredPagosList.size(); i++) {
            try {
                JSONObject jsonObject = filteredPagosList.get(i);

                // Crear una vista "list_pago" por cada fila de datos
                TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.list_pago, null);
                TextView nombreuserPago = row.findViewById(R.id.nombreuser_pago);
                TextView idPago = row.findViewById(R.id.id_pago);
                TextView conceptoPago = row.findViewById(R.id.concepto_pago);
                TextView descripcionPago = row.findViewById(R.id.descripcion_pago);
                TextView montoPago = row.findViewById(R.id.monto_pago);
                TextView estadoPago = row.findViewById(R.id.estado_pago);

                // Establecer los datos en las vistas
                nombreuserPago.setText(jsonObject.getString("nombreuser_pago"));
                idPago.setText(jsonObject.getString("id_pago"));
                conceptoPago.setText(jsonObject.getString("concepto_pago"));
                descripcionPago.setText(jsonObject.getString("descripcion_pago"));
                montoPago.setText(jsonObject.getString("monto_pago"));
                estadoPago.setText(jsonObject.getString("estado_pago"));

                listaPagosLayout.addView(row);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

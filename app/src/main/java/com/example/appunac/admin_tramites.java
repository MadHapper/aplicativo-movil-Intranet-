package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class admin_tramites extends AppCompatActivity {

    private LinearLayout listaTramites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tramites);

        listaTramites = findViewById(R.id.ListaTramites);

        // Realizar una solicitud HTTP para obtener los datos
        new GetTramitesTask().execute("https://www.productosjr.com/aplicativos/appunac/get_tramites.php");
    }

    private class GetTramitesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Procesar la respuesta
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray tramitesArray = new JSONArray(result);

                for (int i = 0; i < tramitesArray.length(); i++) {
                    JSONObject tramite = tramitesArray.getJSONObject(i);

                    // Crear una vista inflando el layout "list_pago"
                    LinearLayout tramiteLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.list_tramite, null);

                    // Obtener referencias a los TextView en "list_pago"
                    TextView idPago = tramiteLayout.findViewById(R.id.id_pago);
                    TextView nombreUserPago = tramiteLayout.findViewById(R.id.nombreuser_pago);
                    TextView descripcionPago = tramiteLayout.findViewById(R.id.descripcion_pago);
                    TextView estadoPago = tramiteLayout.findViewById(R.id.estado_pago);

                    // Establecer los datos en los TextView
                    idPago.setText(tramite.getString("id_pago"));
                    nombreUserPago.setText(tramite.getString("nombreuser_pago"));
                    descripcionPago.setText(tramite.getString("descripcion_pago"));
                    estadoPago.setText(tramite.getString("estado_pago"));

                    // Agregar la vista al LinearLayout "ListaTramites"
                    listaTramites.addView(tramiteLayout);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

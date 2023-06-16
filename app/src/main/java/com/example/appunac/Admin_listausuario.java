package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Admin_listausuario extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listausuario);

        tableLayout = findViewById(R.id.tableLayout);
        new LoadUsuarios().execute();
    }

    private class LoadUsuarios extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/conexion_listausuario.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("usu_id");
                    String codigo = jsonObject.getString("usu_codigo");
                    String correo = jsonObject.getString("usu_correo");
                    String nombre = jsonObject.getString("usu_nombre");
                    String apellidoPaterno = jsonObject.getString("usu_apellidoP");
                    String apellidoMaterno = jsonObject.getString("usu_apellidoM");
                    int cargo = jsonObject.getInt("usu_grado");

                    String cargoText = "";
                    switch (cargo) {
                        case 1:
                            cargoText = "Usuario";
                            break;
                        case 2:
                            cargoText = "Docente";
                            break;
                        case 3:
                            cargoText = "Admin";
                            break;
                    }

                    TableRow tableRow = new TableRow(getApplicationContext());
                    TextView txtCodigo = createTextView(codigo);
                    TextView txtCorreo = createTextView(correo);
                    TextView txtNombre = createTextView(nombre);
                    TextView txtApellidoP = createTextView(apellidoPaterno);
                    TextView txtApellidoM = createTextView(apellidoMaterno);
                    TextView txtCargo = createTextView(cargoText);

                    tableRow.addView(txtCodigo);
                    tableRow.addView(txtCorreo);
                    tableRow.addView(txtNombre);
                    tableRow.addView(txtApellidoP);
                    tableRow.addView(txtApellidoM);
                    tableRow.addView(txtCargo);

                    tableLayout.addView(tableRow);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setPadding(16, 16, 16, 16);
        textView.setText(text);
        return textView;
    }
    public void abrirbusqueda (View view) {
        startActivity(new Intent(getApplicationContext(),Registroacademico_usuario.class));
        finish();
    }

    public void abriradminregistro (View view) {
        startActivity(new Intent(getApplicationContext(),admin_registrarnuevo.class));
        finish();
    }

}

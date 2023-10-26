package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.SearchView;

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

public class Admin_listausuario extends AppCompatActivity {

    TextView textViewUsuario;
    TableLayout tableLayoutUsuarios;
    SearchView txtBuscarUser;

    List<TableRow> allRows; // Lista para almacenar todas las filas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_listausuario);

        textViewUsuario = findViewById(R.id.textView28);
        tableLayoutUsuarios = findViewById(R.id.tableLayout3);
        txtBuscarUser = findViewById(R.id.txtbuscaruser);

        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        String url = "https://www.productosjr.com/aplicativos/appunac/listausuarios.php";
        new ObtenerDatosAsyncTask().execute(url);

        allRows = new ArrayList<>(); // Inicializar la lista

        // Agregar un TextWatcher al SearchView
        txtBuscarUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Aplicar el filtro al TableLayout
                filtrarUsuarios(newText);
                return true;
            }
        });

        // Obtener todas las filas del TableLayout
        getAllRows();
    }

    private void mostrarUsuarios(JSONArray usuarios) {
        try {
            for (int i = 0; i < usuarios.length(); i++) {
                JSONObject usuario = usuarios.getJSONObject(i);
                String codigo = usuario.getString("usu_codigo");
                String correo = usuario.getString("usu_correo");
                String nombre = usuario.getString("usu_nombre");
                String apellidoP = usuario.getString("usu_apellidoP");
                String apellidoM = usuario.getString("usu_apellidoM");
                String grado = usuario.getString("usu_grado");

                // Obtener el nombre completo
                String nombreCompleto = nombre + " " + apellidoP + " " + apellidoM;

                // Duplicar TableRow
                TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.user_row, null);
                TextView textViewNombre = tableRow.findViewById(R.id.nombre);
                TextView textViewCodigo = tableRow.findViewById(R.id.codigo);
                TextView textViewCargo = tableRow.findViewById(R.id.cargo);
                TextView textViewCorreo = tableRow.findViewById(R.id.Correo);

                textViewNombre.setText(nombreCompleto);
                textViewCodigo.setText(codigo);
                textViewCargo.setText(grado);
                textViewCorreo.setText(correo);

                // Agregar OnClickListener a TableRow
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Obtener los valores de codigocurso y nombrecurso
                        String Correo = textViewCorreo.getText().toString();
                        String Usuario = textViewUsuario.getText().toString();

                        // Crear Intent para iniciar la actividad InglesBasico1.java
                        Intent intent = new Intent(Admin_listausuario.this, admin_modificarusuario.class);
                        intent.putExtra("correo", Correo);
                        intent.putExtra("usuario", Usuario);
                        startActivity(intent);
                    }
                });

                tableLayoutUsuarios.addView(tableRow);

                // Agregar la fila a la lista
                allRows.add(tableRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void filtrarUsuarios(String filtro) {
        for (TableRow row : allRows) {
            TextView textViewNombre = row.findViewById(R.id.nombre);
            String nombreCompleto = textViewNombre.getText().toString();

            if (nombreCompleto.toLowerCase().contains(filtro.toLowerCase())) {
                row.setVisibility(View.VISIBLE);
            } else {
                row.setVisibility(View.GONE);
            }
        }
    }

    private class ObtenerDatosAsyncTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            JSONArray usuariosJsonArray = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    return null;
                }

                String usuariosJsonString = builder.toString();
                usuariosJsonArray = new JSONArray(usuariosJsonString);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return usuariosJsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray usuariosJsonArray) {
            super.onPostExecute(usuariosJsonArray);
            if (usuariosJsonArray != null) {
                mostrarUsuarios(usuariosJsonArray);
            }
        }
    }


    // Método para obtener todas las filas del TableLayout
    private void getAllRows() {
        for (int i = 0; i < tableLayoutUsuarios.getChildCount(); i++) {
            View view = tableLayoutUsuarios.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                allRows.add(row);
            }
        }
    }

    public void crearusuario (View view) {
        startActivity(new Intent(getApplicationContext(),admin_registrarnuevo.class));
    }
}

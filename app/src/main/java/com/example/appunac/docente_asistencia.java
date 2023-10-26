package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class docente_asistencia extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc, textcod;
    LinearLayout asistenciaList;
    String codigoc;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_asistencia);

        textViewUsuario = findViewById(R.id.alumnon);
        textViewCurso = findViewById(R.id.nombrec);
        textViewCursoc = findViewById(R.id.codigoc);

        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);

        asistenciaList = findViewById(R.id.AsitenciaList);
        searchView = findViewById(R.id.txtbuscar);

        // Obtén el valor de codigoc desde textViewCursoc
        codigoc = textViewCursoc.getText().toString();

        // Realiza la solicitud HTTP al archivo PHP
        new GetTableData().execute("https://www.productosjr.com/aplicativos/appunac/asistenciadocente.php?codigo=" + codigoc);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Este método se llama cuando se envía la consulta, puedes dejarlo vacío si no es necesario.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Este método se llama cuando el texto de búsqueda cambia.
                filterData(newText); // Llama a una función para filtrar tus datos según el nuevo texto.
                return true;
            }
        });
    }

    private void filterData(String searchText) {
        // Recorre todas las vistas en tu LinearLayout y oculta/muestra según el filtro.
        for (int i = 0; i < asistenciaList.getChildCount(); i++) {
            View rowView = asistenciaList.getChildAt(i);
            TextView textNombreCompleto = rowView.findViewById(R.id.textNombreCompleto);

            if (textNombreCompleto.getText().toString().toLowerCase().contains(searchText.toLowerCase())) {
                rowView.setVisibility(View.VISIBLE);
            } else {
                rowView.setVisibility(View.GONE);
            }
        }
    }

    // Clase para realizar la solicitud HTTP en segundo plano
    private class GetTableData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Leer la respuesta
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    // Recorrer los datos obtenidos
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // Crear una vista TableRow a partir del archivo XML
                        LayoutInflater inflater = getLayoutInflater();
                        View rowView = inflater.inflate(R.layout.table_row_asistenciadocente, null);

                        // Obtener los datos del JSON
                        String codigoAlumno = jsonObject.getString("codigo_alumno");
                        String nombreAlumno = jsonObject.getString("nombre_alumno");
                        String apellidoPAlumno = jsonObject.getString("apellidoP_alumno");
                        String apellidoMAlumno = jsonObject.getString("apellidoM_alumno");


                        String nombreCompleto = nombreAlumno + " " + apellidoPAlumno + " " + apellidoMAlumno;

                        // Asignar los datos a las vistas en la vista TableRow
                        TextView textNombreCompleto = rowView.findViewById(R.id.textNombreCompleto);
                        textNombreCompleto.setText(nombreCompleto);
                        TextView textCod = rowView.findViewById(R.id.textcod);
                        textCod.setText(codigoAlumno);

                        // Obtener el CardView
                        CardView cardViewAsistencia = rowView.findViewById(R.id.cardViewAsistencia);

                        // Configurar el clic en el CardView
                        cardViewAsistencia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Cuando se hace clic en el CardView, inicia la actividad "docente_asistencias_list.java"
                                Intent intent = new Intent(docente_asistencia.this, docente_asistencias_list.class);

                                // Pasa los datos a la nueva actividad
                                intent.putExtra("usuario", textViewUsuario.getText().toString());
                                intent.putExtra("curso", textViewCurso.getText().toString());
                                intent.putExtra("cursoc", textViewCursoc.getText().toString());
                                intent.putExtra("nombreCompleto", nombreCompleto);
                                intent.putExtra("cod", codigoAlumno);

                                // Inicia la nueva actividad
                                startActivity(intent);
                            }
                        });

                        // Agregar la vista TableRow al LinearLayout
                        asistenciaList.addView(rowView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
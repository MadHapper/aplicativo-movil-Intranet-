package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class docente_registronotas extends AppCompatActivity {

    private TextView textViewUsuario, textViewCurso, textViewCursoc;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_registronotas);

        textViewUsuario = findViewById(R.id.textView48);
        textViewCurso = findViewById(R.id.textView51);
        textViewCursoc = findViewById(R.id.textView52);
        tableLayout = findViewById(R.id.tabla1);

        String usuario = getIntent().getStringExtra("usuario");
        String curso = getIntent().getStringExtra("curso");
        String cursoc = getIntent().getStringExtra("cursoc");

        textViewUsuario.setText(usuario);
        textViewCurso.setText(curso);
        textViewCursoc.setText(cursoc);

        String tableName = "tabla_" + cursoc;
        String url = "https://www.productosjr.com/aplicativos/appunac/docenteregistrarnotascursos.php?table=" + tableName;
        new GetDataTask().execute(url);
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String nombre = jsonObject.getString("nombre_alumno");
                    String apellidoP = jsonObject.getString("apellidoP_alumno");
                    String apellidoM = jsonObject.getString("apellidoM_alumno");
                    String practica1 = jsonObject.getString("practica1");
                    String parcial = jsonObject.getString("parcial");
                    String practica2 = jsonObject.getString("practica2");
                    String finalExam = jsonObject.getString("final");
                    String promedio = jsonObject.getString("promedio");

                    createTableRow(nombre, apellidoP, apellidoM, practica1, parcial, practica2, finalExam, promedio);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTableRow(String nombre, String apellidoP, String apellidoM, String practica1,
                                String parcial, String practica2, String finalExam, String promedio) {
        TableRow row = new TableRow(this);

        TextView textNombre = new TextView(this);
        textNombre.setText(nombre);
        TextView textApellidoP = new TextView(this);
        textApellidoP.setText(apellidoP);
        TextView textApellidoM = new TextView(this);
        textApellidoM.setText(apellidoM);
        TextView textPractica1 = new TextView(this);
        textPractica1.setText(practica1);
        TextView textParcial = new TextView(this);
        textParcial.setText(parcial);
        TextView textPractica2 = new TextView(this);
        textPractica2.setText(practica2);
        TextView textFinal = new TextView(this);
        textFinal.setText(finalExam);
        TextView textPromedio = new TextView(this);
        textPromedio.setText(promedio);

        row.addView(textNombre);
        row.addView(textApellidoP);
        row.addView(textApellidoM);
        row.addView(textPractica1);
        row.addView(textParcial);
        row.addView(textPractica2);
        row.addView(textFinal);
        row.addView(textPromedio);

        tableLayout.addView(row);
    }
}
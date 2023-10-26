package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class docente_biblioteca extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso, textViewCursoc;
    EditText editTextURLMeet;
    Button guardarURLButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente_biblioteca);


        textViewUsuario = findViewById(R.id.textView14);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        textViewCurso = findViewById(R.id.textView9);
        String curso = getIntent().getStringExtra("curso");
        textViewCurso.setText(curso);

        textViewCursoc = findViewById(R.id.textView47);
        String cursoc = getIntent().getStringExtra("cursoc");
        textViewCursoc.setText(cursoc);

        editTextURLMeet = findViewById(R.id.URLMeet);

        guardarURLButton = findViewById(R.id.GuardarURL);
        guardarURLButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlMeet = editTextURLMeet.getText().toString();
                new docente_biblioteca.ModificarURLMeet().execute(cursoc, urlMeet);
            }
        });

        new docente_biblioteca.ObtenerURLMeet().execute(cursoc);
    }

    private class ObtenerURLMeet extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cursoc = params[0];
            String resultado = "";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/docentebiblioteca.php?cursoc=" + cursoc);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                resultado = buffer.toString();
            } catch (IOException e) {
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

            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            if (resultado != null) {
                try {
                    JSONObject jsonResultado = new JSONObject(resultado);
                    String urlMeet = jsonResultado.getString("curso_biblioteca");
                    editTextURLMeet.setText(urlMeet);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ModificarURLMeet extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String cursoc = params[0];
            String urlMeet = params[1];

            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("https://www.productosjr.com/aplicativos/appunac/docenteblibliotecaModificar.php?cursoc=" + cursoc + "&urlMeet=" + urlMeet);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                JSONObject requestBody = new JSONObject();
                requestBody.put("cursoc", cursoc);
                requestBody.put("urlMeet", urlMeet);

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(requestBody.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = urlConnection.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(docente_biblioteca.this, "URL modificada exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(docente_biblioteca.this, "Error al modificar la URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void iniciarmeet (View view){
        String url = editTextURLMeet.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
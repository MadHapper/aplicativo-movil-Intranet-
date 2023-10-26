package com.example.appunac;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MatriculaCurso_usuario extends AppCompatActivity {

    TextView textView59, textViewUsuario;
    RadioGroup radioGroupCursos;
    LinearLayout linearLayoutCursosLista;
    Button btnAceptarMatricula;
    CheckBox selectedCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matricula_curso_usuario);

        textView59 = findViewById(R.id.textCodigoMa);
        radioGroupCursos = findViewById(R.id.radioGroupCursos);
        linearLayoutCursosLista = findViewById(R.id.linearLayoutCursosLista);
        btnAceptarMatricula = findViewById(R.id.AcceptarMatricula);
        textViewUsuario = findViewById(R.id.UsuarioView);

        String usuario = getIntent().getStringExtra("usuario");
        String codigo = getIntent().getStringExtra("codigo");
        textView59.setText(codigo);
        textViewUsuario.setText(usuario);

        radioGroupCursos.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedCourse = selectedRadioButton.getText().toString();
                // Realiza la solicitud HTTP para obtener los cursos basados en el idioma seleccionado.
                obtenerCursos(selectedCourse);
            }
        });

        btnAceptarMatricula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el valor de textCodigoMa
                String codigoMa = textView59.getText().toString();

                // Encontrar el curso marcado (CheckBox)
                String cursoMarcado = encontrarCursoMarcado();

                // Realizar la actualización en la base de datos
                if (!cursoMarcado.isEmpty()) {
                    actualizarBaseDeDatos(codigoMa, cursoMarcado);
                    mostrarMensaje("Matrícula aceptada");
                } else {
                    mostrarMensaje("Selecciona un curso primero");
                }
            }
        });
    }

    private void obtenerCursos(final String selectedCourse) {
        // URL de tu PHP en el servidor.
        String url = "https://www.productosjr.com/aplicativos/appunac/MatriculaCursoLista.php?idioma=" + selectedCourse;

        // Realiza la solicitud HTTP en un hilo separado para no bloquear la interfaz de usuario.
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL requestURL = new URL(url);
                    urlConnection = (HttpURLConnection) requestURL.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder builder = new StringBuilder();

                    if (inputStream == null) {
                        return;
                    }

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }

                    if (builder.length() == 0) {
                        return;
                    }

                    String cursosJsonString = builder.toString();

                    // Procesa la respuesta JSON.
                    mostrarCursos(cursosJsonString);

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
            }
        }).start();
    }

    private void mostrarCursos(String cursosJsonString) {
        try {
            JSONArray cursosArray = new JSONArray(cursosJsonString);

            // Borra cualquier vista previa de cursos en el LinearLayout.
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayoutCursosLista.removeAllViews();
                }
            });

            for (int i = 0; i < cursosArray.length(); i++) {
                JSONObject curso = cursosArray.getJSONObject(i);
                final String codigoCurso = curso.getString("curso_id");
                final String nombreCurso = curso.getString("curso_nombre");
                final String horarioCurso = curso.getString("curso_horario");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Infla la vista de fila de tabla personalizada para mostrar los datos del curso.
                        LinearLayout courseRow = (LinearLayout) getLayoutInflater().inflate(R.layout.table_row_matricula, null);

                        final CheckBox checkBoxCurso = courseRow.findViewById(R.id.CursoMatricular);
                        TextView textViewCodigoCurso = courseRow.findViewById(R.id.codigocurso);
                        TextView textViewNombreCurso = courseRow.findViewById(R.id.nombrecurso);
                        TextView textViewHorarioCurso = courseRow.findViewById(R.id.Fecha);

                        // Establece los valores en los elementos de la vista.
                        checkBoxCurso.setText(codigoCurso);  // Establece el código como el texto de CheckBox.
                        textViewCodigoCurso.setText(codigoCurso);
                        textViewNombreCurso.setText(nombreCurso);
                        textViewHorarioCurso.setText(horarioCurso);

                        // Agrega un listener para controlar la selección de CheckBox.
                        checkBoxCurso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Deselecciona el CheckBox previamente seleccionado si hay uno.
                                if (isChecked && selectedCheckBox != null && selectedCheckBox != checkBoxCurso) {
                                    selectedCheckBox.setChecked(false);
                                }
                                // Actualiza el CheckBox seleccionado actual.
                                if (isChecked) {
                                    selectedCheckBox = checkBoxCurso;
                                }
                            }
                        });

                        // Agrega el curso a la lista.
                        linearLayoutCursosLista.addView(courseRow);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String encontrarCursoMarcado() {
        // Recorre los CheckBox dentro del LinearLayout
        for (int i = 0; i < linearLayoutCursosLista.getChildCount(); i++) {
            View childView = linearLayoutCursosLista.getChildAt(i);
            if (childView instanceof LinearLayout) {
                CheckBox checkBoxCurso = childView.findViewById(R.id.CursoMatricular);
                if (checkBoxCurso.isChecked()) {
                    return checkBoxCurso.getText().toString();
                }
            }
        }
        return "";
    }

    private void actualizarBaseDeDatos(final String codigoMa, final String cursoMarcado) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // URL del script PHP para actualizar la base de datos
                    URL url = new URL("https://www.productosjr.com/aplicativos/appunac/matriculacrearcursod.php");

                    // Abrir una conexión HTTP
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Parámetros a enviar al servidor
                    String parameters = "usu_codigo=" + codigoMa + "&curso_id=" + cursoMarcado;

                    // Enviar los parámetros al servidor
                    OutputStream os = connection.getOutputStream();
                    os.write(parameters.getBytes());
                    os.flush();
                    os.close();

                    // Obtener la respuesta del servidor
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    // Cerrar las conexiones
                    bufferedReader.close();
                    inputStream.close();
                    connection.disconnect();

                    // Mostrar la respuesta del servidor en el hilo principal (UI)
                    final String serverResponse = response.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Puedes mostrar un mensaje de éxito o manejar errores aquí
                            // serverResponse contiene la respuesta del servidor.
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mostrarMensaje(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void CancelarMatricula(View view) {
        finish();
    }

}

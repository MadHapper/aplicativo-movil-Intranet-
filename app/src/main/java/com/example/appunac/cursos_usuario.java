package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

public class cursos_usuario extends AppCompatActivity {

    TextView textViewUsuario, textViewCurso1, textViewCurso2, textViewCurso3, textViewCursoc1, textViewCursoc2, textViewCursoc3;

    private TextView textView44, textView45, textView46;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos_usuario);

        textViewUsuario = findViewById(R.id.textView28);
        String usuario = getIntent().getStringExtra("usuario");
        textViewUsuario.setText(usuario);

        // Inicializar TextViews
        textView44 = findViewById(R.id.textView44);
        textView45 = findViewById(R.id.textView45);
        textView46 = findViewById(R.id.textView46);

        textViewCurso1 = findViewById(R.id.textView15);
        textViewCurso2 = findViewById(R.id.textView22);
        textViewCurso3 = findViewById(R.id.textView19);

        textViewCursoc1 = findViewById(R.id.textView44);
        textViewCursoc2 = findViewById(R.id.textView45);
        textViewCursoc3 = findViewById(R.id.textView46);


        // Obtener cursos del usuario desde la base de datos
        obtenerCursos(usuario);
    }

    public void obtenerCursos(String usuario) {
        // URL del archivo PHP en el servidor
        String url = "https://www.productosjr.com/aplicativos/appunac/conexion_curso.php?curso_codigo=" + usuario;

        // Crear solicitud GET usando Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Obtener el primer objeto JSON del array
                            JSONObject curso = response.getJSONObject(0);

                            // Obtener los datos del curso
                            String curso1 = curso.optString("usu_curso1", "");
                            String curso2 = curso.optString("usu_curso2", "");
                            String curso3 = curso.optString("usu_curso3", "");

                            // Actualizar los TextViews con los cursos obtenidos
                            textView44.setText(curso1);
                            textView45.setText(curso2);
                            textView46.setText(curso3);

                            // Si no se encontró un nombre de curso, ocultar el TableRow correspondiente
                            if (curso1.isEmpty()) {
                                findViewById(R.id.imageButton2).setVisibility(View.GONE);
                                findViewById(R.id.textView15).setVisibility(View.GONE);
                                findViewById(R.id.textView44).setVisibility(View.GONE);
                            }
                            if (curso2.isEmpty()) {
                                findViewById(R.id.imageButton4).setVisibility(View.GONE);
                                findViewById(R.id.textView22).setVisibility(View.GONE);
                                findViewById(R.id.textView45).setVisibility(View.GONE);
                            }
                            if (curso3.isEmpty()) {
                                findViewById(R.id.imageButton8).setVisibility(View.GONE);
                                findViewById(R.id.textView19).setVisibility(View.GONE);
                                findViewById(R.id.textView46).setVisibility(View.GONE);
                            }

                            // Buscar nombres de curso basado en los IDs
                            buscarNombresCursos(curso1, curso2, curso3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(request);
    }

    public void buscarNombresCursos(String curso1, String curso2, String curso3) {
        // Tabla con los IDs y nombres de curso
        String[][] tablaCursos = {
                {"11", "INGLES BASICO 1"},
                {"12", "INGLES BASICO 1"},
                {"13", "INGLES BASICO 1"},
                {"14", "INGLES BASICO 1"},
                {"15", "INGLES BASICO 1"},
                {"16", "INGLES BASICO 2"},
                {"17", "INGLES BASICO 2"},
                {"18", "INGLES BASICO 3"},
                {"19", "INGLES BASICO 3"},
                {"20", "INGLES BASICO 4"},
                {"21", "INGLES BASICO 5"},
                {"22", "INGLES BASICO 6"},
                {"23", "INGLES BASICO 7"},
                {"24", "INGLES BASICO 8"},
                {"25", "INGLES BASICO 9"},
                {"26", "INGLES INTERMEDIO 1"},
                {"27", "INGLES INTERMEDIO 2"},
                {"28", "INGLES INTERMEDIO 3"},
                {"29", "INGLES INTERMEDIO 4"},
                {"30", "INGLES INTERMEDIO 5"},
                {"31", "INGLES INTERMEDIO 6"},
                {"32", "INGLES INTERMEDIO 7"},
                {"33", "INGLES INTERMEDIO 8"},
                {"34", "INGLES INTERMEDIO 9"},
                {"35", "INGLES AVANZADO 1"},
                {"36", "INGLES AVANZADO 2"},
                {"37", "INGLES AVANZADO 3"},
                {"38", "INGLES AVANZADO 4"},
                {"39", "INGLES AVANZADO 5"},
                {"40", "INGLES AVANZADO 6"},
                {"41", "INGLES AVANZADO 7"},
                {"42", "INGLES AVANZADO 8"},
                {"43", "INGLES AVANZADO 9"},
                {"44", "PORTUGUES BASICO 1"},
                {"45", "PORTUGUES BASICO 1"},
                {"46", "PORTUGUES BASICO 1"},
                {"47", "PORTUGUES BASICO 1"},
                {"48", "PORTUGUES BASICO 1"},
                {"49", "PORTUGUES BASICO 2"},
                {"50", "PORTUGUES BASICO 2"},
                {"51", "PORTUGUES BASICO 3"},
                {"52", "PORTUGUES BASICO 3"},
                {"53", "PORTUGUES BASICO 4"},
                {"54", "PORTUGUES BASICO 5"},
                {"55", "PORTUGUES BASICO 6"},
                {"56", "PORTUGUES BASICO 7"},
                {"57", "PORTUGUES BASICO 8"},
                {"58", "PORTUGUES BASICO 9"},
                {"59", "PORTUGUES INTERMEDIO 1"},
                {"60", "PORTUGUES INTERMEDIO 2"},
                {"61", "PORTUGUES INTERMEDIO 3"},
                {"62", "PORTUGUES INTERMEDIO 4"},
                {"63", "PORTUGUES INTERMEDIO 5"},
                {"64", "PORTUGUES INTERMEDIO 6"},
                {"65", "PORTUGUES INTERMEDIO 7"},
                {"66", "PORTUGUES INTERMEDIO 8"},
                {"67", "PORTUGUES INTERMEDIO 9"},
                {"68", "PORTUGUES AVANZADO 1"},
                {"69", "PORTUGUES AVANZADO 2"},
                {"70", "PORTUGUES AVANZADO 3"},
                {"71", "PORTUGUES AVANZADO 4"},
                {"72", "PORTUGUES AVANZADO 5"},
                {"73", "PORTUGUES AVANZADO 6"},
                {"74", "PORTUGUES AVANZADO 7"},
                {"75", "PORTUGUES AVANZADO 8"},
                {"76", "PORTUGUES AVANZADO 9"},
                {"77", "FRANCES BASICO 1"},
                {"78", "FRANCES BASICO 1"},
                {"79", "FRANCES BASICO 1"},
                {"80", "FRANCES BASICO 1"},
                {"81", "FRANCES BASICO 1"},
                {"82", "FRANCES BASICO 2"},
                {"83", "FRANCES BASICO 2"},
                {"84", "FRANCES BASICO 3"},
                {"85", "FRANCES BASICO 3"},
                {"86", "FRANCES BASICO 4"},
                {"87", "FRANCES BASICO 5"},
                {"88", "FRANCES BASICO 6"},
                {"89", "FRANCES BASICO 7"},
                {"90", "FRANCES BASICO 8"},
                {"91", "FRANCES BASICO 9"},
                {"92", "FRANCES INTERMEDIO 1"},
                {"93", "FRANCES INTERMEDIO 2"},
                {"94", "FRANCES INTERMEDIO 3"},
                {"95", "FRANCES INTERMEDIO 4"},
                {"96", "FRANCES INTERMEDIO 5"},
                {"97", "FRANCES INTERMEDIO 6"},
                {"98", "FRANCES INTERMEDIO 7"},
                {"99", "FRANCES INTERMEDIO 8"},
                {"100", "FRANCES INTERMEDIO 9"},
                {"101", "FRANCES AVANZADO 1"},
                {"102", "FRANCES AVANZADO 2"},
                {"103", "FRANCES AVANZADO 3"},
                {"104", "FRANCES AVANZADO 4"},
                {"105", "FRANCES AVANZADO 5"},
                {"106", "FRANCES AVANZADO 6"},
                {"107", "FRANCES AVANZADO 7"},
                {"108", "FRANCES AVANZADO 8"},
                {"109", "FRANCES AVANZADO 9"}
        };

        // Buscar nombres de curso basado en los IDs
        String nombreCurso1 = buscarNombreCurso(curso1, tablaCursos);
        String nombreCurso2 = buscarNombreCurso(curso2, tablaCursos);
        String nombreCurso3 = buscarNombreCurso(curso3, tablaCursos);

        // Actualizar los TextViews con los nombres de curso
        textViewCurso1.setText(nombreCurso1);
        textViewCurso2.setText(nombreCurso2);
        textViewCurso3.setText(nombreCurso3);
    }

    public String buscarNombreCurso(String idCurso, String[][] tablaCursos) {
        for (String[] curso : tablaCursos) {
            if (curso[0].equals(idCurso)) {
                return curso[1];
            }
        }
        return "";
    }

    public void abrirInglesBasico1(View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso1.getText().toString();
        String cursoc = textViewCursoc1.getText().toString();

        Intent intent = new Intent(cursos_usuario.this, InglesBasico1.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

    public void abrirInglesBasico2(View view) {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso2.getText().toString();
        String cursoc = textViewCursoc2.getText().toString();

        Intent intent = new Intent(cursos_usuario.this, InglesBasico1.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

    public void abrirInglesBasico3(View view) {
        String usuario = textViewUsuario.getText().toString();
        String curso = textViewCurso3.getText().toString();
        String cursoc = textViewCursoc3.getText().toString();

        Intent intent = new Intent(cursos_usuario.this, InglesBasico1.class);
        intent.putExtra("curso", curso);
        intent.putExtra("usuario", usuario);
        intent.putExtra("cursoc", cursoc);
        startActivity(intent);
        finish();
    }

    public void onBackPressed() {
        // Obtén el valor del TextView
        String usuario = textViewUsuario.getText().toString();

        // Crea un Intent y agrega el dato utilizando putExtra()
        Intent intent = new Intent(cursos_usuario.this, menuUsuario.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
        finish();
    }
}

package com.example.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Registroacademico_usuario extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String urlToload = "https://www.productosjr.com/aplicativos/appunac/HORARIOS.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registroacademico_usuario);

        mProgressBar = findViewById(R.id.login);
        mWebView = findViewById(R.id.webview1);

        // Habilitar JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Establecer cliente WebViewClient
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // La página se ha cargado completamente
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Manejar la carga de URL dentro del WebView
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });

        // Cargar el enlace
        mWebView.loadUrl(urlToload);
    }
}

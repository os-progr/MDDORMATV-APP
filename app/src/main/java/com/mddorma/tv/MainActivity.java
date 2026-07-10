package com.mddorma.tv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView webView;
    
    // AQUÍ ES DONDE CAMBIARÁS LA URL CUANDO LA SUBAS A TU SERVIDOR PRINCIPAL
    // Por ahora apunta a tu repositorio, pero luego deberás poner: "https://mddorma.com/tv"
    private final String APP_URL = "https://os-progr.github.io/mddormaTV/#";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hacer la aplicación pantalla completa sin barra de notificaciones
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
                             
        // Mantener la pantalla encendida mientras se ve un dorama
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Crear el WebView
        webView = new WebView(this);
        setContentView(webView);

        // Configuración crítica para que el reproductor HLS y el Spatial Navigation funcionen
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true); // Necesario para guardar sesiones/progreso
        webSettings.setMediaPlaybackRequiresUserGesture(false); // Autoplay de videos sin tocar la pantalla
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // Abrir enlaces dentro de la misma app en vez de usar Chrome
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        // Cargar tu página web
        webView.loadUrl(APP_URL);
        
        // Enfocar el WebView para que el control remoto de TV funcione inmediatamente
        webView.requestFocus();
    }

    // Interceptar el botón "Atrás" del control remoto de la TV
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack(); // Volver a la página anterior de tu web
                    } else {
                        finish(); // Salir de la app si ya estamos en el inicio
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

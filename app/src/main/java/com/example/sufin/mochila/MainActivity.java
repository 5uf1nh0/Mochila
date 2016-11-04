package com.example.sufin.mochila;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String nombre, url;
    private int id;
    private ProgressBar progressBar;
    private WebView myWebView;
    private EditText editText;
    private Button bIr;
    AdminSQLiteOpenHelper bd;
    long nreg_afectados=-1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd=new AdminSQLiteOpenHelper(this);

        myWebView = (WebView) this.findViewById(R.id.myWebView);//Componente donde se muestra la pagina web

        WebSettings webSettings = myWebView.getSettings();//Obtenemos los ajustes del webView en cuestion
        webSettings.setJavaScriptEnabled(true);//Habilitamos Js para reproducir contenido web

        editText = (EditText) this.findViewById(R.id.editText);
        url = editText.getText().toString();//Almacenamos la direccion url escrita en el editText

        //Cargamos la web con el siguiente metodo
        myWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;

            }

        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    goWeb();
                    return true;
                }
                return false;
            }
        });


        myWebView.setWebViewClient(new WebViewClient());

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myWebView.getWindowToken(), 0);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        myWebView.setWebChromeClient(new WebChromeClient() {
            private int progress;

            public void setProgress(int progress) {

                this.progress = progress;
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        bIr = (Button) findViewById(R.id.bIr);
        bIr.setOnClickListener(this);
        goWeb();
        try {
            Alta(v);
        }catch(Exception e){
            System.out.println("No se ha guardado en el historial");
        }
    }

    public void goWeb() {
        String header = "http://www.";
        String url = editText.getText().toString();

        if (URLUtil.isValidUrl(url)) {
            myWebView.loadUrl(url);
        } else {
            myWebView.loadUrl(header + url);
        }
    }

    public void anterior(View v) {
        myWebView.goBack();
    }

    public void siguiente(View v) {
        myWebView.goForward();
    }

    public void parar() {
        myWebView.stopLoading();
    }


    public void Alta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        bd.insertar(url);
    }

    public void Consulta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT URL FROM HISTORIAL WHERE URL=" + editText.getText().toString(), null);
        if (fila.moveToFirst()) {
            editText.setText(fila.getString(0));
        } else {
            Toast.makeText(this, "No hay sugerencias", Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void Alta(){
        nreg_afectados=bd.insertar(url);

        if(nreg_afectados<=0){
            Toast.makeText(this,"ERROR: No se ha insertado ningun registro",Toast.LENGTH_SHORT).show();
        }
    }

}


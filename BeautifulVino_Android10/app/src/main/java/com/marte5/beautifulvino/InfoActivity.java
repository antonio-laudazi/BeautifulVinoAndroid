package com.marte5.beautifulvino;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView textViewInfo1=findViewById(R.id.textViewInfo1);
        textViewInfo1.setText(Html.fromHtml("Nella sezione eventi, trovi la lista degli <font color='#462b35'>eventi</font></b> in programma. Cliccando su TUTTI," +
                " potrai scegliere dal menu la tua provincia e controllare solo gli eventi vicini a te."));

        TextView textViewInfo2=findViewById(R.id.textViewInfo2);
        textViewInfo2.setText(Html.fromHtml("Controlli gli eventi prenotati o acquistati, consulti la tua <font color='#462b35'>lista dei vini</font></b>, " +
                "collezioni <font color='#462b35'>badge</font></b> esperienziali e controlli quelli che puoi guadagnare ai prossimi eventi." +
                "<br>Vediamo nel dettaglio questi punti.<\\br>"));

    }

}

package com.marte5.beautifulvino;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Badge;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.Model.Utente;
import com.marte5.beautifulvino.Model.Vino;

public class UtenteDetailsActivity extends AppCompatActivity implements EventiFragment.OnListFragmentInteractionListener, BadgeFragment.OnListFragmentInteractionListener, ViniFragment.OnListFragmentInteractionListener {
    private Utente utente;
    private String TAG = UtenteDetailsActivity.class.getSimpleName();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_details);

        Bundle data = getIntent().getExtras();
        utente = data.getParcelable("utente");
        UtenteFragment newFragment = new UtenteFragment();
        Bundle bundle = new Bundle();
        fragmentManager = getSupportFragmentManager();
        bundle.putParcelable("utente", utente);
        newFragment.setArguments(bundle);
        loadFragment(newFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView c, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema) {
        Intent intent = new Intent(this, EventoDetailsActivity.class);
        intent.putExtra("evento", ev);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Vino v) {
        Intent intent = new Intent(this, VinoDetailsActivity.class);
        intent.putExtra("vino", v);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Azienda az) {
        Intent intent = new Intent(this, AziendaDetailsActivity.class);
        intent.putExtra("azienda", az);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(Badge b) {
        if (b.getEventoBadge().getIdEvento() != "") {
            Intent intent = new Intent(this, EventoDetailsActivity.class);
            intent.putExtra("evento", b.getEventoBadge());
            startActivity(intent);
        }
    }

    @Override
    public void onButtonMostraClick() {

    }
}

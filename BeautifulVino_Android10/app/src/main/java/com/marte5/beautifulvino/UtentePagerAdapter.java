package com.marte5.beautifulvino;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.SharedPreferencesManager;
import com.marte5.beautifulvino.Model.Utente;
import com.squareup.picasso.Picasso;

/**
 * Created by Marte5, Maria Tourbanova on 15/03/18.
 */

public class UtentePagerAdapter extends PagerAdapter {


    private String TAG = UtentePagerAdapter.class.getSimpleName();
    Utente ut;
    Context context;
    private ImageView imageViewUt;
    private TextView textViewNomeUt;
    private TextView textViewLivelloUt;
    private TextView textViewProssimoLivelloUt;
    private TextView textViewBiografia;
    private OnUtenteButtonsListener listener;
    private Button buttonSegui;
    private ImageButton buttonImpostazioni;

    public UtentePagerAdapter(Utente ut, Context context, OnUtenteButtonsListener listener) {
        this.ut = ut;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public int getCount() {
        return 2;
    }

    public interface OnUtenteButtonsListener {
        void onImpostazioniButtonPressed();

        void onSeguiButtonPressed();

    }

    public Object instantiateItem(final ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;
        switch (position) {
            case 0:
                view = inflater.inflate(R.layout.utente_first_view, container, false);
                imageViewUt = view.findViewById(R.id.imageViewUtente);
                textViewNomeUt = view.findViewById(R.id.textViewNomeUtente);
                textViewLivelloUt = view.findViewById(R.id.textViewLivelloUtente);
                textViewProssimoLivelloUt = view.findViewById(R.id.textViewProssimoLivelloUtente);
                buttonImpostazioni = view.findViewById(R.id.buttonImpostazioniUtente);
                buttonSegui = view.findViewById(R.id.buttonSeguiUtente);
                setButtonsVisibility();
                setButtonSegui();
                buttonImpostazioni.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onImpostazioniButtonPressed();
                        }
                    }
                });
                buttonSegui.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onSeguiButtonPressed();
                        }
                    }
                });
                imageViewUt.setClipToOutline(true);
                Picasso.with(imageViewUt.getContext()).load(Uri.parse(ut.getUrlFotoUtente())).error(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).into(imageViewUt);
                textViewNomeUt.setText(ut.getUsernameUtente());

                break;
            case 1:
                view = inflater.inflate(R.layout.utente_second_view, container, false);
                textViewBiografia = view.findViewById(R.id.textViewBiografiaUtente);
                textViewBiografia.setText(ut.getBiografiaUtente());
                textViewBiografia.setMovementMethod(new ScrollingMovementMethod());
                break;
        }
        container.addView(view, 0);
        return view;
    }

    public void refreshViews(Utente ut) {
        this.ut = ut;
        setButtonsVisibility();
        setButtonSegui();
        textViewNomeUt.setText(ut.getUsernameUtente());
        textViewBiografia.setText(ut.getBiografiaUtente());
        Picasso.with(imageViewUt.getContext()).load(Uri.parse(ut.getUrlFotoUtente())).error(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).into(imageViewUt);
    }


    private void setButtonsVisibility() {
        if (ut.getIdUtente() == "") {
            buttonImpostazioni.setVisibility(View.GONE);
            buttonSegui.setVisibility(View.GONE);
            textViewLivelloUt.setText("");
            textViewProssimoLivelloUt.setText("");
        } else {
            if (ut.getIdUtente().equals(SharedPreferencesManager.getIdUser(context))) {//mio Profilo
                textViewLivelloUt.setText("livello: "+ut.getLivelloUtente().toUpperCase());
                textViewProssimoLivelloUt.setText(ut.getPuntiMancantiUtente());
                textViewProssimoLivelloUt.setVisibility(View.VISIBLE);
                buttonImpostazioni.setVisibility(View.VISIBLE);
                buttonSegui.setVisibility(View.GONE);
            } else {
                buttonImpostazioni.setVisibility(View.GONE);
                buttonSegui.setVisibility(View.VISIBLE);
                textViewProssimoLivelloUt.setVisibility(View.GONE);
                textViewProssimoLivelloUt.setText("");
                textViewLivelloUt.setText("livello: "+ut.getLivelloUtente().toUpperCase());
            }
        }
    }

    public void setButtonSegui() {
        if (ut.getStatoUtente().equals(Utente.VALUES_STATO_OTHER) || ut.getStatoUtente() == "") {
            buttonSegui.setBackground(context.getResources().getDrawable(R.drawable.layer_white_corner_button, null));
            buttonSegui.setTextColor(context.getResources().getColor(R.color.colorRedPink));
            buttonSegui.setText("SEGUI");
        } else {
            buttonSegui.setBackground(context.getResources().getDrawable(R.drawable.border_white, null));
            buttonSegui.setTextColor(Color.WHITE);
            buttonSegui.setText("SEGUITO");
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}


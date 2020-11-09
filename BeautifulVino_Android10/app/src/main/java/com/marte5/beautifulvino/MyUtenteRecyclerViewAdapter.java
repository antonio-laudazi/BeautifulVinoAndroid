package com.marte5.beautifulvino;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.Utente;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyUtenteRecyclerViewAdapter extends RecyclerView.Adapter<MyUtenteRecyclerViewAdapter.ViewHolder> {

    private List<Utente> mUtenti;
    private final OnItemClickListener mListener;
    private Bitmap placeholder;

    public MyUtenteRecyclerViewAdapter(List<Utente> items, OnItemClickListener listener, Context context) {
        mUtenti = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.header_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ut = mUtenti.get(position);
        holder.mNomeView.setText(mUtenti.get(position).getUsernameUtente());
        holder.mDatiView.setText("livello: " + mUtenti.get(position).getLivelloUtente().toUpperCase());

        holder.mImageViewFoto.setClipToOutline(true);
       /* holder.mImageViewFoto.setImageBitmap(placeholder);
        if (holder.mImageViewFoto != null) {
            new ImageDownloaderTask(holder.mImageViewFoto).execute(mUtenti.get(position).getUrlFotoUtente());
        }*/
        Picasso.with(holder.mImageViewFoto.getContext()).load(Uri.parse(mUtenti.get(position).getUrlFotoUtente())).error(R.drawable.placeholder_user).placeholder(R.drawable.placeholder_user).fit()
                .centerCrop().into(holder.mImageViewFoto);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onItemClickUtente(holder.ut);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mUtenti == null) {
            return 0;
        } else
            return mUtenti.size();
    }

    public void updateUtenti(List<Utente> utenti_list) {
        this.mUtenti = utenti_list;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDatiView;
        public final TextView mNomeView;
        public final ImageView mImageViewFoto;
        public Utente ut;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDatiView = view.findViewById(R.id.textViewSottoHeader);
            mNomeView = view.findViewById(R.id.textViewHeader);
            mImageViewFoto = view.findViewById(R.id.imageViewHeader);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNomeView.getText() + "'";
        }
    }

    public interface OnItemClickListener {
        void onItemClickUtente(Utente utente);
    }

}

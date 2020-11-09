package com.marte5.beautifulvino;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.Azienda;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAziendaRecyclerViewAdapter extends RecyclerView.Adapter<MyAziendaRecyclerViewAdapter.ViewHolder> {

    private List<Azienda> mAziende;
    private final MyAziendaRecyclerViewAdapter.OnItemClickListener mListener;
    private String TAG = MyAziendaRecyclerViewAdapter.class.getSimpleName();

    public MyAziendaRecyclerViewAdapter(List<Azienda> items, MyAziendaRecyclerViewAdapter.OnItemClickListener listener, Context context) {
        mAziende = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_azienda_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.az = mAziende.get(position);
        holder.textViewNomeAz.setText(mAziende.get(position).getNomeAzienda());
        holder.textViewLuogoAz.setText(mAziende.get(position).getCittaAzienda()+ " - " + mAziende.get(position).getRegioneAzienda());
        holder.textViewTitoloAz.setText(mAziende.get(position).getInfoAzienda());
      //  holder.imageViewBigAz.setImageBitmap(placeholder);
        holder.imageViewBigAz.setClipToOutline(true);
       // holder.imageViewSmallAz.setImageBitmap(placeholder);
        holder.imageViewSmallAz.setClipToOutline(true);
        Picasso.with(holder.imageViewSmallAz.getContext()).load(Uri.parse(mAziende.get(position).getUrlLogoAzienda())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.imageViewSmallAz);
        Picasso.with(holder.imageViewBigAz.getContext()).load(Uri.parse(mAziende.get(position).getUrlImmagineAzienda())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.imageViewBigAz);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.OnItemClickAzienda(holder.az);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mAziende == null) {
            return 0;
        } else
            return mAziende.size();
    }

    public void updateAziende(List<Azienda> az_list) {
        this.mAziende = az_list;
        notifyDataSetChanged();
      //  Log.d("updateEventi", String.valueOf(mAziende.size()));
    }

    public interface OnItemClickListener {
        void OnItemClickAzienda(Azienda azienda);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewNomeAz;
        public final TextView textViewLuogoAz;
        public final TextView textViewTitoloAz;
        public final ImageView imageViewSmallAz;
        public final ImageView imageViewBigAz;

        public Azienda az;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewNomeAz = view.findViewById(R.id.textViewHeader);
            textViewLuogoAz = view.findViewById(R.id.textViewSottoHeader);
            textViewTitoloAz = view.findViewById(R.id.textViewInfoAzienda);
            imageViewSmallAz = view.findViewById(R.id.imageViewHeader);
            imageViewBigAz = view.findViewById(R.id.imageViewBigAzienda);
        }
    }
}

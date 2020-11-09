package com.marte5.beautifulvino;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marte5.beautifulvino.EventiFragment.OnListFragmentInteractionListener;
import com.marte5.beautifulvino.Model.Evento;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyEventoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = MyEventoRecyclerViewAdapter.class.getSimpleName();

    private List<Evento> mEventi;
    private OnListFragmentInteractionListener mListenerInteraction;
    private EventiFragment.OnLoadMoreEventiListener mListenerOnLoadMore;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    private final int VIEW_ITEM = 0;
    private final int VIEW_PROGRESS = 1;
    private boolean isLoading;
    private boolean prezzoHidden;

    public MyEventoRecyclerViewAdapter(RecyclerView recyclerView, List<Evento> items, EventiFragment.OnLoadMoreEventiListener onLoadMoreListener, OnListFragmentInteractionListener listener, boolean prezzoHidden) {
        mEventi = items;
        mListenerInteraction = listener;
        mListenerOnLoadMore = onLoadMoreListener;
        this.prezzoHidden=prezzoHidden;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && mEventi.size() > 0) {
                    isLoading = true;
                    if (mListenerOnLoadMore != null) {
                        mListenerOnLoadMore.onLoadMoreEventi();
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_evento, parent, false);
            return new EventoViewHolder(view);
        } else if (viewType == VIEW_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_bar, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventoViewHolder) {
            initLayoutEvento((EventoViewHolder) holder, position);
        } else {
            initLayoutLoader((LoadingViewHolder) holder);
        }
    }

    private void initLayoutEvento(final EventoViewHolder holder, int position) {
        holder.ev = mEventi.get(position);
        holder.mTitoloView.setText(mEventi.get(position).getTitoloEvento());
        holder.mDataView.setText(mEventi.get(position).getDataStringEvento());
        holder.mTemaView.setText(mEventi.get(position).getTemaEvento());
        holder.mCittaView.setText(mEventi.get(position).getCittaEvento());

        holder.mImageViewEvento.setClipToOutline(true);
        holder.mPrezzoView.setText(mEventi.get(position).getPrezzoStringEvento());

        if (prezzoHidden && !holder.ev.getStatoEvento().equals(Evento.VALUES_STATO_OTHER)){
            holder.mPrezzoView.setVisibility(View.INVISIBLE);
            holder.mPrenotatoView.setVisibility(View.VISIBLE);
            if (holder.ev.getStatoEvento().equals(Evento.VALUES_STATO_ACQUISTATO)){
                holder.mPrenotatoView.setText("ACQUISTATO");
            }else{
                holder.mPrenotatoView.setText("PRENOTATO");
            }
        }else {
            holder.mPrenotatoView.setVisibility(View.INVISIBLE);
            holder.mPrezzoView.setVisibility(View.VISIBLE);
        }

        Picasso.with(holder.mImageViewEvento.getContext())
                .load(Uri.parse(mEventi.get(position).getUrlFotoEvento()))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop()
                .into(holder.mImageViewEvento);

        ViewCompat.setTransitionName(holder.mImageViewEvento, mEventi.get(position).getIdEvento());
        ViewCompat.setTransitionName(holder.mTitoloView, mEventi.get(position).getIdEvento() + "titolo" + position);
        ViewCompat.setTransitionName(holder.mDataView, mEventi.get(position).getIdEvento() + "data" + position);
        ViewCompat.setTransitionName(holder.mCittaView, mEventi.get(position).getIdEvento() + "citta" + position);
        ViewCompat.setTransitionName(holder.mImagePin, mEventi.get(position).getIdEvento() + "pin" + position);
        ViewCompat.setTransitionName(holder.mPrezzoView, mEventi.get(position).getIdEvento() + "prezzo" + position);
        ViewCompat.setTransitionName(holder.whiteView, mEventi.get(position).getIdEvento() + "white" + position);
        ViewCompat.setTransitionName(holder.mTemaView, mEventi.get(position).getIdEvento() + "tema" + position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListenerInteraction) {
                    mListenerInteraction.onListFragmentInteraction(holder.ev, holder.mImageViewEvento,
                            holder.mCittaView, holder.mTitoloView,
                            holder.mImagePin, holder.mPrezzoView,
                            holder.mDataView, holder.whiteView, holder.mTemaView);
                }
            }
        });
    }

    private void initLayoutLoader(final LoadingViewHolder holder) {
        LoadingViewHolder loadingViewHolder =  holder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }


    @Override
    public int getItemViewType(int position) {
        return mEventi.get(position) == null ? VIEW_PROGRESS : VIEW_ITEM;
    }

    @Override
    public int getItemCount() {
        if (mEventi == null) {
            return 0;
        } else
            return mEventi.size();
    }

    public void updateEventi(List<Evento> eventi_list) {
        mEventi = eventi_list;
        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler.post(r);

      //  Log.d(TAG, "updateEventi " + String.valueOf(mEventi.size()));

    }

    public void setLoaded(List<Evento> eventi_list) {
        isLoading = false;
        if (eventi_list != null) {
            updateEventi(eventi_list);
        }
    }

    public class EventoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitoloView;
        public final TextView mDataView;
        public final TextView mPrezzoView;
        public final TextView mPrenotatoView;
        public final TextView mCittaView;
        public final TextView mTemaView;
        public final ImageView mImageViewEvento;
        public final ImageView mImagePin;
        public final View whiteView;
        public Evento ev;

        public EventoViewHolder(View view) {
            super(view);
            mView = view;
            mTitoloView = view.findViewById(R.id.textViewTitolo);
            mDataView = view.findViewById(R.id.textViewData);
            mPrezzoView = view.findViewById(R.id.textViewPrezzo);
            mPrenotatoView = view.findViewById(R.id.textViewPrenotato);
            mTemaView = view.findViewById(R.id.textViewTema);
            mCittaView = view.findViewById(R.id.textViewCittaEvento);
            mImageViewEvento = view.findViewById(R.id.imageViewEvento);
            mImagePin = view.findViewById(R.id.imageViewPin);
            whiteView = view.findViewById(R.id.viewTwo);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mTemaView.getText() + "'";
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }

    }
}
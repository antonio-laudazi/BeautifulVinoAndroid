package com.marte5.beautifulvino;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marte5.beautifulvino.FeedFragment.OnListFragmentInteractionListener;
import com.marte5.beautifulvino.Model.Feed;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyFeedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Feed> mFeed;
    private final OnListFragmentInteractionListener mListener;
    private FeedFragment.OnLoadMoreFeedListener mListenerOnLoadMore;

    private int visibleThreshold = 1;
    private int lastVisibleItem, totalItemCount;

    private final int VIEW_PROGRESS = -1;
    private boolean isLoading;

    public MyFeedRecyclerViewAdapter(RecyclerView recyclerView, List<Feed> items, OnListFragmentInteractionListener listener, FeedFragment.OnLoadMoreFeedListener onLoadMoreListener, Context context) {
        mFeed = items;
        mListener = listener;
        mListenerOnLoadMore = onLoadMoreListener;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && mFeed.size() > 0) {
                    isLoading = true;
                    if (mListenerOnLoadMore != null) {
                        mListenerOnLoadMore.onLoadMoreFeed();
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Feed.VALUES_TIPO_FEED_POST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_list_item, parent, false);
            return new ViewHolderPost(view);
        }/*else if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_post_list_item, parent, false);
            return new ViewHolderPost(view);
        }*/  else if (viewType == Feed.VALUES_TIPO_FEED_PUBBLICITA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_pubblicita_list_item, parent, false);
            return new ViewHolderPubblicita(view);
        } else if (viewType == Feed.VALUES_TIPO_FEED_AZIENDA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_azienda_list_item, parent, false);
            return new ViewHolderAzienda(view);
        } else if (viewType == Feed.VALUES_TIPO_FEED_VINO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_azione_list_item, parent, false);
            return new ViewHolderAzione(view);
        } else if (viewType == Feed.VALUES_TIPO_FEED_EVENTO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_azione_list_item, parent, false);
            return new ViewHolderAzione(view);
        }else if (viewType == VIEW_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_bar, parent, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Feed.VALUES_TIPO_FEED_PUBBLICITA:
                initLayoutPubblicita((ViewHolderPubblicita) holder, position);
                break;
            case Feed.VALUES_TIPO_FEED_AZIENDA:
                initLayoutAzienda((ViewHolderAzienda) holder, position);
                break;
            case Feed.VALUES_TIPO_FEED_VINO:
                initLayoutAzione((ViewHolderAzione) holder, position);
                break;
            case Feed.VALUES_TIPO_FEED_EVENTO:
                initLayoutAzione((ViewHolderAzione) holder, position);
                break;
            case Feed.VALUES_TIPO_FEED_POST:
                initLayoutPost((ViewHolderPost) holder, position);
                break;
            default:
               // initLayoutPost((ViewHolderPost) holder, position);
                  initLayoutLoader((LoadingViewHolder) holder);
                break;
        }
    }

    private void initLayoutLoader(final LoadingViewHolder holder) {
        LoadingViewHolder loadingViewHolder =  holder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    private void initLayoutPost(final ViewHolderPost holder, int position) {
        holder.f = mFeed.get(position);
        holder.textViewTitoloPost.setTextColor(Color.WHITE);
        holder.textViewTitoloPost.setText(mFeed.get(position).getTitoloFeed());
        holder.textViewHeaderPost.setTextColor(Color.WHITE);
        holder.textViewHeaderPost.setText(mFeed.get(position).getHeaderFeed());
        holder.textViewSottoHeaderPost.setTextColor(Color.WHITE);
        holder.textViewSottoHeaderPost.setText(mFeed.get(position).getSottoHeaderFeed());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.textViewTestoPost.setText(Html.fromHtml(mFeed.get(position).getTestoFeed(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.textViewTestoPost.setText(Html.fromHtml(mFeed.get(position).getTestoFeed()));
        }

        Picasso.with(holder.imageViewPost.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewPost);

        Picasso.with(holder.imageViewSmallPost.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineHeaderFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewSmallPost);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.f);
                }
            }
        });
        holder.relativeLayoutHeader.setBackgroundResource(R.color.colorRedPink);
        holder.relativeLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onListHeaderFragmentInteraction(holder.f);
            }
    });
    }

    private void initLayoutPubblicita(final ViewHolderPubblicita holder, int position) {
        holder.f = mFeed.get(position);
        if (mFeed.get(position).getTestoLabelFeed()==""){
            holder.textViewLabelPubblicita.setVisibility(View.INVISIBLE);
        }else{
            holder.textViewLabelPubblicita.setText(mFeed.get(position).getTestoLabelFeed());
            holder.textViewLabelPubblicita.setVisibility(View.VISIBLE);
        }
        holder.textViewTitoloPubblicita.setText(mFeed.get(position).getTitoloFeed());
        holder.imageViewSfondoPubblicita.setClipToOutline(true);

        Picasso.with(holder.imageViewSfondoPubblicita.getContext())
                .load(Uri.parse(mFeed.get(position).getUrlImmagineFeed()))
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerCrop().into(holder.imageViewSfondoPubblicita);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.f);
                }
            }
        });
    }

    private void initLayoutAzienda(final ViewHolderAzienda holder, int position) {
        holder.f = mFeed.get(position);
        holder.textViewTitolo.setText(mFeed.get(position).getTitoloFeed());
        holder.textViewHeaderAz.setText(mFeed.get(position).getHeaderFeed());
        holder.textViewSottoHeaderAz.setText(mFeed.get(position).getSottoHeaderFeed());
        holder.imageViewBigAz.setClipToOutline(true);
        holder.imageViewSmallAz.setClipToOutline(true);
        Picasso.with(holder.imageViewBigAz.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewBigAz);
        Picasso.with(holder.imageViewSmallAz.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineHeaderFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewSmallAz);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.f);
                }
            }
        });
        holder.relativeLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onListHeaderFragmentInteraction(holder.f);
            }
        });

    }

    private void initLayoutAzione(final ViewHolderAzione holder, int position) {
        holder.f = mFeed.get(position);
        holder.textViewTitolo.setText(mFeed.get(position).getTitoloFeed());
        holder.textViewHeaderAz.setText(mFeed.get(position).getHeaderFeed());
        holder.textViewSottoHeaderAz.setText(mFeed.get(position).getSottoHeaderFeed());
        holder.textViewNomeAzione.setText(mFeed.get(position).getTitoloFeed());
        holder.textViewSottoNome.setClipToOutline(true);
        holder.textViewTestoAzione.setText(mFeed.get(position).getSottoHeaderFeed());
        holder.imageViewAzione.setClipToOutline(true);

        Picasso.with(holder.imageViewAzione.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewAzione);
        Picasso.with(holder.imageViewSmallAzione.getContext()).load(Uri.parse(mFeed.get(position).getUrlImmagineHeaderFeed())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewSmallAzione);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.f);
                }
            }
        });

        holder.relativeLayoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onListHeaderFragmentInteraction(holder.f);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mFeed == null) {
            return 0;
        } else
            return mFeed.size();
    }

    @Override
    public int getItemViewType(int position) {
        Feed f = mFeed.get(position);
        if (f == null)
            return VIEW_PROGRESS;
        else
            return f.getTipoFeed();
    }

    public void updateFeed(List<Feed> feedArrayList) {
        this.mFeed = feedArrayList;

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                notifyDataSetChanged();
            }
        };
        handler.post(r);

       // Log.d("updateFeed", String.valueOf(mFeed.size()));

    }


    public void setLoaded(List<Feed> feedList) {
        isLoading = false;
        if (feedList != null) {
            updateFeed(feedList);
        }
    }

    public class ViewHolderPubblicita extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewLabelPubblicita;
        public final TextView textViewTitoloPubblicita;
        public final ImageView imageViewSfondoPubblicita;
        public Feed f;

        public ViewHolderPubblicita(View view) {
            super(view);
            mView = view;
            textViewLabelPubblicita = view.findViewById(R.id.textViewLabelPubblicita);
            textViewTitoloPubblicita = view.findViewById(R.id.textViewTitoloPubblicita);
            imageViewSfondoPubblicita = view.findViewById(R.id.imageViewPubblicita);
        }
    }


    public class ViewHolderPost extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewTitoloPost;
        public final TextView textViewTestoPost;
        public final ImageView imageViewPost;
        public final ImageView imageViewSmallPost;
        public Feed f;
        public final RelativeLayout relativeLayoutHeader;

        public final TextView textViewHeaderPost;
        public final TextView textViewSottoHeaderPost;

        public ViewHolderPost(View view) {
            super(view);
            mView = view;

            textViewHeaderPost = view.findViewById(R.id.textViewHeader);
            textViewSottoHeaderPost = view.findViewById(R.id.textViewSottoHeader);
            textViewTitoloPost = view.findViewById(R.id.textViewTitoloPost);
            textViewTestoPost = view.findViewById(R.id.textViewTestoPost);
            imageViewPost = view.findViewById(R.id.imageViewPost);
            imageViewSmallPost = view.findViewById(R.id.imageViewHeader);
            relativeLayoutHeader =view.findViewById(R.id.relativeLayoutHeader);
        }

    }

    public class ViewHolderAzienda extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewHeaderAz;
        public final TextView textViewSottoHeaderAz;
        public final TextView textViewTitolo;
        public final ImageView imageViewSmallAz;
        public final ImageView imageViewBigAz;
        public final RelativeLayout relativeLayoutHeader;

        public Feed f;

        public ViewHolderAzienda(View view) {
            super(view);
            mView = view;

            textViewHeaderAz = view.findViewById(R.id.textViewHeader);
            textViewSottoHeaderAz = view.findViewById(R.id.textViewSottoHeader);
            textViewTitolo = view.findViewById(R.id.textViewInfoAzienda);
            imageViewSmallAz = view.findViewById(R.id.imageViewHeader);
            imageViewBigAz = view.findViewById(R.id.imageViewBigAzienda);
            relativeLayoutHeader =view.findViewById(R.id.relativeLayoutHeader);
        }
    }

    public class ViewHolderAzione extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewHeaderAz;
        public final TextView textViewSottoHeaderAz;
        public final TextView textViewTitolo;
        public final TextView textViewNomeAzione;
        public final TextView textViewSottoNome;
        public final ImageView imageViewAzione;
        public final ImageView imageViewSmallAzione;
        public final TextView textViewTestoAzione;
        public final RelativeLayout relativeLayoutHeader;

        public Feed f;

        public ViewHolderAzione(View view) {
            super(view);
            mView = view;

            textViewHeaderAz = view.findViewById(R.id.textViewHeader);
            textViewSottoHeaderAz = view.findViewById(R.id.textViewSottoHeader);
            textViewTitolo = view.findViewById(R.id.textViewTitoloAzione);
            textViewNomeAzione = view.findViewById(R.id.textViewNomeAzione);
            textViewSottoNome = view.findViewById(R.id.textViewSottoNomeAzione);
            textViewTestoAzione = view.findViewById(R.id.textViewTestoAzione);
            imageViewAzione = view.findViewById(R.id.imageViewAzione);
            imageViewSmallAzione = view.findViewById(R.id.imageViewHeader);
            relativeLayoutHeader =view.findViewById(R.id.relativeLayoutHeader);
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


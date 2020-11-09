package com.marte5.beautifulvino;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.ViniFragment.OnListFragmentInteractionListener;
import com.marte5.beautifulvino.dummy.ListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyVinoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListItem> mItems;
    private boolean nascondi;
    private final OnListFragmentInteractionListener mListener;

    public MyVinoRecyclerViewAdapter(List<ListItem> items, OnListFragmentInteractionListener listener, boolean nascondiMostra) {
        mItems = items;
        mListener = listener;
        nascondi = nascondiMostra;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ListItem.TYPE_AZ) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_lista_vini, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == ListItem.TYPE_VINO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vino_list_item, parent, false);
            return new ItemViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            initLayoutHeader((HeaderViewHolder) holder, position);
        } else if (holder instanceof ItemViewHolder) {
            initLayoutItem((ItemViewHolder) holder, position);

        }
    }

    private void initLayoutHeader(final HeaderViewHolder holder, int position) {
        Azienda az = (Azienda) mItems.get(position);
        holder.azienda = az;
        holder.headerTitle.setText(az.getNomeAzienda());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(((HeaderViewHolder) holder).azienda);
                }
            }
        });
    }

    private void initLayoutItem(final ItemViewHolder holder, int position) {
        Vino v = (Vino) mItems.get(position);
        if (mItems.size() == position + 1 || mItems.get(position + 1).getType() == ListItem.TYPE_AZ) {
            holder.bindBackground(true);
            holder.nascondiFrameMostra(nascondi);
        } else {
            holder.bindBackground(false);
            holder.nascondiFrameMostra(true);
        }
        holder.vino = v;
        holder.mTextViewNomeVino.setText(v.getNomeVino());
        holder.mTextViewPercentVino.setText(v.getUvaggioVino());
        holder.mTextViewInBreveVino.setText(v.getInBreveVino());
        holder.mImageViewVino.setClipToOutline(true);
        Picasso.with(holder.mImageViewVino.getContext()).load(Uri.parse(v.getUrlLogoVino())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).into(holder.mImageViewVino);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(((ItemViewHolder) holder).vino);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    // determine which layout to use for the row
    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    public void updateVini(List<ListItem> vini_list) {
        this.mItems = vini_list;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTextViewNomeVino;
        public final TextView mTextViewInBreveVino;
        public final TextView mTextViewPercentVino;
        public final ImageView mImageViewVino;
        public Vino vino;
        public final LinearLayout linearLayout;
        public final FrameLayout frameLayoutMostra;
        public final Button buttonMostra;

        private void bindBackground(boolean last) {
            if (last) {
                linearLayout.setBackground(linearLayout.getContext().getResources().getDrawable(R.drawable.layer_lista_vini_last, null));
            } else {
                linearLayout.setBackground(linearLayout.getContext().getResources().getDrawable(R.drawable.layer_lista_vini_item, null));
            }
        }

        private void nascondiFrameMostra(boolean nascondi) {
            if (nascondi) {
                frameLayoutMostra.setVisibility(View.GONE);
            } else {
                frameLayoutMostra.setVisibility(View.VISIBLE);
                buttonMostra.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mListener) {
                            mListener.onButtonMostraClick();
                        }
                    }
                });
            }
        }

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            mTextViewNomeVino = view.findViewById(R.id.textViewNomeVino);
            mTextViewInBreveVino = view.findViewById(R.id.textViewInBreveVino);
            mTextViewPercentVino = view.findViewById(R.id.textViewPercentVino);
            mImageViewVino = view.findViewById(R.id.imageViewVino);
            linearLayout = view.findViewById(R.id.linearLayoutVinoListItem);
            frameLayoutMostra = view.findViewById(R.id.framelayoutMostra);
            buttonMostra=view.findViewById(R.id.buttonMostraVini);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextViewInBreveVino.getText() + "'";
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView headerTitle;
        public Azienda azienda;

        public HeaderViewHolder(View view) {
            super(view);
            mView = view;
            headerTitle = itemView.findViewById(R.id.header_id);
        }
    }
}


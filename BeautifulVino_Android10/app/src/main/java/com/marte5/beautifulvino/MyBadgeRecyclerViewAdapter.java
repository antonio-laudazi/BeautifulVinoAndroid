package com.marte5.beautifulvino;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marte5.beautifulvino.BadgeFragment.OnListFragmentInteractionListener;
import com.marte5.beautifulvino.Model.Badge;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyBadgeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Badge> badges;
    private final int VIEW_SECTION = -1;
    private final int VIEW_BADGE = 1;
    private final OnListFragmentInteractionListener mListener;

    public MyBadgeRecyclerViewAdapter(List<Badge> items, OnListFragmentInteractionListener listener, Context context) {
        badges = items;
        int i = 0;
        mListener = listener;
        while (badges != null && i < badges.size() && badges.get(i).getTuoBadge().equals(Badge.KEY_TUO_BADGE)) {
            i++;
        }
        if (badges != null && i < badges.size()) {
            badges.add(i, null);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_SECTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_badge_section, parent, false);
            return new ViewHolderGuadagna(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_badge, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_BADGE:
                initLayoutBadge((ViewHolder) holder, position);
                break;
            default:
                initLayoutLoader((ViewHolderGuadagna) holder);
                break;
        }
    }

    private void initLayoutLoader(final ViewHolderGuadagna holder) {
    }

    private void initLayoutBadge(final ViewHolder holder, int position) {
        holder.badge = badges.get(position);
        if (holder.badge.getTuoBadge().equals("S")) {
            holder.viewTransparentBadge.setVisibility(View.INVISIBLE);
        } else {
            holder.viewTransparentBadge.setVisibility(View.VISIBLE);
        }
        if (badges.get(position).getInfoBadge() == "") {
            holder.textViewTitoloBadge.setVisibility(View.GONE);
            holder.textViewTestoBadge.setVisibility(View.GONE);
            holder.textViewTitoloCenteredBadge.setVisibility(View.VISIBLE);
            holder.textViewTitoloCenteredBadge.setText(badges.get(position).getNomeBadge());
        } else {
            holder.textViewTitoloBadge.setVisibility(View.VISIBLE);
            holder.textViewTestoBadge.setVisibility(View.VISIBLE);
            holder.textViewTitoloCenteredBadge.setVisibility(View.GONE);
            holder.textViewTitoloBadge.setText(badges.get(position).getNomeBadge());
            holder.textViewTestoBadge.setText(badges.get(position).getInfoBadge());
        }

        Picasso.with(holder.imageViewBadge.getContext()).load(Uri.parse(badges.get(position).getUrlLogoBadge())).error(R.drawable.placeholder).placeholder(R.drawable.placeholder).fit()
                .centerCrop().into(holder.imageViewBadge);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.badge);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        if (badges != null)
            return badges.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Badge b = badges.get(position);
        if (b == null)
            return VIEW_SECTION;
        else
            return VIEW_BADGE;
    }

    public void updateBadge(List<Badge> badgeUtente, boolean mioProfilo) {
        this.badges = badgeUtente;
        if (mioProfilo) {
            int i = 0;
            while (badges != null && i < badges.size() && badges.get(i).getTuoBadge().equals(Badge.VALUES_TUO_SI) ) {
                i++;
            }
            if (badges != null && i < badges.size()) {
                badges.add(i, null);
            }
        }
        notifyDataSetChanged();
        //  Log.d("updateBadge" , String.valueOf(mValues.size()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewTitoloBadge;
        public final TextView textViewTitoloCenteredBadge;
        public final TextView textViewTestoBadge;
        public final ImageView imageViewBadge;
        public final View viewTransparentBadge;
        public Badge badge;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewTitoloBadge = view.findViewById(R.id.textViewTitoloBadge);
            textViewTitoloCenteredBadge = view.findViewById(R.id.textViewTitoloCenteredBadge);
            textViewTestoBadge = view.findViewById(R.id.textViewTestoBadge);
            viewTransparentBadge = view.findViewById(R.id.viewTransparentBadge);
            imageViewBadge = view.findViewById(R.id.imageViewBadge);
        }
    }

    public class ViewHolderGuadagna extends RecyclerView.ViewHolder {

        public final View mView;

        public ViewHolderGuadagna(View view) {
            super(view);
            mView = view;
        }
    }
}

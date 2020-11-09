package com.marte5.beautifulvino;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marte5.beautifulvino.Model.Badge;

import java.util.List;

public class BadgeFragment extends Fragment {

    private List<Badge> badges;
    public MyBadgeRecyclerViewAdapter adapterBadge;
    private OnListFragmentInteractionListener mListener;

    public BadgeFragment() {
    }

    public static BadgeFragment newInstance() {
        BadgeFragment fragment = new BadgeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            badges = (List<Badge>) getArguments().get("badge");
        }
        adapterBadge = new MyBadgeRecyclerViewAdapter(badges, mListener, this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_badge_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.listBadge);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapterBadge);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Badge b);
    }


}

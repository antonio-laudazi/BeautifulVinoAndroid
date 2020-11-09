package com.marte5.beautifulvino;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Feed;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FeedFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnListFragmentInteractionListener mListener;
    private List<Feed> feedArray;
    private MyFeedRecyclerViewAdapter adapterFeed;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OnLoadMoreFeedListener mLoadListener;
    private RecyclerView recyclerView;

    public FeedFragment() {
    }

    public static FeedFragment newInstance(int columnCount) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.feedArray = (List<Feed>) getArguments().get("feed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
        recyclerView = view.findViewById(R.id.listFeed);
        Context context = view.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterFeed = new MyFeedRecyclerViewAdapter(recyclerView, feedArray, mListener, mLoadListener, this.getContext());
        recyclerView.setAdapter(adapterFeed);
        mSwipeRefreshLayout = view.findViewById(R.id.swiperefreshFeed);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFeed();
            }
        });
        return view;
    }

    private void refreshFeed() {
        ((MainActivity)getActivity()).getFeed(true);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateFeedFragment(List f) {
        this.feedArray = f;
        if (adapterFeed != null) {
            adapterFeed.updateFeed(this.feedArray);
    }
    }

    public void stopLoading(List f) {
        if (f != null) {
            this.feedArray = f;
        }
        adapterFeed.setLoaded(this.feedArray);
    }

    public void addLoading(){

        recyclerView.post(new Runnable() {
            public void run() {
                feedArray.add(null);
                adapterFeed.notifyItemInserted(feedArray.size() - 1);
            }
        });
    }

    public void removeLoading(){
        recyclerView.post(new Runnable() {
            public void run() {
                feedArray.remove(feedArray.size() - 1);
                adapterFeed.notifyItemRemoved(feedArray.size());
            }
        });
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
        if (context instanceof OnLoadMoreFeedListener) {
            mLoadListener = (OnLoadMoreFeedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mLoadListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Feed f);
        void onListHeaderFragmentInteraction(Feed f);
    }

    public interface OnLoadMoreFeedListener {
        void onLoadMoreFeed();
    }
}

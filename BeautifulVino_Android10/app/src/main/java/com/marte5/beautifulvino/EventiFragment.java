package com.marte5.beautifulvino;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.marte5.beautifulvino.Model.Evento;
import com.marte5.beautifulvino.RegistrazioneLogin.CognitoSyncClientManager;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventiFragment extends Fragment {

    private boolean disableSwape;
    private boolean prezzoHidden;
    private OnListFragmentInteractionListener mInteractionListener;
    private OnLoadMoreEventiListener mLoadListener;
    private List<Evento> eventi;
    public MyEventoRecyclerViewAdapter adapterEventi;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;

    public EventiFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventiFragment newInstance() {
        EventiFragment fragment = new EventiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.eventi = (List<Evento>) getArguments().get("eventi");
            disableSwape = getArguments().getBoolean("disableSwape", false);
            prezzoHidden = getArguments().getBoolean("prezzoHidden", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento_list, container, false);
        final Context context = view.getContext();
        recyclerView = view.findViewById(R.id.listEventi);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterEventi = new MyEventoRecyclerViewAdapter(recyclerView, this.eventi, mLoadListener, mInteractionListener, prezzoHidden);
        recyclerView.setAdapter(adapterEventi);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefreshEventi);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEventi();
            }
        });
        mSwipeRefreshLayout.setEnabled(!disableSwape);
        return view;
    }

    private void refreshEventi() {
        ((MainActivity) getActivity()).refreshEventi();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void updateEventiFragment(List ev) {
        this.eventi = ev;
        if (adapterEventi != null) {
            adapterEventi.updateEventi(this.eventi);
        }
    }

    public void stopLoading(List ev) {

        if (ev != null) {
            this.eventi = ev;
        }
        adapterEventi.setLoaded(this.eventi);
    }

    public void addLoading(){
        recyclerView.post(new Runnable() {
            public void run() {
                eventi.add(null);
                adapterEventi.notifyItemInserted(eventi.size() - 1);
            }
        });
    }

    public void removeLoading(){
        recyclerView.post(new Runnable() {
            public void run() {
                eventi.remove(eventi.size() - 1);
                adapterEventi.notifyItemRemoved(eventi.size());
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mInteractionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        if (context instanceof OnLoadMoreEventiListener) {
            mLoadListener = (OnLoadMoreEventiListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
        mLoadListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Evento ev, ImageView sharedImageView, TextView citta, TextView titolo, ImageView pinImage, TextView prezzo, TextView data, View whiteView, TextView tema);

    }

    public interface OnLoadMoreEventiListener {
        void onLoadMoreEventi();
    }

}

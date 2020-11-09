package com.marte5.beautifulvino;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marte5.beautifulvino.Model.Azienda;
import com.marte5.beautifulvino.Model.Vino;
import com.marte5.beautifulvino.dummy.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ViniFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private List<ListItem> vini;
    private List<Azienda> aziende;
    private MyVinoRecyclerViewAdapter adapterVini;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ViniFragment() {
    }

    // TODO: Customize parameter initialization
    public static ViniFragment newInstance() {
        ViniFragment fragment = new ViniFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vini = new ArrayList<>();
        if (getArguments() != null) {
            aziende = (List<Azienda>) getArguments().get("aziende");
            if (aziende != null) {
                for (int i = 0; i < aziende.size(); i++) {
                    Azienda c = aziende.get(i);
                    vini.add(c);
                    for (ListItem v : c.getViniAzienda()) {
                        vini.add(v);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vino_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.listVini);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapterVini = new MyVinoRecyclerViewAdapter(vini, mListener, true);
        recyclerView.setAdapter(adapterVini);
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

    public void updateViniFragment(List<Azienda> aziende) {
        this.aziende = aziende;
        vini.clear();
        for (int i = 0; i < this.aziende.size(); i++) {
            Azienda c = this.aziende.get(i);
            vini.add(c);
            for (ListItem v : c.getViniAzienda()) {
                vini.add(v);
            }
        }
        adapterVini.updateVini(vini);
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Vino v);
        void onListFragmentInteraction(Azienda az);
        void onButtonMostraClick();
    }
}
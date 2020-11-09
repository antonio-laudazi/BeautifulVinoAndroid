package com.marte5.beautifulvino;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marte5.beautifulvino.Model.Provincia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marte5, Maria Tourbanova on 15/02/18.
 */

class ListViewProvinceAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<Provincia> provinceList = null;
    private ArrayList<Provincia> provinceArraylist;

    public ListViewProvinceAdapter(Context context, List<Provincia> provinceList) {
        mContext = context;
        this.provinceList = provinceList;
        inflater = LayoutInflater.from(mContext);
        this.provinceArraylist = new ArrayList<Provincia>();
        this.provinceArraylist.addAll(provinceList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return provinceList.size();
    }

    @Override
    public Provincia getItem(int position) {
        return provinceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_provincia, null);
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(provinceList.get(position).getNomeProvincia());

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        provinceList.clear();
        if (charText.length() == 0) {
            provinceList.addAll(provinceArraylist);
        } else {
            for (Provincia wp : provinceArraylist) {
                if (wp.getNomeProvincia().toLowerCase(Locale.getDefault()).contains(charText)) {
                    provinceList.add(wp);
                }
            }
        }
        Collections.sort(provinceList);
        notifyDataSetChanged();
    }
    public void refreshProvince(ArrayList<Provincia> events) {
        this.provinceList = events;
        this.provinceArraylist = new ArrayList<Provincia>();
        this.provinceArraylist.addAll(provinceList);
        notifyDataSetChanged();
    }
}

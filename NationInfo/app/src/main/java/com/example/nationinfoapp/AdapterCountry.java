package com.example.nationinfoapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterCountry extends BaseAdapter {
    private List<Country> countries;

    public AdapterCountry(List<Country> countries) {
        this.countries = countries;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Object getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if(convertView == null){
            row = View.inflate(parent.getContext(), R.layout.row_item, null);

        }else{
            row = convertView;
        }
        ((TextView)row.findViewById(R.id.txtCountryName)).setText(countries.get(position).getCountryName());

        return row;
    }
}


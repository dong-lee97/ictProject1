package com.example.ictproject.fragment_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.ictproject.R;
import com.example.ictproject.RegionData;
import com.example.ictproject.RegionDataViewHolder;

import java.util.ArrayList;


public class RegionAdapter extends BaseAdapter {
    Context mContext = null;
    ArrayList<RegionData> region;

    public RegionAdapter(Context context, ArrayList<RegionData> data){
        mContext = context;
        region = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegionDataViewHolder regionDataViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_seoul, null, true);
            regionDataViewHolder = new RegionDataViewHolder();
            regionDataViewHolder.region = convertView.findViewById(R.id.seoulRegion);
            convertView.setTag(regionDataViewHolder);
        } else {
            regionDataViewHolder = (RegionDataViewHolder) convertView.getTag();
        }

        regionDataViewHolder.region.setText(region.get(position).getRegion());

        return convertView;
    }


    @Override
    public RegionData getItem(int position) {
        return region.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return region.size();
    }

}

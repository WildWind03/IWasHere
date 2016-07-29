package com.noveogroup.teamzolotov.iwashere.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.util.RegionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dserov on 29/07/16.
 */
public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    private List<Region> regions;
    private Context context;

    public RegionAdapter(List<Region> regions, Context context) {
        this.regions = regions;
        this.context = context;
    }


    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.region_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegionAdapter.ViewHolder holder, int position) {
        Region region = regions.get(position);
        holder.regionImage.setImageResource(RegionUtil.getRegionIconResource(region.getOsmId()));
        holder.regionName.setText(RegionUtil.getRegionNameResource(region.getOsmId()));
        holder.regionVisited.setChecked(region.isVisited());
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.regionImage)
        protected ImageView regionImage;

        @BindView(R.id.regionName)
        protected TextView regionName;

        @BindView(R.id.regionCheckbox)
        protected CheckBox regionVisited;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

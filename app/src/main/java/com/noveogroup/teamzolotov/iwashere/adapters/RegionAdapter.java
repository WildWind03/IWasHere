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
import com.noveogroup.teamzolotov.iwashere.RegionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dserov on 29/07/16.
 */
public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    private static final String TAG = RegionAdapter.class.getSimpleName();

    private final List<Region> regions = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;
    private RegionUpdateListener listener;

    public RegionAdapter(Context context, RegionUpdateListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.listener = listener;
    }


    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.region_list_item, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(final RegionAdapter.ViewHolder holder, final int position) {
        final Region region = regions.get(holder.getAdapterPosition());
        holder.regionImage.setImageResource(RegionUtils.getRegionIconResource(region.getOsmId()));
        holder.regionName.setText(RegionUtils.getRegionNameResource(region.getOsmId()));
        holder.regionVisited.setChecked(region.isVisited());
    }

    @Override
    public int getItemCount() {
        return regions.size();
    }

    public void clear() {
        regions.clear();
        notifyDataSetChanged();
    }

    public void replaceData(List<Region> newRegions) {
        regions.clear();
        regions.addAll(newRegions);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RegionUpdateListener listener;

        @BindView(R.id.regionImage)
        protected ImageView regionImage;

        @BindView(R.id.regionName)
        protected TextView regionName;

        @BindView(R.id.regionCheckbox)
        protected CheckBox regionVisited;

        public ViewHolder(View itemView, RegionUpdateListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @OnClick(R.id.regionCheckbox)
        public void onClick(CheckBox checkBox) {
            if (listener != null) {
                Region region = regions.get(getAdapterPosition());
                region.setVisited(checkBox.isChecked());
                listener.onUpdate(region);
            }
        }
    }

    public interface RegionUpdateListener {
        void onUpdate(Region region);
    }
}

package com.noveogroup.teamzolotov.iwashere.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.util.RegionUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        holder.regionImage.setImageResource(RegionUtil.getRegionIconResource(region.getOsmId()));
        holder.regionName.setText(RegionUtil.getRegionNameResource(region.getOsmId()));
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

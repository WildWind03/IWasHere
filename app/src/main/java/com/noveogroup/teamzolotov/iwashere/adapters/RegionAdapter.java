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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dserov on 29/07/16.
 */
public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

    private static final String TAG = RegionAdapter.class.getSimpleName();

    private List<Region> regions;
    private Context context;
    private Dao<Region, Integer> dao;

    public RegionAdapter(List<Region> regions, Context context, Dao<Region, Integer> dao) {
        this.regions = regions;
        this.context = context;
        this.dao = dao;
    }


    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.region_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegionAdapter.ViewHolder holder, final int position) {
        final Region region = regions.get(position);
        holder.regionImage.setImageResource(RegionUtil.getRegionIconResource(region.getOsmId()));
        holder.regionName.setText(RegionUtil.getRegionNameResource(region.getOsmId()));
        holder.regionVisited.setChecked(region.isVisited());
        holder.regionVisited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                region.setVisited(b);
                try {
                    Observable.just(dao.update(region))
                            .subscribeOn(Schedulers.computation())
                            .subscribe(new Action1<Integer>() {
                                @Override
                                public void call(Integer integer) {
                                    if (integer != 1) {
                                        Log.d(TAG, "How the hell did you get up here, anyway?");
                                    }
                                }
                            });
                } catch (SQLException e) {
                    Log.d(TAG, "Failed updating region at pos " + position);
                    e.printStackTrace();
                }
            }
        });
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

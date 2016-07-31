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


    // CR1: Do not pass dao to adapter. Adapter should not perform queries.
    // Passing click callback is a better solution.
    // https://github.com/rohitshampur/RecyclerItemClickSupport - this lib can be used to create a callback in Activity/Fragment

    // CR1: Add extra methods for data handling:

    /*
    private final List<Region> regions = new ArrayList<>();
    public void clear() {
        regions.clear();
        notifyDataSetChanged();
    }


    public void replaceData(List<Region> newRegions) {
        regions.clear();
        regions.addAll(newRegions);
        notifyDataSetChanged();
    }

    Same for adding, modifying, etc.
    */


    public RegionAdapter(List<Region> regions, Context context, Dao<Region, Integer> dao) {
        this.regions = regions;
        this.context = context;
        this.dao = dao;

        // CR1: inflater can be created here as well.
    }


    @Override
    public RegionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.region_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RegionAdapter.ViewHolder holder, int position) {
        final Region region = regions.get(holder.getAdapterPosition());
        holder.regionImage.setImageResource(RegionUtil.getRegionIconResource(region.getOsmId()));
        holder.regionName.setText(RegionUtil.getRegionNameResource(region.getOsmId()));
        holder.regionVisited.setChecked(region.isVisited());

        // CR1: Move this click listener to ViewHolder, see ButterKnife @OnClick.
        holder.regionVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof CheckBox) {
                    CheckBox box = (CheckBox) v;
                    region.setVisited(box.isChecked());
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
                        Log.d(TAG, "Failed updating region at pos " + holder.getAdapterPosition());
                        e.printStackTrace();
                    }
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

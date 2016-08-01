package com.noveogroup.teamzolotov.iwashere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.adapters.RegionAdapter;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.RegionUtils;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RegionListFragment extends BaseDatabaseFragment implements RegionAdapter.RegionUpdateListener {

    private static final String TAG = RegionListFragment.class.getSimpleName();

    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    public static RegionListFragment newInstance() {
        RegionListFragment fragment = new RegionListFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {

        Context context = getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        final RegionAdapter adapter = new RegionAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);

        try {
            final Dao<Region, Integer> dao = openHelper.getDao();

            Observable.from(dao.queryForAll())
                    .toSortedList(new Func2<Region, Region, Integer>() {
                        @Override
                        public Integer call(final Region r1, final Region r2) {
                            int firstNameResource = RegionUtils.getRegionNameResource(r1.getOsmId());
                            int secondNameResource = RegionUtils.getRegionNameResource(r2.getOsmId());
                            return getString(firstNameResource).compareTo(getString(secondNameResource));
                        }
                    }).compose(new Observable.Transformer<List<Region>, List<Region>>() {
                @Override
                public Observable<List<Region>> call(final Observable<List<Region>> listObservable) {
                    return listObservable
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread());
                }
            }).subscribe(new Action1<List<Region>>() {
                @Override
                public void call(final List<Region> regions) {
                    adapter.replaceData(regions);
                }
            });

        } catch (SQLException e) {
            Log.d(TAG, "Failed querying for regions", e);
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_region_list;
    }

    @Override
    public void onUpdate(Region region) {
        try {
            final Dao<Region, Integer> dao = openHelper.getDao();

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
            Log.d(TAG, "Failed updating region " + region.getOsmId());
            e.printStackTrace();
        }
    }
}

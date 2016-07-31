package com.noveogroup.teamzolotov.iwashere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.adapters.RegionAdapter;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.util.RegionUtil;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RegionListFragment extends Fragment {

    private static final String TAG = RegionListFragment.class.getSimpleName();


    // CR1: Can be moved to BaseDatabaseFragment (extends BaseFragment) and acquired/released in onCreate/onDestroy

    private RegionOrmLiteOpenHelper openHelper;

    public static RegionListFragment newInstance() {
        RegionListFragment fragment = new RegionListFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // CR1: Better wrap into a FrameLayout and use Butterknife
        View view = inflater.inflate(R.layout.fragment_region_list, container, false);


        // CR1: avoid instanceof whenever possible
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (openHelper == null) {
                openHelper = OpenHelperManager.getHelper(getContext(), RegionOrmLiteOpenHelper.class);
            }
            try {
                final Dao<Region, Integer> dao = openHelper.getDao();



                /* CR1: any sequential transformation can be done using transformation operators.
                This helps avoiding deep code nesting with anonymous classes

                see related CRs in RegionAdapter

                final RegionAdapter adapter = new RegionAdapter(getActivity());
                recyclerView.setAdapter(adapter);


                Observable.from(dao.queryForAll())
                        .toSortedList(new Func2<Region, Region, Integer>() {
                            @Override
                            public Integer call(final Region region, final Region region2) {
                                // TODO: compare
                                return null;
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
                                adapter.setData(regions);
                            }
                        });*/


                Observable.just(dao.queryForAll())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<List<Region>, List<Region>>() {
                            @Override
                            public List<Region> call(List<Region> regions) {
                                Collections.sort(regions, new Comparator<Region>() {
                                    @Override
                                    public int compare(Region r1, Region r2) {
                                        int firstNameResource = RegionUtil.getRegionNameResource(r1.getOsmId());
                                        int secondNameResource = RegionUtil.getRegionNameResource(r2.getOsmId());
                                        return getString(firstNameResource).compareTo(getString(secondNameResource));
                                    }
                                });
                                return regions;
                            }
                        })
                        .subscribe(new Action1<List<Region>>() {
                            @Override
                            public void call(List<Region> regions) {
                                recyclerView.setAdapter(new RegionAdapter(regions, getContext(), dao));
                            }
                        });
            } catch (SQLException e) {
                Log.d(TAG, "Failed querying for regions", e);
                e.printStackTrace();
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        if (openHelper != null) {
            OpenHelperManager.releaseHelper();
            openHelper = null;
        }
        super.onDestroyView();
    }
}

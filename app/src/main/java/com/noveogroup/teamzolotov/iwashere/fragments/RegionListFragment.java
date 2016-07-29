package com.noveogroup.teamzolotov.iwashere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;
import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.adapters.RegionAdapter;
import com.noveogroup.teamzolotov.iwashere.model.Region;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegionListFragment extends Fragment {

    private Dao<Region, Integer> dao;
    private LinkedList<Region> changeQueue;
    private RecyclerView.Adapter adapter;

    public static RegionListFragment newInstance(Dao<Region, Integer> dao) {
        RegionListFragment fragment = new RegionListFragment();
        fragment.setRetainInstance(true);
        fragment.dao = dao;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_region_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            try {
                Observable.just(dao.queryForAll())
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<Region>>() {
                            @Override
                            public void call(List<Region> regions) {
                                recyclerView.setAdapter(new RegionAdapter(regions, getContext()));
                            }
                        });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}

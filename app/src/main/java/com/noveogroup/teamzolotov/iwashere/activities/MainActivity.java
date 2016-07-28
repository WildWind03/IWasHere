package com.noveogroup.teamzolotov.iwashere.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.noveogroup.teamzolotov.iwashere.R;

import java.util.logging.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private final static int MAP_ID = 1;
    private final static int LIST_REGIONS_ID = 2;
    private final static int SETTINGS_ID = 3;
    private final static int HELP_ID = 4;

    private final static Logger logger = Logger.getLogger(MainActivity.class.getName());

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @Override
    protected void onPostCreate(@Nullable final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        PrimaryDrawerItem mapDrawerItem = new PrimaryDrawerItem();
        mapDrawerItem
                .withIdentifier(MAP_ID)
                .withName(R.string.map_string)
                .withIcon(R.drawable.ic_map_black_24dp);

        PrimaryDrawerItem listRegionsDrawerItem = new PrimaryDrawerItem();
        listRegionsDrawerItem
                .withIdentifier(LIST_REGIONS_ID)
                .withName(R.string.regions_string)
                .withIcon(R.drawable.ic_list_black_24dp);

        SecondaryDrawerItem settingDrawerItem = new SecondaryDrawerItem();
        settingDrawerItem
                .withIdentifier(SETTINGS_ID)
                .withName(R.string.settings_string)
                .withIcon(R.drawable.ic_settings_black_24dp);

        SecondaryDrawerItem helpDrawerItem = new SecondaryDrawerItem();
        helpDrawerItem
                .withIdentifier(HELP_ID)
                .withName(R.string.help_string)
                .withIcon(R.drawable.ic_help_black_24dp);


        new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.header_view)
                .addDrawerItems(mapDrawerItem,
                        listRegionsDrawerItem,
                        new DividerDrawerItem(),
                        settingDrawerItem,
                        helpDrawerItem)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int itemId = (int) drawerItem.getIdentifier();

                        switch (itemId) {
                            case MAP_ID:
                                onMapItemSelected();
                                break;
                            case LIST_REGIONS_ID:
                                onRegionsItemSelected();
                                break;
                            case SETTINGS_ID:
                                onSettingItemSelected();
                                break;
                            case HELP_ID:
                                onHelpItemSelected();
                                break;
                        }
                        return false;
                    }
                })
                .build();

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    private void onMapItemSelected() {

    }

    private void onRegionsItemSelected() {

    }

    private void onSettingItemSelected() {

    }

    private void onHelpItemSelected() {

    }
}

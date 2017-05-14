package fraiburgo.ifc.edu.br.rubble;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import fraiburgo.ifc.edu.br.fragments.AboutFragment;
import fraiburgo.ifc.edu.br.fragments.FeedFragment;
import fraiburgo.ifc.edu.br.fragments.MapsFragment;
import fraiburgo.ifc.edu.br.fragments.TrendingFragment;
import fraiburgo.ifc.edu.br.fragments.UserFragment;

public class MainActivity extends FragmentActivity implements
        TabListener {

    public static FragmentManager fragmentManager;
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        mCollectionPagerAdapter = new CollectionPagerAdapter(
                getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener
        // for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });
        String icons[] = {"@drawable/ic_flag", "@drawable/ic_feed", "@drawable/ic_trend", "@drawable/ic_user", "@drawable/ic_about"};
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
            int imageResource = getResources().getIdentifier(icons[i], null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            actionBar.addTab(actionBar.newTab()
                    .setIcon(res)
                    .setTabListener(this));
        }
    }

    public void onTabUnselected(Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public void onTabSelected(Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabReselected(Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public class CollectionPagerAdapter extends FragmentPagerAdapter {

        final int NUM_ITEMS = 5; // number of tabs

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new MapsFragment();
                    break;
                case 1:
                    fragment = new FeedFragment();
                    break;
                case 2:
                    fragment = new TrendingFragment();
                    break;
                case 3:
                    fragment = new UserFragment();
                    break;
                case 4:
                    fragment = new AboutFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String tabLabel = null;
            switch (position) {
                case 0:
                    tabLabel = getString(R.string.google_maps_tab);
                    break;
                case 1:
                    tabLabel = getString(R.string.feed_tab);
                    break;
                case 2:
                    tabLabel = getString(R.string.trending_tab);
                    break;
                case 3:
                    tabLabel = getString(R.string.user_tab);
                    break;
                case 4:
                    tabLabel = getString(R.string.about_tab);
                    break;
            }
            return tabLabel;
        }
    }
}

package com.ranh.news;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Media> mediaList = new ArrayList<>();
    private HashMap<String, ArrayList<Media>> countryData = new HashMap<>();
    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<Media> arrayAdapter;
    private SampleReceiver sampleReceiver;
    private TextView textView;
    private boolean serviceIsRunning = false;
    public static final String MESSAGE_FROM_SERVICE = "MESSAGE_FROM_SERVICE";
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ArrayAdapter<String> parrayAdapter;
    private ViewPager pager;
    public static int screenWidth, screenHeight;
    private Media currentMedia;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);


        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectItem(position);
                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );

        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        // Load the data
        if (countryData.isEmpty()) {
            SourceLoaderRunnable clr = new SourceLoaderRunnable(this);
            new Thread(clr).start();
        }

        fragments = new ArrayList<>();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);



        sampleReceiver = new SampleReceiver(this);
        IntentFilter filter = new IntentFilter(MESSAGE_FROM_SERVICE);
        registerReceiver(sampleReceiver, filter);

        //startService();


    }

    private void startService(String sid) {
        Log.d(TAG, "startService: ");
        Intent intent = new Intent(MainActivity.this, ArticleService.class);
        intent.putExtra("source id",sid);
        startService(intent);
        serviceIsRunning = true;
    }

    private void stopService() {
        Intent intent = new Intent(MainActivity.this, ArticleService.class);
        stopService(intent);

    }

    protected void onDestroy() {
        // Unregister your receiver
        unregisterReceiver(sampleReceiver);

        // Stop the service
        stopService();

        super.onDestroy();
    }

    private void selectItem(int position) {


        pager.setBackground(null);
        currentMedia = mediaList.get(position);
        Log.d(TAG, "selectItem: "+currentMedia.getName());

        startService(currentMedia.getId());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    public void setArticles(ArrayList<Article> articleList) {


        setTitle(currentMedia.getName());

        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();


        for (int i = 0; i < articleList.size(); i++) {
            fragments.add(
                    NewsFragment.newInstance(articleList.get(i), i+1, articleList.size()));
            //pageAdapter.notifyChangeInPosition(i);
            Log.d(TAG, "setArticles: "+fragments.size());
        }

        pageAdapter.notifyDataSetChanged();
        Log.d(TAG, "setArticles: ");
        pager.setCurrentItem(0);
    }



    public void updateData(ArrayList<Media> listIn) {

        for (Media c : listIn) {
            //if (c.getSubRegion().isEmpty()) {
            //    c.setSubRegion("Unspecified");
            //}
            if (!countryData.containsKey(c.getCategory())) {
                countryData.put(c.getCategory(), new ArrayList<Media>());
            }
            ArrayList<Media> clist = countryData.get(c.getCategory());
            if (clist != null) {
                clist.add(c);
            }
        }

        countryData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(countryData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);


        mediaList.addAll(listIn);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, mediaList);


        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    // You need the 2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    // You need the below to open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        setTitle(item.getTitle());

        mediaList.clear();
        ArrayList<Media> clist = countryData.get(item.getTitle().toString());
        if (clist != null) {
            mediaList.addAll(clist);
        }

        arrayAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);

    }

    // You need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        MyPageAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }


}
